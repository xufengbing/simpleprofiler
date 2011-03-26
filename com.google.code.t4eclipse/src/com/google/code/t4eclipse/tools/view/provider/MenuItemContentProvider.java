package com.google.code.t4eclipse.tools.view.provider;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.google.code.t4eclipse.core.utility.MenuUtility;

public class MenuItemContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof MenuItem) {
			MenuItem item = (MenuItem) parentElement;

			Object data = item.getData();

			if (data != null && data instanceof MenuManager) {
				MenuManager mm = (MenuManager) data;

				if (mm != null) {

					return mm.getMenu().getItems();
				}
			}

		}

		return null;

	}

	public Object getParent(Object element) {

		if (element != null && element instanceof MenuItem) {
			MenuItem item = (MenuItem) element;

			Menu menu = item.getMenu();
			if (menu != null) {
				return menu.getParentItem();
			}
		}

		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof MenuItem) {
			MenuItem item = (MenuItem) element;

			Object data = item.getData();

			if (data != null && data instanceof MenuManager) {
				MenuManager mm = (MenuManager) data;

				if (mm != null)
					return mm.getMenu().getItemCount() > 0;

			}

		}

		return false;
	}

	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof Menu) {
			Menu menu = (Menu) inputElement;
			return menu.getItems();
		}

		return null;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}
