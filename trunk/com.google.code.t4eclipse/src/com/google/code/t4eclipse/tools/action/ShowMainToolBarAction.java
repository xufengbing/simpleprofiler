package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.google.code.t4eclipse.core.utility.ToolBarUtility;
import com.google.code.t4eclipse.tools.view.ToolBarAnalyzerView;

public class ShowMainToolBarAction implements IViewActionDelegate {

	private ToolBarAnalyzerView view;

	public void init(IViewPart view) {
		this.view = (ToolBarAnalyzerView) view;

	}

	public void run(IAction action) {

		CoolBar cb = ToolBarUtility.getEclipseCoolBar();
		this.view.update(cb);

	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
