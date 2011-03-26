package com.google.code.t4eclipse.tools.view.provider;

import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import com.google.code.t4eclipse.core.utility.MenuUtility;
import com.google.code.t4eclipse.core.utility.ToolBarUtility;

public class ToolBarItemLabelProvider extends LabelProvider implements
		ITableLabelProvider, ITableFontProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		if (ToolBarUtility.COLUMNS[columnIndex].equals(ToolBarUtility.TEXT)) {
			if (element instanceof ToolItem) {
				ToolItem item = (ToolItem) element;
				Image image = item.getImage();
				if (image != null) {
					Image newImage = new Image(Display.getDefault(), image
							.getImageData());
					return newImage;
				}

			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String head = ToolBarUtility.COLUMNS[columnIndex];
		if (element instanceof ToolItem) {
			ToolItem item = (ToolItem) element;
			if (ToolBarUtility.COLUMNS[columnIndex]
					.equals(ToolBarUtility.ACTION)) {
				item.setData("JAVA_TYPE", ToolBarUtility.getToolBarRowDataStr(
						item, ToolBarUtility.ACTION));

			}

			return ToolBarUtility.getToolBarRowDataStr(item, head);
		}

		return "";
	}

	public Font getFont(Object element, int columnIndex) {
		return null;
	}

}
