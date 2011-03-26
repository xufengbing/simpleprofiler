/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/
package com.google.code.t4eclipse.core.finder;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;

/**
 * @author xufengbing
 *
 */
public class ConditionClassFind implements IConditionFind {

	private Class classV;

	public ConditionClassFind(Class c) {
		this.classV = c;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.code.t4eclipse.core.finder.IConditionFind#check(org.eclipse.swt.widgets.Widget)
	 */
	public boolean check(Widget widget) {
		if (classV == null || widget == null)
			return false;

		Class a = widget.getClass();
		return this.classV.isAssignableFrom(a);

	}
}