package com.google.code.t4eclipse.tools.action;


import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

import com.google.code.t4eclipse.tools.view.MainView;

public class UpdataTabsAction extends ActionDelegate implements
		IViewActionDelegate {

	/**
	 * @see ActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part != null && part instanceof MainView) {
			MainView m = (MainView) part;
			m.updateView();

		}

	}

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
	}

}
