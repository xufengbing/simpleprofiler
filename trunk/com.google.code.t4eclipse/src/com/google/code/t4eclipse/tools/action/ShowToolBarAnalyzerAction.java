package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.google.code.t4eclipse.tools.utility.EclipseCommonUtility;

public class ShowToolBarAnalyzerAction implements
		IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		EclipseCommonUtility
		.showView("com.google.code.t4eclipse.tools.view.ToolBarAnalyzerView");

	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
