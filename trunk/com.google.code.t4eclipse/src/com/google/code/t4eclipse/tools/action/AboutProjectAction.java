package com.google.code.t4eclipse.tools.action;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.google.code.t4eclipse.tools.dialog.AboutT4EclipseDialog;

public class AboutProjectAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		Shell shell=new Shell(Display.getDefault());
		 
		AboutT4EclipseDialog d = new AboutT4EclipseDialog(shell);
		d.setBlockOnOpen(false);
		
 		d.open();
	 
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
