package org.eclipsercp.book.tools.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.progress.UIJob;
import org.eclipsercp.book.tools.IConstants;
import org.eclipsercp.book.tools.SampleProject;
import org.eclipsercp.book.tools.SampleDirectory;
import org.eclipsercp.book.tools.SamplesModel;
import org.eclipsercp.book.tools.Utils;
import org.eclipsercp.book.tools.SampleDirectory.ProjectImportDef;
import org.osgi.service.prefs.Preferences;

public class ImportSamplesOperation implements IRunnableWithProgress {

	private SampleDirectory sampleDirectory;

	private SampleDirectory[] availableSampleDirectories;

	private Shell shell;

	public ImportSamplesOperation(Shell shell, SampleDirectory sampleDirectory,
			SampleDirectory[] availableSampleDirectories) {
		this.shell = shell;
		this.sampleDirectory = sampleDirectory;
		this.availableSampleDirectories = availableSampleDirectories;
	}

	public void run(IProgressMonitor monitor) {
		if (sampleDirectory == null)
			return;

		// delete launch configurations
		ILaunchManager mng = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] launchConfigs;
		try {
			launchConfigs = mng.getLaunchConfigurations();
			for (int i = 0; i < launchConfigs.length; i++) {
				ILaunchConfiguration configuration = launchConfigs[i];
					configuration.delete();
			}
		} catch (CoreException e) {
			Utils.handleError(shell, e, "Error",
					"Problems deleting Hyperbola launch configuration");
		}

