/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/
package com.google.code.t4eclipse.core.finder;

import java.util.Stack;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author xufengbing
 *
 */
class FinderImp extends Finder {

	FinderImp() {
		s = new Stack<Widget>();

	}

	private void addStack(Widget w) {
		if (w != null) {
			s.push(w);
		}

	}

	private void addStack(Widget[] widgets) {
		if (widgets != null) {
			for (int i = widgets.length - 1; i >= 0; i--) {
				if (widgets[i] != null) {
					s.push(widgets[i]);
				}
			}

		}
	}

	/**
	 * user can create a new Finder by reimplement this method.<br>
	 * for example, if you need to find special things inside KTable, you need
	 * to<br>
	 * implement this method to use special KTable method to add KTable sub
	 * items.
	 */
	@Override
	protected void addWidget(Widget w) {
		// Decorations (shell is a sub of it)
		if (w instanceof Decorations) {
			Decorations d = (Decorations) w;
			addStack(d.getMenuBar());
		}

		// Control (windowed widget)
		if (w instanceof Control) {
			Control c = (Control) w;
			// localList.addAll(getAllMenuItems(c.getMenu()));
			addStack(c.getMenu());
		}
		/**
		 * widget-->Control-->Scrollable----->Composite---------->Canvas---------->KTable
		 * *****************************----->List
		 * *****************************----->Text
		 *
		 */
		if (w instanceof Scrollable) {
			addStack(((Scrollable) w).getVerticalBar());
			addStack(((Scrollable) w).getHorizontalBar());
		}
		if (w instanceof TreeItem) {
			Widget[] widgets = ((TreeItem) w).getItems();
			addStack(widgets);
		}
		if (w instanceof Menu) {
			Widget[] widgets = ((Menu) w).getItems();
			addStack(widgets);
		}
		if (w instanceof MenuItem) {
			Widget childMenu = ((MenuItem) w).getMenu();
			addStack(childMenu);
		}
		if (w instanceof Composite) {

			if (w instanceof ToolBar) {
				addStack(((ToolBar) w).getItems());
			}
			if (w instanceof Table) {
				addStack(((Table) w).getItems());
				addStack(((Table) w).getColumns());
			}
			if (w instanceof Tree) {
				addStack(((Tree) w).getItems());
			}
			if (w instanceof CoolBar) {
				addStack(((CoolBar) w).getItems());
			}
			if (w instanceof TabFolder) {
				Widget[] widgets = ((TabFolder) w).getItems();
				addStack(widgets);
			}
			if (w instanceof CTabFolder) {
				Widget[] widgets = ((CTabFolder) w).getItems();
				addStack(widgets);
			}
			Composite cont = (Composite) w;
			Control[] children = cont.getChildren();
			addStack(children);
		}
	}
}
