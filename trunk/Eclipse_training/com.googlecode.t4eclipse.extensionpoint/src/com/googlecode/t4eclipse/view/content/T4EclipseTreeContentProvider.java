package com.googlecode.t4eclipse.view.content;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.googlecode.t4eclipse.extension.ContentExtentionProvider;
import com.googlecode.t4eclipse.model.TreeModel;

public class T4EclipseTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TreeModel) {
			TreeModel tm = (TreeModel) parentElement;
			return tm.getChildren().toArray(new TreeModel[0]);
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof TreeModel) {
			TreeModel tm = (TreeModel) element;
			return tm.getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof TreeModel) {
			TreeModel tm = (TreeModel) element;
			return tm.getChildren().size() > 0;

		}
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ContentExtentionProvider) {
			ContentExtentionProvider contents = (ContentExtentionProvider) inputElement;
			return contents.getAllRootModel().toArray(new TreeModel[0]);
		}
		if (inputElement instanceof TreeModel) {
			TreeModel tm = (TreeModel) inputElement;
			return tm.getChildren().toArray(new TreeModel[0]);

		}
		return null;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
