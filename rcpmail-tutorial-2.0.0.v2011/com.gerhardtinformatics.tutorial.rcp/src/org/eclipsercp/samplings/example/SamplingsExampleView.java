package org.eclipsercp.samplings.example;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipsercp.book.tools.SampleDirectory;
import org.eclipsercp.book.tools.SampleDirectoryLabelProvider;
import org.eclipsercp.book.tools.SamplesModel;
import org.eclipsercp.book.tools.Utils;
import org.eclipsercp.book.tools.actions.CompareSamplesOperation;
import org.eclipsercp.book.tools.actions.ImportSamplesOperation;

public class SamplingsExampleView extends ViewPart {

	private static final String LOCATION_OF_SAMPLES = 
		"com.gerhardtinformatics.tutorial.rcp";

	/*
	 * Button Ids
	 */
	private static final int IMPORT_ID = 101;

	private static final int COMPARE_ID = 102;

	/*
	 * Main widgets
	 */
	private TableViewer projectsList;

	private SamplesModel samplesModel;

	private Action importAction;

	private Action compareAction;

	public SamplingsExampleView() {
	}

	public void createPartControl(Composite parent) {
		Composite workArea = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		workArea.setLayout(layout);
		workArea.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		createProjectsList(workArea);
		Dialog.applyDialogFont(workArea);
		updateProjectsList();
		projectsList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				buttonPressed(IMPORT_ID);
			}
		});
		fillActionBars(projectsList.getControl());
	}

	protected void fillActionBars(Control control) {
		IActionBars bars = getViewSite().getActionBars();
		importAction = new Action("&Import") {
			public void run() {
				buttonPressed(IMPORT_ID);
			}
		};

		importAction
				.setToolTipText("Import the selected sample code into the Eclipse workspace.");
		compareAction = new Action("&Compare with Workspace") {
			public void run() {
				buttonPressed(COMPARE_ID);
			}
		};

		compareAction
				.setToolTipText("Compare the projects in your workspace with the currently selected sample code.");
		IToolBarManager toolbar = bars.getToolBarManager();
		toolbar.add(importAction);
		toolbar.add(compareAction);
		IMenuManager menu = bars.getMenuManager();
		menu.add(importAction);
		menu.add(compareAction);
		MenuManager contextMenu = new MenuManager();
		contextMenu.add(importAction);
		contextMenu.add(compareAction);
		control.setMenu(contextMenu.createContextMenu(control));
	}

	protected Shell getShell() {
		return getSite().getShell();
	}

	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case IMPORT_ID:
			createProjects();
			projectsList.update(samplesModel.getSampleDirs(), null);
			break;
		case COMPARE_ID:
			new CompareSamplesOperation().run(getSelection());
			break;
		}
	}

	private void createProjects() {
		final ImportSamplesOperation operation = new ImportSamplesOperation(
				getShell(), getSelection(), samplesModel.getSampleDirs());
		Utils.run(getShell(), operation);
	}

	/**
	 * Create the checkbox list for the found projects.
	 * 
	 * @param workArea
	 */
	private void createProjectsList(Composite listComposite) {

		Label title = new Label(listComposite, SWT.NONE);
		title.setText("Sample Code:");

		projectsList = new TableViewer(listComposite, SWT.BORDER | SWT.SINGLE);
		GridData listData = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
		listData.heightHint = 125;
		listData.widthHint = 100;
		projectsList.getControl().setLayoutData(listData);
		projectsList.setSorter(new ViewerSorter() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				SampleDirectory c1 = (SampleDirectory) e1;
				SampleDirectory c2 = (SampleDirectory) e2;
				return c1.getChapterNum().compareTo(c2.getChapterNum());
			}
		});
		projectsList.setContentProvider(new ArrayContentProvider());
		projectsList.setLabelProvider(new SampleDirectoryLabelProvider(
				projectsList));
		projectsList.getControl().setFocus();
	}

	/**
	 * Update the list of projects based on path
	 * 
	 * @param path
	 */
	protected void updateProjectsList() {
		Job job = new Job("Finding Samples") {
			protected org.eclipse.core.runtime.IStatus run(
					IProgressMonitor monitor) {
				String path = SamplesModel
						.findSamplesDirectory(LOCATION_OF_SAMPLES, "samples");
				samplesModel = new SamplesModel();
				samplesModel.init(path, monitor);
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (projectsList != null
								&& !projectsList.getControl().isDisposed())
							projectsList.setInput(samplesModel.getSampleDirs());
					}
				});
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private SampleDirectory getSelection() {
		IStructuredSelection selected = (IStructuredSelection) projectsList
				.getSelection();
		if (selected != null && selected.size() == 1) {
			return (SampleDirectory) ((IStructuredSelection) selected)
					.getFirstElement();
		}
		return null;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		projectsList.getControl().setFocus();
	}
}