		// import projects
		try {
			importChapters(sampleDirectory, monitor);
			openDefaultEditor();
		} catch (CoreException e) {
			Utils.handleError(shell, e, "Error", "Importing projects");
		} catch (InvocationTargetException e) {
			Utils.handleError(shell, e, "Error", "Importing projects");
		} catch (InterruptedException e) {
			Utils.handleError(shell, e, "Error", "Importing projects");
		}
	}

	private void openDefaultEditor() {

		UIJob job = new UIJob("Opening editor") {
			public org.eclipse.core.runtime.IStatus runInUIThread(
					IProgressMonitor monitor) {
				IProject hyperbolaProject = ResourcesPlugin.getWorkspace()
						.getRoot().getProject("org.eclipsercp.hyperbola");
				if (hyperbolaProject != null) {
					IFile productConfig = hyperbolaProject
							.getFile("hyperbola.product");
					IFile pluginXml = hyperbolaProject.getFile("plugin.xml");
					final IFile fileToOpen = (productConfig != null && productConfig
							.exists()) ? productConfig : pluginXml;
					if (fileToOpen != null && fileToOpen.exists()) {
						IWorkbench workbench = PlatformUI.getWorkbench();
						IEditorRegistry registry = workbench
								.getEditorRegistry();
						IWorkbenchPage page = workbench
								.getActiveWorkbenchWindow().getActivePage();
						IEditorDescriptor descriptor = registry
								.getDefaultEditor(fileToOpen.getName());
						String id;
						if (descriptor == null) {
							id = "org.eclipse.ui.DefaultTextEditor"; //$NON-NLS-1$
						} else {
							id = descriptor.getId();
						}
						try {
							page
									.openEditor(
											new FileEditorInput(fileToOpen), id);
							return Status.OK_STATUS;
						} catch (PartInitException e) {
							Utils.handleError(shell, e, "Error",
									"Error opening editor");
						}
					}
				}
				return Status.OK_STATUS;
			};
		};
		job.schedule(750);
	}

	private void importChapters(final SampleDirectory firstElement,
			IProgressMonitor monitor) throws CoreException,
			InvocationTargetException, InterruptedException {
		boolean prompted = false;
		try {
			monitor.beginTask("Importing", 100);

			// deletion
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
					.getProjects();
			SubProgressMonitor subDeleteMonitor = new SubProgressMonitor(
					monitor, 30);
			subDeleteMonitor.beginTask("Deleting projects",
					projects.length * 100);
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				if (project.isAccessible()
						&& project
								.getPersistentProperty(IConstants.SAMPLES_KEY) != null) {
					if (!prompted && !promptToOverwrite())
						return;
					prompted = true;
					project.delete(true, true, new SubProgressMonitor(
							subDeleteMonitor, 100));
				}
			}
			subDeleteMonitor.done();

			// import
			SubProgressMonitor subImportMonitor = new SubProgressMonitor(
					monitor, 70);
			subImportMonitor.beginTask("Importing projects", firstElement
					.getProjects().size() * 100);

			final List importedProjects = new ArrayList();
			IWorkspaceRunnable workspaceOperation = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						for (Iterator it = firstElement.getProjects()
								.iterator(); it.hasNext();) {
							SampleProject element = (SampleProject) it.next();
							createExistingProject(firstElement, element, true,
									new SubProgressMonitor(monitor, 100));
						}

						SampleDirectory.ProjectImportDef[] imports = firstElement.getImports();
						if(imports.length > 0) {
							for (int i = 0; i < imports.length; i++) {
								ProjectImportDef importDef = imports[i];
								importOthersFrom(importDef.getSampleNumber().intValue(), 
										importDef.getProjectName(),
										firstElement, importedProjects, monitor);
							}
						}
						
						//importOthersFrom(10, "org.jivesoftware.smack",
						//		firstElement, importedProjects, monitor);
						//importOthersFrom(14,
						//		"org.eclipsercp.hyperbola.feature",
						//		firstElement, importedProjects, monitor);
					} catch (InvocationTargetException e) {
						throw new CoreException(Utils.statusFrom(e));
					} catch (InterruptedException e) {
						throw new CoreException(Utils.statusFrom(e));
					}
				}

				private void importOthersFrom(int chapNum, String chapName,
						final SampleDirectory firstElement,
						final List importedProjects, IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException,
						CoreException {

					SampleDirectory chap = SamplesModel
							.findSampleById(
									chapNum,
									ImportSamplesOperation.this.availableSampleDirectories);
					if (chap != null) {
						for (Iterator it = chap.getProjects().iterator(); it
								.hasNext();) {
							SampleProject project = (SampleProject) it.next();
							IProject current = ResourcesPlugin
									.getWorkspace()
									.getRoot()
									.getProject(
											project.projectSystemFile.getName());
							importedProjects.add(current);
							if (current.getName().endsWith(chapName)) {
								if (current.exists() && !current.isOpen()) {
									current.open(null);
								} else {
									createExistingProject(
											chap,
											project,
											false,
											new SubProgressMonitor(monitor, 100));
								}
							}
						}
					}

				}
			};
			ResourcesPlugin.getWorkspace().run(workspaceOperation,
					(ISchedulingRule) ResourcesPlugin.getWorkspace().getRoot(),
					IWorkspace.AVOID_UPDATE,
					(IProgressMonitor) subImportMonitor);

			// HACK - to ensure that org.eclipsercp.hyperbola doens't have
			// compile errors, close and re-open it.
			try {
				IJobManager jobManager = Platform.getJobManager();
				jobManager.join(ResourcesPlugin.FAMILY_MANUAL_BUILD, monitor);
				jobManager.join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);

				IWorkspaceRunnable closeRunnable = new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor)
							throws CoreException {
						boolean reopen = false;
						for (Iterator it = importedProjects.iterator(); it
								.hasNext();) {
							IProject project = (IProject) it.next();
							if (project.exists() && existsProblems(project)) {
								reopen = true;
							}
						}
						if (reopen) {
							IProject[] projects = ResourcesPlugin
									.getWorkspace().getRoot().getProjects();
							for (int i = 0; i < projects.length; i++) {
								IProject project = projects[i];
								project.close(new NullProgressMonitor());
							}
						}

					}
				};
				IWorkspaceRunnable openRunnable = new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor)
							throws CoreException {

						IProject[] projects = ResourcesPlugin.getWorkspace()
								.getRoot().getProjects();
						for (int i = 0; i < projects.length; i++) {
							IProject project = projects[i];
							project.open(new NullProgressMonitor());
						}
					}
				};
				ResourcesPlugin.getWorkspace().run(closeRunnable,
						new NullProgressMonitor());
				ResourcesPlugin.getWorkspace().run(openRunnable,
						new NullProgressMonitor());

			} catch (InterruptedException e) {
				// just continue.
			}

			subImportMonitor.done();
		} finally {
			monitor.done();
		}
	}

	/**
	 * Returns whether the given project contains any problem markers of the
	 * specified severity.
	 * 
	 * @param proj
	 *            the project to search
	 * @return whether the given project contains any problems that should stop
	 *         it from launching
	 * @throws CoreException
	 *             if an error occurs while searching for problem markers
	 */
	protected boolean existsProblems(IProject proj) throws CoreException {
		IMarker[] markers = proj.findMarkers(IMarker.PROBLEM, true,
				IResource.DEPTH_INFINITE);
		if (markers.length > 0) {
			for (int i = 0; i < markers.length; i++) {
				if (isLaunchProblem(markers[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns whether the given problem should potentially abort the launch. By
	 * default if the problem has an error severity, the problem is considered a
	 * potential launch problem. Subclasses may override to specialize error
	 * detection.
	 * 
	 * @param problemMarker
	 *            candidate problem
	 * @return whether the given problem should potentially abort the launch
	 * @throws CoreException
	 *             if any exceptions occurr while accessing marker attributes
	 */
	protected boolean isLaunchProblem(IMarker problemMarker)
			throws CoreException {
		Integer severity = (Integer) problemMarker
				.getAttribute(IMarker.SEVERITY);
		if (severity != null) {
			return severity.intValue() >= IMarker.SEVERITY_ERROR;
		}

		return false;
	}

	public boolean promptToOverwrite() {
		Preferences prefs = new InstanceScope()
				.getNode(IConstants.PLUGIN_ID);
		boolean defaultValue = prefs.getBoolean(
				IConstants.PROMPT_OVERWRITE_PREF, true);
		if (!defaultValue)
			return true;
		final boolean[] retVal = { false };
		shell.getDisplay().syncExec(new Runnable() {
			public void run() {
				ScopedPreferenceStore store = new ScopedPreferenceStore(
						new InstanceScope(), IConstants.PLUGIN_ID);
				Dialog d = MessageDialogWithToggle
						.openOkCancelConfirm(
								shell,
								"Warning",
								"This will delete all imported projects and in your workspace and replace them with the projects for the selected samples. Are you sure about this?",
								"Don't show this message again", false, store,
								IConstants.PROMPT_OVERWRITE_PREF);
				retVal[0] = d.getReturnCode() == Dialog.OK;
				try {
					store.save();
				} catch (IOException e) {
					Utils.handleError(shell, e, "Error",
							"Problems saving preferences");
				}
			}
		});
		return retVal[0];
	}

	/**
	 * Create the project described in record. If it is successful return true.
	 * 
	 * @param record
	 * @return boolean <code>true</code> of successult
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	private boolean createExistingProject(final SampleDirectory chap,
			final SampleProject record, final boolean updateProperty,
			IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Importing", 2000);

		String projectName = record.projectSystemFile.getName();
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject project = workspace.getRoot().getProject(projectName);
		final IProjectDescription description = workspace
				.newProjectDescription(projectName);
		URL instanceLocation = Platform.getInstanceLocation().getURL();
		File projectDir = new File(instanceLocation.getPath(), projectName);
		try {
			Utils.copy(record.projectSystemFile, projectDir, true, monitor);

			description.setLocation(null);
			if (project.exists()) {
				if (!project.isOpen())
					project.open(null);
				project.delete(true, null);
			}
			project.create(description, new SubProgressMonitor(monitor, 1000));
			if (monitor.isCanceled())
				throw new OperationCanceledException();
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
					monitor, 1000));
			if (updateProperty)
				project.setPersistentProperty(
						IConstants.CHAP_NUM_KEY, chap
								.getChapterNum().toString());
			project.setPersistentProperty(IConstants.SAMPLES_KEY,
					"rcpbook-project");
		} catch (Exception e) {
			Utils.handleError(shell, e, "Error", "Problem importing projects");
		}

		return true;
	}
}
