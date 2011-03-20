package com.googlecode.t4eclipse.extension;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;

public interface IToolPageContent {

	Composite createControl(Composite parent);

	void fillLocalToolBar(IToolBarManager manager);


}
