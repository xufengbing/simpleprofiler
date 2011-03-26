package com.google.code.t4eclipse.tools.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class MainView extends ViewPart {
	public static final String viewID = "com.google.code.t4eclipse.tools.view.MainView";

	static MainSWT instance = null;

	private Composite composite;

	public static MainSWT getMainSWT() {
		return instance;
	}

	/**
	 * Create the example
	 * 
	 * @see ViewPart#createPartControl
	 */
	public void createPartControl(Composite frame) {
		this.composite = frame;
		instance = new MainSWT(composite, this);
	}

	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus() {
		instance.setFocus();
	}

	/**
	 * Called when the View is to be disposed
	 */
	public void dispose() {
		instance.dispose();
		instance = null;
		this.composite.dispose();
		super.dispose();
	}

	public synchronized void updateView() {
		instance.updatePerspectiveTable();
		instance.updatePrefTable();
		instance.updateViewTable();
		instance.updateWizardTable();
		instance.updateToolbarTable();
		instance.updateMainMenuTable();
		instance.updateEditorTable();
	}

}
