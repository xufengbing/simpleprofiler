package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class OpenToolItemJavaTypeAction implements IViewActionDelegate {
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
			if (o != null && o instanceof ToolItem) {
				ToolItem mi = (ToolItem) o;
				String str = (String) mi.getData("JAVA_TYPE");
				if (str != null) {
					this.className = str;
				}
			}

		}
	}

}
