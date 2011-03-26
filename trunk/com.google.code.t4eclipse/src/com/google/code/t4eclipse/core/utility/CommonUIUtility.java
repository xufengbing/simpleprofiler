package com.google.code.t4eclipse.core.utility;

import org.eclipse.swt.widgets.Display;

public class CommonUIUtility {

	protected static void checkTime(long time) {
		if (time < 0L)
			throw new IllegalArgumentException("time is negative");
	}

	protected static void checkThread() {
		if (isOnDisplayThread())
			throw new RuntimeException("invalid thread!");
	}

	/** Sleep the given duration of time (in milliseconds). */
	public static void sleep(long time) {
		checkThread();
		sleepPrim(time);
	}

	private static void sleepPrim(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException exception) {
			// Empty block intended.
		}
	}

	protected static boolean isOnDisplayThread() {
		return getDisplay().getThread() == Thread.currentThread();
	}

	/** Delay the given duration of time (in milliseconds). */
	public static void delay(long time) {
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

	public static Display getDisplay() {
		return Display.getDefault();
	}
}
