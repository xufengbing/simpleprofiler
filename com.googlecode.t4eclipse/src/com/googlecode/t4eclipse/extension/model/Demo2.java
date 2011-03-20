package com.googlecode.t4eclipse.extension.model;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.googlecode.t4eclipse.extension.IToolPageContent;

public class Demo2 implements IToolPageContent {

	@Override
	public Composite createControl(Composite parent) {
		Composite p = new Composite(parent, SWT.NONE);
		p.setLayout(new FillLayout());
		Label l = new Label(p, SWT.None);
		l.setText("DEMO2");
		return p;
	}

	@Override
	public void fillLocalToolBar(IToolBarManager manager) {
		Action action1;
		Action action2;
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		manager.add(action1);
		manager.add(action2);

	}

	private void showMessage(String message) {
		MessageDialog.openInformation(Display.getDefault().getActiveShell(),
				"DEMO2:", message);
	}
}
