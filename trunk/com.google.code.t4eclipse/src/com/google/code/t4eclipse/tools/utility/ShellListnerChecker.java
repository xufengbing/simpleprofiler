package com.google.code.t4eclipse.tools.utility;

import java.util.concurrent.atomic.AtomicBoolean;

public class ShellListnerChecker {
	private static AtomicBoolean checkLogError = new AtomicBoolean(false);

	public static void setCheck(boolean check) {
		checkLogError.set(check);
	}

	public static boolean getCheck() {
		return checkLogError.get();
	}
}
