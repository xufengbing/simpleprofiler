package com.google.code.t4eclipse.core.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PartPane;
import org.eclipse.ui.internal.PartSite;

import com.google.code.t4eclipse.core.finder.Finder;
import com.google.code.t4eclipse.core.finder.IConditionFind;
import com.google.code.t4eclipse.selection.ControlSelection;
import com.google.code.t4eclipse.tools.utility.ReflctionProvider;

import de.kupzog.ktable.t4eclipse.KTable;
import de.kupzog.ktable.t4eclipse.KTableCellEditor;

public class ControlUtility {

	private static final String FULL_NAME = "FullName";

	private static final String CONTROL_MENU = "Menu";

	private static final String CONTROL_DATA = "Data";

	private static final String TEXT = "Text";

	private static final String ENABLE = "Enable";

	private static final String VISIBLE = "Visible";

	private static final String NAME = "Name";

	public static final String[] COLUMNS = new String[] { NAME, TEXT, VISIBLE,
			ENABLE, CONTROL_MENU, FULL_NAME };

	public static final int[] COL_WIDTH = new int[] { 200, 100, 50, 50, 50,
			200, 300 };

	public static final String[] YESNO = new String[] { "Y", "" };

	public static String getControlRowDataStr(Control co, String str) {

		if (str != null && co != null && !co.isDisposed()) {

			if (str.equals(FULL_NAME)) {
				return co.getClass().getName();
			}

			if (str.equals(NAME)) {
				return co.getClass().getSimpleName();
			}

			if (str.equals(VISIBLE)) {
				return getBoolStr(co.isVisible());
			}

			if (str.equals(ENABLE)) {
				return getBoolStr(co.isEnabled());
			}

			if (str.equals(TEXT)) {
				return getText(co);
			}
			if (str.equals(CONTROL_DATA)) {
				return getBoolStr(co.getData() != null);
			}
			if (str.equals(CONTROL_MENU)) {
				return getBoolStr(co.getMenu() != null);
			}

		}
		return "";

	}

	public static String getMenuStr(Menu m) {
		if (m != null) {
			System.out.println("   " + m);
			for (MenuItem item : m.getItems()) {
				// System.out.println(item.getText());
				Object data = item.getData();
				// System.out.println(data.getClass().getName() + " ");
				if (data != null && data instanceof ActionContributionItem) {
					ActionContributionItem ci = (ActionContributionItem) data;

					// System.out.println(ci.getAction().getClass().getName()
					// + "...id:" + ci.getId());

				} else if (data != null) {
					// System.err.println("error............................"
					// + data.getClass().getName());

				}
				// System.out.println("\n\n................");
			}
		}
		// System.out.println("\n\n\n");

		return "";
	}

	private static String getText(Control co) {

		Object o = ReflctionProvider.invokeMethod("getText", co);
		if (o == null)
			return "";
		else if (o instanceof String)
			return o.toString().replace("\n", " ");
		else
			return "";

	}

	private static String getBoolStr(boolean b) {
		return b ? YESNO[0] : YESNO[1];
	}

	public static Control getFocusControl(Control c) {

		Stack<Control> cs = new Stack<Control>();
		cs.push(c);
		while (!cs.isEmpty()) {
			Control tmp = cs.pop();
			if (tmp.isFocusControl()) {
				return tmp;
			}

			if (tmp instanceof Composite) {
				Composite com = (Composite) tmp;
				Control[] children = com.getChildren();
				for (Control child : children) {
					cs.push(child);
				}
			}

		}

		return null;
	}

	public static void selectFocusedTreeItem(Tree tree) {
		Stack<TreeItem> stack = new Stack<TreeItem>();
		for (TreeItem tmp : tree.getItems()) {
			stack.push(tmp);
		}
		while (!stack.isEmpty()) {
			TreeItem i = stack.pop();
			ControlSelection dataControl = (ControlSelection) i.getData();
			if (dataControl.getControl().isFocusControl()) {
				tree.setSelection(i);
			}
			for (TreeItem tmpItem : i.getItems()) {
				stack.push(tmpItem);
			}
		}

	}

	public static Control getActivePartControl() {

		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		return getPartControl(part);
	}

	public static List<Menu> getAllMenu(Control c) {
		List<Menu> menuList = new ArrayList<Menu>();

		Widget[] all = Finder.getDefault().findAllByCondition(c,
				new IConditionFind() {

					public boolean check(Widget widget) {
						if (widget instanceof Control) {
							Control tempC = (Control) widget;

							if (tempC != null && !tempC.isDisposed()
									&& tempC.isVisible()) {
								Menu menu = tempC.getMenu();
								if (menu != null && !menu.isDisposed()) {
									return true;
								}

							}

						}

						return false;
					}
				});

		for (Widget w : all) {
			Control control = (Control) w;
			menuList.add(control.getMenu());
		}
		return menuList;
	}

	public static Control getPartControl(IWorkbenchPart part) {

		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			PartPane pane = ((PartSite) site).getPane();
			return pane.getControl();
		} else {
			return null;
		}
	}

	public static String getActivePartDiscription(IWorkbenchPart part) {

		String str = "";
		if (part instanceof IEditorPart) {
			str += "Editor->";

		}
		if (part instanceof IViewPart) {
			str += "View->";
		}
		str += part.getTitle();

		str += "    Class->" + part.getClass().getName() + "		";
		return str;
	}

	public static Control getRootControl() {
		Control activePartControl = getActivePartControl();
		Control root = activePartControl;
		while (root.getParent() != null) {
			root = root.getParent();
		}

		return root;

	}

	public static String getSelectionText(ISelection sel) {

		if (sel instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection) sel;
			ControlSelection cs = (ControlSelection) s.getFirstElement();
			if (cs == null)
				return "";
			return cs.toString();
		}
		return "";
	}

	public static String getControlParentClassName(Control control) {
		if (control != null) {
			return control.getClass().getSuperclass().getName();
		}
		return "";

	}
}