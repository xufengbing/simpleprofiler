package com.google.code.t4eclipse.selection;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.google.code.t4eclipse.core.utility.ControlUtility;

public class ControlSelection implements org.eclipse.ui.IActionFilter,
		ISelection {

	private String className;
	private ControlSelection parent;
	private Control c;

	public ControlSelection(Control c) {
		this(c, null);
	}

	public ControlSelection(Control c, ControlSelection p) {
		this.c = c;
		this.parent = p;
		this.className = c.getClass().getName();
	}

	public String getClassName() {
		return this.className;
	}

	public boolean testAttribute(Object target, String name, String value) {

		if (c == null || c.isDisposed()) {
			return false;
		}

		if (name.equals("hasMenu")) {
			if (value.toLowerCase().contains("y")) {
				return c.getMenu() != null;
			}
			if (value.toLowerCase().contains("n")) {
				return c.getMenu() == null;
			}
		}

		if (name.equals("hasData")) {
			if (value.toLowerCase().contains("y")) {
				return c.getData() != null;
			}
			if (value.toLowerCase().contains("n")) {
				return c.getData() == null;
			}
		}
		if (name.equals("isVisible")) {
			if (value.toLowerCase().contains("y")) {
				return c.isVisible();
			}
			if (value.toLowerCase().contains("n")) {
				return !c.isVisible();
			}
		}
		return false;
	}

	public ControlSelection[] getChildren() {
		if (this.c != null && !this.c.isDisposed()
				&& this.c instanceof Composite) {
			Composite com = (Composite) this.c;
			Control[] children = com.getChildren();
			ControlSelection[] cs = new ControlSelection[children.length];
			for (int i = 0; i < children.length; i++) {
				cs[i] = new ControlSelection(children[i], this);
			}
			return cs;
		}

		return new ControlSelection[0];
	}

	public boolean hasChildren() {
		if (c == null || c.isDisposed()) {
			return false;
		}
		return getChildren().length > 0;
	}

	public boolean isEmpty() {
		return false;
	}

	public Control getControl() {
		return this.c;
	}

	public ControlSelection getParent() {
		// TODO Auto-generated method stub
		return this.parent;
	}

	@Override
	public String toString() {

		String tmp = "";

		String[] cols = ControlUtility.COLUMNS;

		for (String col : cols) {
			tmp += "[" + col + "]:";
			tmp += ControlUtility.getControlRowDataStr(this.getControl(), col)
					+ "\n";

		}
		// for enhancement request 31,add parent class name support
		tmp += "[Parent class]:"
				+ ControlUtility.getControlParentClassName(this.getControl())
				+ "\n";
		return tmp;
	}

}
