/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/

package com.google.code.t4eclipse.core.swt.helper;

/**
 *
 * @author xufengbing
 */
public class WaitTimedOutError extends RuntimeException {
    private static final long serialVersionUID = 6286817949395706974L;
	public WaitTimedOutError() {
		super();
	}

	public WaitTimedOutError(String message) {
		super(message);
	}
}
