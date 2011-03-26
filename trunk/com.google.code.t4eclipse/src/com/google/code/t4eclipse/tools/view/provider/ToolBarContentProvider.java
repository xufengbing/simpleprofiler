package com.google.code.t4eclipse.tools.view.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.google.code.t4eclipse.core.utility.MenuUtility;

public class ToolBarContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		// if (parentElement instanceof ToolItem) {
		// ToolItem item = (ToolItem) parentElement;
		//
		// Object data = item.getData();
		//
		// if (data != null && data instanceof MenuManager) {
		// MenuManager mm = (MenuManager) data;
		//
		// if (mm != null) {
		//
		// return mm.getMenu().getItems();
		// }
		// }
		//
		// }

		return null;

	}

	public Object getParent(Object element) {

		if (element != null && element instanceof ToolItem) {
			ToolItem item = (ToolItem) element;

			ToolBar toolbar = item.getParent();
			if (toolbar != null) {
				return toolbar;
			}
		}

		return null;
	}

	public boolean hasChildren(Object element) {
		// if (element instanceof MenuItem) {
		// MenuItem item = (MenuItem) element;
		//
		// Object data = item.getData();
		//
		// if (data != null && data instanceof MenuManager) {
		// MenuManager mm = (MenuManager) data;
		//
		// if (mm != null)
		// return mm.getMenu().getItemCount() > 0;
		//
		// }
		//
		// }

		return false;
	}

	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof ToolBar) {
			ToolBar toolBar = (ToolBar) inputElement;
			return toolBar.getItems();
		}

		if (inputElement instanceof CoolBar) {
			List<ToolItem> list = new ArrayList<ToolItem>();

			CoolBar cBar = (CoolBar) inputElement;
			Control[] childs = cBar.getChildren();
			for (Control c : childs) {
				if (c instanceof ToolBar) {
					ToolBar tb = (ToolBar) c;
					for (ToolItem tmpItem : tb.getItems()) {
						list.add(tmpItem);
					}

				}
			}
			return list.toArray(new ToolItem[0]);
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
