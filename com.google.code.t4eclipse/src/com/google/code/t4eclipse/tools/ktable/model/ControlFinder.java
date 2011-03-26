/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/
package com.google.code.t4eclipse.tools.ktable.model;

import java.util.Stack;
import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author xufengbing
 * 
 */

class ControlFinder {
	Stack<Control> s;

	public ControlFinder() {
		s = new Stack<Control>();
	}

	private void addStack(Control[] controls) {
		if (controls != null) {
			for (int i = controls.length - 1; i >= 0; i--) {
				if (controls[i] != null) {
					s.push(controls[i]);
				}
			}

		}
	}

	private void addControl(Control c) {

		if (c instanceof Composite) {
			Composite cont = (Composite) c;
			Control[] children = cont.getChildren();
			addStack(children);
		}
	}

	public Vector<Control> getAllChilds(Control root) {

		Vector<Control> v = new Vector<Control>();
		// clear everything in the stack.
		s.clear();

		s.push(root);
		while (!s.isEmpty()) {
			Control tmpControl = s.pop();
			if (tmpControl != null) {
				v.add(tmpControl);
				addControl(tmpControl);
			}
		}
		return v;
	}

}
