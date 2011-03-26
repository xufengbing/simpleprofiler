package com.google.code.t4eclipse.selection;

import org.eclipse.swt.widgets.Control;

public class RootControlSelection extends ControlSelection {

	public RootControlSelection(Control c) {
		super(c);
	}

	@Override
	public ControlSelection[] getChildren() {

		return new ControlSelection[] { new ControlSelection(this.getControl()) };
	}
}
