package com.googlecode.t4eclipse.view;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.googlecode.t4eclipse.extension.ContentExtentionProvider;
import com.googlecode.t4eclipse.view.content.T4EclipseTreeContentProvider;

public class T4EclipseContentView extends ViewPart {

	private TreeViewer tview;

	public T4EclipseContentView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		tview = new TreeViewer(parent);
		tview.setLabelProvider(new LabelProvider());
		tview.setContentProvider(new T4EclipseTreeContentProvider());
		tview.setInput(ContentExtentionProvider.getDefault());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
