package com.google.code.t4eclipse.tools.view.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.google.code.t4eclipse.selection.ControlSelection;

public class ControlContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof ControlSelection) {
			ControlSelection cs = (ControlSelection) parentElement;
			return cs.getChildren();
		}
		return new Object[0];

	}

	public Object getParent(Object element) {
		if (element instanceof ControlSelection) {
			ControlSelection com = (ControlSelection) element;
			return com.getParent();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof ControlSelection) {
			ControlSelection com = (ControlSelection) element;
			return com.getChildren().length > 0;

		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ControlSelection) {
	 
			ControlSelection com = (ControlSelection) inputElement;
			return com.getChildren();
		}
		
		return null;
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
