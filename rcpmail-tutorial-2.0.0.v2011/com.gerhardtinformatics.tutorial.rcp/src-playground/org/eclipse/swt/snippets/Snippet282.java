/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Copy/paste image to/from clipboard.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Snippet282 {

	Shell shell;

	Display display;

	private View view;

	private Clipboard clipboard;

	private class View {

		final Text imageText;

		final Button imageButton;

		private Button openButton;

		private Button copyButton;

		private Button pasteButton;

		private Button clearButton;

		public View() {
			shell.setLayout(new GridLayout());
			shell.setText("Clipboard ImageTransfer");

			imageButton = new Button(shell, SWT.NONE);
			imageText = new Text(shell, SWT.NONE);
		}

		public void init() {
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.minimumHeight = 400;
			gd.minimumWidth = 600;

			imageButton.setLayoutData(gd);
			imageText.setText("");
			imageText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

			Composite buttons = new Composite(shell, SWT.NONE);
			buttons.setLayout(new GridLayout(4, true));

			openButton = new Button(buttons, SWT.PUSH);
			openButton.setText("Open");

			copyButton = new Button(buttons, SWT.PUSH);
			copyButton.setText("Copy");

			pasteButton = new Button(buttons, SWT.PUSH);
			pasteButton.setText("Paste");

			clearButton = new Button(buttons, SWT.PUSH);
			clearButton.setText("Clear");
		}
	}

	public static void main(String[] args) {
		new Snippet282().run();
	}

	private void run() {
		display = new Display();
		shell = new Shell(display, SWT.SHELL_TRIM);
		clipboard = new Clipboard(display);

		view = new View();
		view.init();

		bind();

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void bind() {
		view.openButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				handleOpen();
			}
		});
		view.copyButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				handleCopy();
			}
		});
		view.pasteButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				handlePaste();
			}
		});
		view.clearButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				handleClear();
			}
		});

	}

	void handleOpen() {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText("Open an image file or cancel");
		String string = dialog.open();
		if (string != null) {
			Image image = view.imageButton.getImage();
			if (image != null)
				image.dispose();
			image = new Image(display, string);
			view.imageButton.setImage(image);
			view.imageText.setText(string);
		}
	}

	private void handleClear() {
		view.imageButton.setText("");
		Image image = view.imageButton.getImage();
		if (image != null)
			image.dispose();
		view.imageButton.setImage(null);
		view.imageText.setText("");
	}

	private void handlePaste() {
		ImageData imageData = (ImageData) clipboard.getContents(ImageTransfer.getInstance());
		if (imageData != null) {
			view.imageButton.setText("");
			Image image = view.imageButton.getImage();
			if (image != null)
				image.dispose();
			image = new Image(display, imageData);
			view.imageButton.setImage(image);
		} else {
			view.imageButton.setText("No image");
		}
		String text = (String) clipboard.getContents(TextTransfer.getInstance());
		if (text != null) {
			view.imageText.setText(text);
		} else {
			view.imageText.setText("");
		}
	}

	private void handleCopy() {
		Image image = view.imageButton.getImage();
		if (image != null) {
			ImageTransfer imageTransfer = ImageTransfer.getInstance();
			TextTransfer textTransfer = TextTransfer.getInstance();
			clipboard.setContents(new Object[] { image.getImageData(), view.imageText.getText() }, new Transfer[] {
					imageTransfer, textTransfer });
		}
	}
}
