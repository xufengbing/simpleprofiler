package com.google.code.t4eclipse.tools.view.provider;

import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

import com.google.code.t4eclipse.core.utility.MenuUtility;

public class MenuItemLabelProvider extends LabelProvider implements
		ITableLabelProvider, ITableFontProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		if (MenuUtility.COLUMNS[columnIndex].equals(MenuUtility.TEXT)) {
			if (element instanceof MenuItem) {
				MenuItem item = (MenuItem) element;
				Image image = item.getImage();
				if (image != null) {
					Image newImage = new Image(Display.getDefault(),
							image.getImageData());
					return newImage;
				}

			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String head = MenuUtility.COLUMNS[columnIndex];
		if (element instanceof MenuItem) {
			MenuItem item = (MenuItem) element;
			if (MenuUtility.COLUMNS[columnIndex].equals(MenuUtility.ACTION)) {
				item.setData("JAVA_TYPE", MenuUtility.getMenuItemRowDataStr(
						item, MenuUtility.ACTION));

			}

			return MenuUtility.getMenuItemRowDataStr(item, head);
		}

		return "";
	}

	public Font getFont(Object element, int columnIndex) {
		return null;
	}

}
