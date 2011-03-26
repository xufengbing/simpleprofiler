/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/

package com.google.code.t4eclipse.core.swt.helper;

import java.util.Stack;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import com.google.code.t4eclipse.core.swt.helper.Displays.BooleanResult;
import com.google.code.t4eclipse.core.swt.helper.Displays.Result;

public class ShellHelper extends HelperRoot {

	public static final long DEFAULT_SHELL_INTERVAL = 200;
	public static final long DEFAULT_SHELL_TIMEOUT = 30 * 1000;

	public static ShellHelper getDefault() {
		return new ShellHelper();
	}

	public Shell waitShellTitle(final String title, final long timeout,
			final long interval) {
		final Shell[] ss = new Shell[1];
		Condition condition1 = new Condition() {
			public boolean test() {
				return hasShellWithTitle(title, ss);
			}
		};
		wait(condition1, timeout, interval);
		return ss[0];
	}

	public Shell waitShellTitle(final String title) {
		return waitShellTitle(title, DEFAULT_SHELL_TIMEOUT,
				DEFAULT_SHELL_INTERVAL);
	}

	public void waitNoShellTitle(final String title, final long timeout,
			final long interval) {
		Condition condition1 = new Condition() {
			public boolean test() {

				return !hasShellWithTitle(title, null);
			}
		};
		wait(condition1, timeout, interval);
	}

	public void waitNoShellTitle(final String title) {
		waitShellTitle(title, DEFAULT_SHELL_TIMEOUT, DEFAULT_SHELL_INTERVAL);
	}

	public Shell waitShellNameOfShellData(final String name,
			final long timeout, final long interval, final boolean isFullName) {
		final Shell[] ss = new Shell[1];
		Condition condition1 = new Condition() {
			public boolean test() {
				return hasShellWithClassName(name, ss, isFullName);
			}
		};
		wait(condition1, timeout, interval);
		return ss[1];
	}

	public Shell waitShellSimpleNameOfShellData(final String simpleName) {
		return waitShellNameOfShellData(simpleName, DEFAULT_SHELL_TIMEOUT,
				DEFAULT_SHELL_INTERVAL, false);
	}

	public Shell waitShellFullNameOfShellData(final String fullName) {
		return waitShellNameOfShellData(fullName, DEFAULT_SHELL_TIMEOUT,
				DEFAULT_SHELL_INTERVAL, true);
	}

	public void waitNoShellDataWithClassName(final String name,
			final long timeout, final long internal, final boolean isFullName) {
		Condition condition1 = new Condition() {
			public boolean test() {
				return !hasShellWithClassName(name, null, isFullName);
			}
		};
		wait(condition1, timeout, internal);

	}

	public void waitNoShellDataWithSimpleClassName(final String name) {
		waitNoShellDataWithClassName(name, DEFAULT_SHELL_TIMEOUT,
				DEFAULT_SHELL_INTERVAL, false);
	}

	public void waitNoShellDataWithFullClassName(final String name) {
		waitNoShellDataWithClassName(name, DEFAULT_SHELL_TIMEOUT,
				DEFAULT_SHELL_INTERVAL, true);
	}

	public Shell getShellFromDisplay(final String text) {

		return (Shell) syncExec(new Result() {
			public Object result() {

				Shell[] ss = Display.getDefault().getShells();
				Stack<Shell> stack = new Stack<Shell>();
				for (Shell shell : ss) {
					if (shell.getText().equals(text))
						return shell;
					stack.push(shell);
				}
				while (!stack.isEmpty()) {
					Shell tmpShell = stack.pop();
					if (tmpShell != null) {
						if (tmpShell.getText().equals(text))
							return tmpShell;
						Shell[] tmpShells = tmpShell.getShells();
						for (Shell shellshell : tmpShells)
							stack.add(shellshell);
					}
				}
				return null;

			}
		});

	}

	/**
	 * Proxy for {@link Shell#getShells()}. <p/>
	 *
	 * @param shell
	 *            the shell under test.
	 * @return the child shells.
	 */
	public Shell[] getShells(final Shell shell) {
		checkWidget(shell);
		return (Shell[]) syncExec(new Result() {
			public Object result() {
				return shell.getShells();
			}
		});
	}

	public Shell getActiveShell() {
		return (Shell) syncExec(new Result() {
			public Object result() {
				return Display.getDefault().getActiveShell();
			}
		});
	}

	public boolean hasActiveShellWithTitle(final String title) {
		return syncExec(new BooleanResult() {
			public boolean result() {

				Shell activeShell = Display.getDefault().getActiveShell();
				if (activeShell != null && !activeShell.isDisposed()
						&& activeShell.getText() != null
						&& activeShell.getText().equals(title)) {
					return true;
				}
				return false;
			}
		});
	}

	public boolean hasShellWithTitle(final String title, final Shell[] ss) {
		return syncExec(new BooleanResult() {
			public boolean result() {
				Shell[] shells = Display.getDefault().getShells();
				for (Shell s : shells) {
					if (s != null && !s.isDisposed() && s.getText() != null
							&& s.getText().equals(title)) {
						if (ss != null && ss.length == 1) {
							ss[0] = s;
						}
						return true;
					}

				}
				return false;
			}
		});
	}

	public boolean hasShellWithClassName(final String name, final Shell[] ss,
			final boolean isFullName) {
		return syncExec(new BooleanResult() {
			public boolean result() {
				Shell[] shells = Display.getDefault().getShells();
				for (Shell s : shells) {
					if (s != null && !s.isDisposed() && s.getData() != null) {
						if (checkShellClassName(s, name, isFullName)) {
							if (ss != null && ss.length == 1) {
								ss[0] = s;
							}
							return true;
						}
					}
				}
				return false;
			}

			private boolean checkShellClassName(Shell s, String name,
					boolean isFullName) {
				String className = isFullName ? s.getData().getClass()
						.getName() : s.getData().getClass().getSimpleName();
				return name.equals(className);
			}
		});
	}

	/**
	 * This method will see if a Shell has a modal style. SWT.SYSTEM_MODAL |
	 * SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL
	 *
	 * @param shell
	 * @return true if the Shell has a modal style.
	 */
	public boolean isModal(final Shell shell) {
		checkWidget(shell);
		return syncExec(new BooleanResult() {
			public boolean result() {
				int style = shell.getStyle();
				if (style <= 0)
					return false;
				int bitmask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL
						| SWT.PRIMARY_MODAL;
				if ((style & bitmask) > 0)
					return true;
				return false;
			}
		});
	}

	@Override
	protected void checkInstance(Widget widget) {
		boolean b = widget instanceof Shell;
		if (!b) {
			throw new RuntimeException("widget is not a Button Type");
		}

	}
}
