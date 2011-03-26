package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.MenuUtil;

import com.google.code.t4eclipse.core.utility.MenuUtility;
import com.google.code.t4eclipse.tools.view.MenuAnalyzerView;

public class AnalyzeMainMenuAction implements IViewActionDelegate {

 

	public void run(IAction action) {
		Menu mainMenu = MenuUtility.getMainMenu();
		try {
			IViewPart view = PlatformUI
					.getWorkbench()
					.getActiveWorkbenchWindow()
					.getActivePage()
					.showView(
							"com.google.code.t4eclipse.tools.view.MenuAnalyzerView");
			if (view != null && view instanceof MenuAnalyzerView) {
				MenuAnalyzerView ma = (MenuAnalyzerView) view;
				ma.update(mainMenu);
			}
		} catch (PartInitException e) {
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

	public void init(IViewPart view) {
		// TODO Auto-generated method stub
		
	}

}
