package org.eclipsercp.book.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

public class SamplesModel {

	private SampleDirectory[] sampleDirs;
	
	private SampleDirectory currentSampleDir;

	public SamplesModel() {
		super();
	}
	
	/**
	 * Initialize the model from the given path. 
	 * 
	 * @param path
	 * @longOp this operation is long running and shouldn't be run from a
	 * 	responsive thread.
	 */
	public void init(String path, IProgressMonitor monitor) {
		updateProjectsList(path, monitor);
	}

	public SampleDirectory getCurrentSampleDir() {
		return currentSampleDir;
	}

	public SampleDirectory[] getSampleDirs() {
		return sampleDirs;
	}

	public static Integer getImportedSamplesId() {
		String chapterString = "0";
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			try {
				if(! project.isAccessible()) continue;
				String chap = project
						.getPersistentProperty(IConstants.CHAP_NUM_KEY);
				if (chap != null)
					chapterString = chap;
			} catch (CoreException e) {
				Utils.handleError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), e, "Error", "Error finding current chapter number.");
			}
		}
		return new Integer(chapterString);
	}

	public static SampleDirectory findSampleById(int number,
			SampleDirectory[] samplesDirectories) {
		for (int i = 0; i < samplesDirectories.length; i++) {
			SampleDirectory chap = samplesDirectories[i];
			if (chap.getChapterNum().intValue() == number)
				return chap;
		}
		return null;
	}
	
	private void updateProjectsList(final String path, IProgressMonitor monitor) {
		if (path == null || path.length() == 0) {
			return;
		}
		monitor.beginTask("Searching", 100);
		File directory = new File(path);
		sampleDirs = new SampleDirectory[0];
		SortedMap files = new TreeMap();
		monitor.worked(10);
		if (directory.isDirectory()) {
			collectProjectFilesFromDirectory(files, directory, monitor);
			sampleDirs = (SampleDirectory[]) files.values().toArray(
						new SampleDirectory[files.values().size()]);
		} else {
			monitor.worked(60);
			monitor.done();
		}
	}

	private boolean collectProjectFilesFromDirectory(Map files, File directory,
			IProgressMonitor monitor) {

		if (monitor.isCanceled())
			return false;
		monitor.subTask("Reading " + directory.getPath());
		File[] contents = directory.listFiles();
		// first look for project description files
		final String dotProject = IProjectDescription.DESCRIPTION_FILE_NAME;
		for (int i = 0; i < contents.length; i++) {
			File file = contents[i];
			if (file.isFile() && file.getName().equals(dotProject)) {
				File parentFile = directory.getParentFile();
				if (directory.getName().equals("snippets"))
					break;
				SampleDirectory chapter = (SampleDirectory) files
						.get(parentFile);
				SampleProject pr = new SampleProject(directory);
				if (chapter == null) {
					chapter = new SampleDirectory(parentFile);
					files.put(parentFile, chapter);
				}
				chapter.addProject(pr);
				// don't search sub-directories since we can't have nested
				// projects
				return true;
			}
		}
		// no project description found, so recurse into sub-directories
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].isDirectory())
				collectProjectFilesFromDirectory(files, contents[i], monitor);
		}
		return true;
	}
	
	public static String findSamplesDirectory(String bundleId, String samplesFolder) {
		Bundle bundle = Platform
				.getBundle(bundleId);
		if (bundle != null) {
				URL samplesDir = bundle
					.getEntry(samplesFolder);
			if (samplesDir != null) {
				URL url;
				try {
					url = FileLocator.resolve(samplesDir);
					if (url.getProtocol().equals("file")) {
						File samplesPath = new File(url.getFile().replace('/',
								File.separatorChar));
						return samplesPath.getAbsolutePath();
					}
				} catch (IOException e) {
			//		Utils.handleError(getShell(), e, "Error",
			//				"Problems searching samples directory");
				}
			}
		}
		//MessageDialog
		//		.openError(
		//				getShell(),
		//				"Error finding samples code",
		//				"The samples code plug-in is not installed. Please visit eclipsercp.org for instructions on how to install the latest sample code for the RCP book.");
		return "";
	}
}
