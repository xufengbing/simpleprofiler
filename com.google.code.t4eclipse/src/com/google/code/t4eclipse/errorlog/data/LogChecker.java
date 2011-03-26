package com.google.code.t4eclipse.errorlog.data;

import java.util.concurrent.atomic.AtomicBoolean;

public class LogChecker {
	private static AtomicBoolean checkLogError = new AtomicBoolean(false);

	public  static void setCheck(boolean check) {
		checkLogError.set(check);
	}

	public  static boolean getCheck() {
		return checkLogError.get();
	}
}
