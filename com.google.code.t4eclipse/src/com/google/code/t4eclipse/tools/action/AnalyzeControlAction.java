package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.google.code.t4eclipse.selection.ControlSelection;
import com.google.code.t4eclipse.tools.dialog.AboutT4EclipseDialog;
import com.google.code.t4eclipse.tools.dialog.AnalyzeControlDialog;

public class AnalyzeControlAction implements IObjectActionDelegate {
	private Control c;

	public AnalyzeControlAction() {
		// TODO Auto-generated constructor stub
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void run(IAction action) {
		Shell shell =  Display.getDefault().getActiveShell();

		AnalyzeControlDialog d = new AnalyzeControlDialog(shell, this.c);
		d.open();
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
