package com.verigy.itee.eclipsetraining.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SWTEventExample {
	public static void main(String[] args) {
		final Display display = new Display();
		display.addFilter(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {

			}
		});

		final Shell shell = new Shell(display);

		Label label = new Label(shell, SWT.NONE);
		label.setText("Enter your name:");

		final Text text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new RowData(100, SWT.DEFAULT));

		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				MessageBox messageBox = new MessageBox(shell);
				messageBox.setMessage("You have entered: " + text.getText());
				messageBox.open();
			}
		});

		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});

		shell.setLayout(new RowLayout());
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}