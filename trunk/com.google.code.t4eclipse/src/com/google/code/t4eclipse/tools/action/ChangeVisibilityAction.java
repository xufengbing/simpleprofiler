package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.google.code.t4eclipse.core.swt.helper.HelperRoot;
import com.google.code.t4eclipse.core.utility.CommonUIUtility;
import com.google.code.t4eclipse.selection.ControlSelection;

public class ChangeVisibilityAction implements IObjectActionDelegate {
	private Control c;

	public ChangeVisibilityAction() {
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void run(IAction action) {
		if (c != null && !c.isDisposed()) {
			if (c.isVisible()) {
				c.setVisible(false);
				CommonUIUtility.delay(1*1000);
				c.setVisible(true);
			}
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.c = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object o = ss.getFirstElement();
			if (o instanceof ControlSelection) {
				ControlSelection cs = (ControlSelection) o;
				this.c = cs.getControl();
			}
		}
	
	}

}
