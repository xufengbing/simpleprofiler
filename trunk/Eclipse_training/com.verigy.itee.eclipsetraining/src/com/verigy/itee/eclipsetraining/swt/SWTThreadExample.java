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

public class SWTThreadExample {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);

		Label label = new Label(shell, SWT.NONE);
		label.setText("Enter your name:");

		final Text text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new RowData(100, SWT.DEFAULT));

		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");

		System.out.println("1.Run in Main  ,threadName:"
				+ Thread.currentThread().getName());

		ok.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out
						.println("2.Run when OK button is selected  ,threadName:"
								+ Thread.currentThread().getName());
				new Thread() {
					public void run() {
						System.out
								.println("3.Run in another thread  ,threadName:"
										+ Thread.currentThread().getName());

						// the following commented code can not run, as it try
						// to access swt from another thread.
						text.setText("abcde");
						// the above code need to be executed through
						// Display.syncExec or Display.asyncExec..

						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								System.out
										.println("4.Run in main thread again  ,threadName:"
												+ Thread.currentThread()
														.getName());

								MessageBox messageBox = new MessageBox(shell);
								messageBox.setMessage("You have entered: "
										+ text.getText());
								messageBox.open();

							}
						});
					}

				}.start();

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