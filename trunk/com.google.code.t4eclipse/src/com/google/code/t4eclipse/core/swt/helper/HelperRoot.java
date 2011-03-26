/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/

package com.google.code.t4eclipse.core.swt.helper;

import java.util.concurrent.atomic.AtomicBoolean;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.google.code.t4eclipse.core.swt.helper.Displays.BooleanResult;
import com.google.code.t4eclipse.core.swt.helper.Displays.CharResult;
import com.google.code.t4eclipse.core.swt.helper.Displays.IntResult;
import com.google.code.t4eclipse.core.swt.helper.Displays.Result;
import com.google.code.t4eclipse.core.swt.helper.Displays.StringResult;
import com.google.code.t4eclipse.core.utility.GuilibUtility;

/**
 *
 * @author xufengbing
 */
public abstract class HelperRoot {

	public static final long DEFAULT_WAIT_TIMEOUT = 30000L;

	public static final long DEFAULT_WAIT_INTERVAL = 10L;

	AtomicBoolean b = new AtomicBoolean(true);

	protected void fireTreeSelectEvent(Widget widget, Widget item) {
		Event e = new Event();
		e.widget = widget;
		e.type = SWT.Selection;
		e.item = item;
		widget.notifyListeners(e.type, e);
	}

	protected void fireTreeExpandEvent(Widget widget, Widget item) {
		Event event = new Event();
		event.type = SWT.Expand;
		event.widget = widget;
		event.item = item;
		widget.notifyListeners(event.type, event);

	}

	protected void fireTreeDefaultSelectEvent(Widget widget, Widget item) {
		Event e = new Event();
		e.widget = widget;
		e.type = SWT.DefaultSelection;
		e.item = item;
		widget.notifyListeners(e.type, e);
	}

	/**
	 * focus is a little different.
	 *
	 * @param widget
	 */

	public boolean actionFocus(Widget widget) {
		boolean returnV = false;
		checkWidget(widget);
		if (widget instanceof Control) {
			returnV = focus((Control) widget);
			waitForIdle();

		}
		return returnV;

	}

	public boolean focus(final Control control) {
		checkWidget(control);
		return syncExec(new BooleanResult() {
			public boolean result() {
				return control.setFocus();
			}
		});

	}

	private void waitForIdle() {
		final Display display = getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				while (display.readAndDispatch())
					; // Null statement intended.
			}

		});
	}

	protected void actionWaitForIdle() {
		waitForIdle();
	}

	protected void checkThread() {
		if (isOnDisplayThread())
			throw new RuntimeException("invalid thread!");
	}

	protected boolean isOnDisplayThread() {
		return getDisplay().getThread() == Thread.currentThread();
	}

	protected void checkWidget(Widget widget) {
		if (widget == null)
			throw new IllegalArgumentException("widget is null!");

		checkInstance(widget);
		if (widget.isDisposed())
			throw new RuntimeException("widget is disposed!");
	}

	/*
	 * Utility syncExec()-like methods.
	 */
	public Display getDisplay() {
		return Display.getDefault();
	}

	/**
	 * sycExec
	 *
	 * @param runnable
	 */

	public void syncExec(Runnable runnable) {
		sleepInterval();
		Displays.syncExec(getDisplay(), runnable);
	}

	public void sleepInterval() {
		Displays.syncExec(getDisplay(), new Runnable() {

			public void run() {
				b.set(true);
				new Thread(new Runnable() {

					public void run() {
						try {
							Thread.sleep(GuilibUtility.TIME_INTERVAL);
						} catch (InterruptedException e) {
							b.set(false);
						}
						b.set(false);
					}
				}) {
				}.start();
				while (b.get())
					getDisplay().readAndDispatch();
			}
		});
	}

	public Object syncExec(Result result) {
		return Displays.syncExec(getDisplay(), result);
	}

	public int syncExec(IntResult result) {
		return Displays.syncExec(getDisplay(), result);
	}

	public boolean syncExec(BooleanResult result) {
		return Displays.syncExec(getDisplay(), result);
	}

	public char syncExec(CharResult result) {
		return Displays.syncExec(getDisplay(), result);
	}

	public String syncExec(StringResult result) {
		return Displays.syncExec(getDisplay(), result);
	}

	public void wait(Condition condition) {
		wait(condition, DEFAULT_WAIT_TIMEOUT, DEFAULT_WAIT_INTERVAL);
	}

	/**
	 * Wait for the specified Condition to become true.
	 *
	 * @throws WaitTimedOutError
	 *             if the timeout is exceeded.
	 */
	public void wait(Condition condition, long timeout, long interval) {
		// checkThread();
		checkTime(timeout);
		checkTime(interval);
		long limit = System.currentTimeMillis() + timeout;
		do {
			sleepPrim(interval);
			if (System.currentTimeMillis() > limit)
				throw new WaitTimedOutError();
		} while (!condition.test());
	}

	protected void checkTime(long time) {
		if (time < 0L)
			throw new IllegalArgumentException("time is negative");
	}

	/** Sleep the given duration of time (in milliseconds). */
	public void sleep(long time) {
		checkThread();
		sleepPrim(time);
	}

	private void sleepPrim(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException exception) {
			// Empty block intended.
		}
	}

	/** Delay the given duration of time (in milliseconds). */
	public void delay(long time) {
		if (!isOnDisplayThread())
			throw new RuntimeException("invalid thread");
		checkTime(time);

		// Have done[0] set to true when the time has elapsed.
		final boolean[] done = new boolean[1];
		Display display = getDisplay();
		display.timerExec((int) time, new Runnable() {
			public void run() {
				done[0] = true;
			}
		});

		while (!done[0]) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	protected abstract void checkInstance(Widget widget);

}
