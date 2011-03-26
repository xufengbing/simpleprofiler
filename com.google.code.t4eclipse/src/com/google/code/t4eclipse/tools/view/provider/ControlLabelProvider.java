package com.google.code.t4eclipse.tools.view.provider;

import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.core.utility.ControlUtility;
import com.google.code.t4eclipse.selection.ControlSelection;


public class ControlLabelProvider extends LabelProvider implements
		ITableLabelProvider, ITableFontProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		if (ControlUtility.COLUMNS[columnIndex].equals("Name")) {

			if (element instanceof ControlSelection) {
				ControlSelection cs = (ControlSelection) element;

				Control controlElement = cs.getControl();

				if (!(controlElement instanceof Composite)) {
					return PlatformUI.getWorkbench().getSharedImages()
							.getImage(ISharedImages.IMG_OBJ_FILE);

				}
			}

		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {

		if (element instanceof ControlSelection) {
			ControlSelection cs = (ControlSelection) element;
			Control controlElement = cs.getControl();

			String data = ControlUtility.getControlRowDataStr(controlElement,
					ControlUtility.COLUMNS[columnIndex]);

			return data;
		}

		return "";
	}

	public Font getFont(Object element, int columnIndex) {
		return null;
	}

}
