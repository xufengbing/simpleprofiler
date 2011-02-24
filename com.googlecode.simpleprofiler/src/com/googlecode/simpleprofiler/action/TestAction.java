package com.googlecode.simpleprofiler.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.googlecode.simpleprofiler.model.LogUtility;

public class TestAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		System.out.println(LogUtility.getDefault().getMapSize());
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
