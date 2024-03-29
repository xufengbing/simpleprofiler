package com.googlecode.simpleprofiler.action;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.googlecode.simpleprofiler.Activator;
import com.googlecode.simpleprofiler.util.InstrumentUtility;
import com.googlecode.simpleprofiler.util.SelectionUtility;

public class InstrumentProjectAction implements IObjectActionDelegate {

	private Shell shell;

	ISelection sel;

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * 
	 */
	public void run(IAction action) {
		// only instrument the first now
		if (this.sel != null) {
			IJavaProject[] javaPs = SelectionUtility
					.getJavaProjectsFromSelection(this.sel);

			for (IJavaProject javaP : javaPs) {
				if (javaP != null) {
					try {
 				InstrumentUtility.instrumentJavaProject(javaP);
						
//						InstrumentUtility.buildProject(javaP);
					} catch (Exception e) {
						Activator.getDefault().logError(
								"Error on instrument project:"
										+ javaP.getProject().getName(), e);
		 

					}
				}
			}
		}
		MessageDialog.openInformation(shell, "ExamplePlugin",
				"New Action was executed.");
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.sel = selection;
	}

}
