package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.google.code.t4eclipse.tools.utility.MainViewTabUtility;
import com.google.code.t4eclipse.tools.view.MainView;

public class OpenViewAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}

	public void run(IAction action) {
		MainView m = (MainView) MainViewTabUtility.openFocusT4EclipseView();
		m.updateView();
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
