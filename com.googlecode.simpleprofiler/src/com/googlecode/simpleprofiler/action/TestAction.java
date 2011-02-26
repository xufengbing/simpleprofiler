package com.googlecode.simpleprofiler.action;

import java.net.URISyntaxException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.googlecode.simpleprofiler.Activator;
import com.googlecode.simpleprofiler.model.LogUtility;
import com.googlecode.simpleprofiler.util.InstrumentUtility;

public class TestAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {

		System.out.println("test index:" + LogUtility.getDefault().getIndex());

	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
