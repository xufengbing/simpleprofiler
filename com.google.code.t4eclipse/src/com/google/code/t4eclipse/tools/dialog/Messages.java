package com.google.code.t4eclipse.tools.dialog;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.google.code.t4eclipse.tools.dialog.messages"; //$NON-NLS-1$

	public static String AboutT4EclipseDialog_1;

	public static String AboutT4EclipseDialog_2;

	public static String AboutT4EclipseDialog_3;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
