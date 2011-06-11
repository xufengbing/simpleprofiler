package com.myeditor;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * Step 2
 * Add syntx coloring
 */
public class MyTextEditor extends TextEditor {

	public MyTextEditor()
	{
		super();
		setSourceViewerConfiguration(new MySourceViewerConfig());
	}
}

