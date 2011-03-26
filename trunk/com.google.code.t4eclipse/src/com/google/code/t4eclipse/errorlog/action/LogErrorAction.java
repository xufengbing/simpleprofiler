package com.google.code.t4eclipse.errorlog.action;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
 

import com.google.code.t4eclipse.errorlog.data.LogChecker;
import com.google.code.t4eclipse.tools.dialog.InternalErrorDialog;

public class LogErrorAction implements IWorkbenchWindowActionDelegate {
	ILogListener listener;
	public void dispose() {
      if(listener!=null){
    	  Platform.removeLogListener(listener);
      }
	}

	public void init(IWorkbenchWindow window) {
		
		  listener = new ILogListener() {
			public void logging(IStatus status, String plugin) {
				
				if (LogChecker.getCheck()) {
					if (IStatus.ERROR == status.getSeverity()) {
						String message = "Error Message:" + status.getMessage();
						// logging method maybe invokedin a non UI thread.
						// so all UI operation in openQuestion should be run in
						// UI thread
						openMRTErrorDialog("Error For MRT", message, status
								.getException(), status);
					}

				}
			}
		};
		Platform.addLogListener(listener);

	}

	//
	public void run(IAction action) {
		LogChecker.setCheck(action.isChecked());

	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	public static void openMRTErrorDialog(final String title,
			final String message, final Throwable detail, final IStatus status) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				Shell parent = PlatformUI.getWorkbench().getWorkbenchWindows()[0]
						.getShell();
				String[] labels;
				if (detail != null) {
					labels = new String[] { IDialogConstants.OK_LABEL,
							IDialogConstants.SHOW_DETAILS_LABEL };
					InternalErrorDialog dialog = new InternalErrorDialog(
							parent, title, null, message, detail, 1, labels, 1);
					dialog.setDetailButton(1);
					dialog.open();
				} else {
					ErrorDialog.openError(parent, title, message, status);
				}
			}
		});

	}
}
