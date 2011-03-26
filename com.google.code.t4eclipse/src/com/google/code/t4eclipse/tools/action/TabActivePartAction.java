package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.google.code.t4eclipse.tools.utility.MainViewTabUtility;

public class TabActivePartAction implements IViewActionDelegate {

	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		MainViewTabUtility.showTabInT4EclipseView("ActivePart");
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		

	}

}
