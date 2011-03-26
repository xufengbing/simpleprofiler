package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.tools.view.MainView;

public class RefreshMainViewAction implements IViewActionDelegate {

	public void run(IAction action) {
		// TODO Auto-generated method stub

	}

	public void selectionChanged(IAction action, ISelection selection) {
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part != null && part instanceof MainView) {
			MainView m = (MainView) part;
			m.updateView();

		}
	}

	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

}
