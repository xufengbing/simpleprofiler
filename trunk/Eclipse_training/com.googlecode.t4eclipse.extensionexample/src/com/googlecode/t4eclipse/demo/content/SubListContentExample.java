package com.googlecode.t4eclipse.demo.content;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.googlecode.t4eclipse.extension.IToolPageContent;

public class SubListContentExample implements IToolPageContent {

	public SubListContentExample() {
	}

	@Override
	public Composite createControl(Composite parent) {
		Composite p = new Composite(parent, SWT.NONE);
		p.setLayout(new FillLayout());
		final ListViewer v = new ListViewer(p, SWT.H_SCROLL | SWT.V_SCROLL);
		v.setLabelProvider(new LabelProvider());
		v.setContentProvider(new MyContentProvider());
		MyModel[] model = createModel();
		v.setInput(model);
		return p;

	}

	private MyModel[] createModel() {
		MyModel[] elements = new MyModel[10];

		for (int i = 0; i < 10; i++) {
			elements[i] = new MyModel("SubItemExampleItem"+i);
		}

		return elements;
	}

	@Override
	public void fillLocalToolBar(IToolBarManager manager) {
		//do nothing now
	}
 
}
