package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.code.t4eclipse.core.finder.Finder;
import com.google.code.t4eclipse.core.utility.ReflectionUtil;

public class OpenJavaTypeAction {

	private String cName;

	public OpenJavaTypeAction(String className) {
		this.cName = className;
	}

	public void run() {

		openJavaType(this.cName);

	}

	public static void openJavaType(final String className) {
		Listener listener = null;
		try {
			listener = new Listener() {
				public void handleEvent(Event event) {
					if (event.widget != null && event.widget instanceof Shell) {
						Shell shell = (Shell) event.widget;
						if (!shell.isDisposed()) {
							// why use start with? because eclispe 3.2 is
							// OpenTypeSelectionDialog2, eclipse 3.3 is
							// OpenTypeSelectionDialog
							if (shell.getData() != null
									&& shell.getData().getClass()
											.getSimpleName().startsWith(
													"OpenTypeSelectionDialog")) {
								Text text = (Text) Finder.getDefault()
										.findOneByClass(shell, Text.class);
								text.setText(className);
							}
						}
					}

				}
			};
			Display.getDefault().addFilter(SWT.Activate, listener);

			Class<?> action = Class
					.forName("org.eclipse.jdt.internal.ui.actions.OpenTypeAction");

			ReflectionUtil.invokeMethod("run", action.newInstance(),
					IAction.class);
			// new OpenTypeAction().run();

		} catch (Exception e) {
			// JDT is not installed
			MessageDialog
					.openWarning(
							Display.getDefault().getActiveShell(),
							"Warning!",
							"Can not open type:"
									+ className
									+ "\nJDT is not installed!\n Can not directly open java class!\n");
			

		} finally {
			if (listener != null) {
				Display.getDefault().removeFilter(SWT.Activate, listener);
			}

		}
	}

}
