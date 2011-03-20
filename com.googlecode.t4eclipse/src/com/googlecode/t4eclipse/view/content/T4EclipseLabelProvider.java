package com.googlecode.t4eclipse.view.content;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.googlecode.t4eclipse.model.TreeModel;

public class T4EclipseLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		if (obj instanceof TreeModel) {
			TreeModel tm = (TreeModel) obj;
			return tm.getLabel();
		}
		return obj.toString();
	}

	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof TreeModel) {
			TreeModel tm = (TreeModel) obj;
			if (tm.hasChildren()) {
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			}
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}