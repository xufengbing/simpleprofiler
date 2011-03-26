package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.google.code.t4eclipse.selection.ControlSelection;
import com.google.code.t4eclipse.tools.view.ActivePartControlView;

public class OpenControlJavaCLass implements IViewActionDelegate {

	private String className;

	public void init(IViewPart view) {

	}

	public void run(IAction action) {
		OpenJavaTypeAction.openJavaType(this.className);
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.className = "";
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection) selection;
			Object o = s.getFirstElement();
			if (o != null && o instanceof ControlSelection) {
				ControlSelection mi = (ControlSelection) o;
				if (mi != null) {
					this.className = mi.getClassName();
				}
			}

		}
	}

}
