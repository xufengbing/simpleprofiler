package com.google.code.t4eclipse.core.utility;

import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.ObjectPluginAction;
import org.eclipse.ui.internal.PartPane;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.internal.PluginAction;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.part.ViewPart;

import com.google.code.t4eclipse.core.utility.ReflectionUtil.ObjectResult;
import com.google.code.t4eclipse.selection.ControlSelection;
import com.google.code.t4eclipse.tools.action.AnalyzeNextMenuAction;
import com.google.code.t4eclipse.tools.utility.ReflctionProvider;
import com.google.code.t4eclipse.tools.view.MenuAnalyzerView;

public class MenuUtility {

	private static final String GET_CONFIG_ELEMENT = "getConfigElement";

	public static final String TEXT = "Text";

	public static final String ITEM_DATA = "itemData";

	public static final String ITEM_DATA_ID = "itemDataID";

	public static final String ACTION = "Action";

	public static final String ACTION_ID = "ActionID";

	// public static final String[] COLUMNS = new String[] { TEXT, ACTION,
	// ACTION_ID, ITEM_DATA, ITEM_DATA_ID };
	//
	// public static final int[] COL_WIDTH = new int[] { 100, 250, 250, 100, 100
	// };

	public static final String[] COLUMNS = new String[] { TEXT, ACTION_ID,
			ACTION };

	public static final int[] COL_WIDTH = new int[] { 150, 300, 400 };

	public static final String[] YESNO = new String[] { "Y", "" };

	public static String getMenuItemRowDataStr(MenuItem item, String str) {

		if (str != null && item != null && !item.isDisposed()) {

			if (str.equals(TEXT)) {
				String text = item.getText();
				if (text == null || text.length() == 0) {
					Object data = item.getData();
					if (data instanceof Separator) {
						text = "-------------";
					}
				}
				return text != null ? text : "";
			}

			if (str.equals(ITEM_DATA)) {
				Object data = item.getData();
				return data != null ? data.getClass().getSimpleName() : "";
			}

			if (str.equals(ITEM_DATA_ID)) {
				Object data = item.getData();
				if (data != null && data instanceof ActionContributionItem) {
					ActionContributionItem ci = (ActionContributionItem) data;
					return ci.getId() != null ? ci.getId() : "";

				}
			}

			if (str.equals(ACTION)) {
				return getMenuItemActionClassStr(item);
			}

			if (str.equals(ACTION_ID)) {
				return getMenuItemActionID(item);
			}

		}
		return "";

	}

	private static String getMenuItemActionID(MenuItem item) {
		Object data = item.getData();
		if (data != null && data instanceof ActionContributionItem) {
			ActionContributionItem ci = (ActionContributionItem) data;
			IAction ac = ci.getAction();
			if (ac != null)
				return ac.getId() != null ? ac.getId() : "";
		}
		return "";
	}

	public static String getMenuItemActionClassStr(MenuItem item) {
		Object data = item.getData();
		if (data != null && data instanceof ActionContributionItem) {
			ActionContributionItem ci = (ActionContributionItem) data;

			IAction ac = ci.getAction();
			if (ac instanceof PluginAction) {
				PluginAction pa = (PluginAction) ac;

				ObjectResult oResult = ReflectionUtil.invokeMethod(
						GET_CONFIG_ELEMENT, pa, PluginAction.class);
				Object obj = oResult.result;
				if (obj != null && obj instanceof IConfigurationElement) {
					IConfigurationElement ice = (IConfigurationElement) obj;
					String className = ice
							.getAttribute(IWorkbenchRegistryConstants.ATT_CLASS);
					if (className != null) {
						return className;
					}
				}

			}

			if (ac instanceof RetargetAction) {
				RetargetAction retargetAction = (RetargetAction) ac;

				IAction handler = retargetAction.getActionHandler();
				if (handler != null) {
					return handler.getClass().getName();
				}

			}

			if (ac != null)
				return ac.getClass().getName();

		}

		return "";
	}

	public static String getMenuRowDataStr(Menu item, String str) {

		if (str != null && item != null && !item.isDisposed()) {

			if (str.equals(TEXT)) {

				return "Menu";
			}

		}
		return "";

	}

	public static Menu getSubMenu(MenuItem item) {

		Object data = item.getData();

		if (data != null && data instanceof MenuManager) {
			MenuManager mm = (MenuManager) data;
			return mm.getMenu();

		}

		return null;

	}

	public static Menu getMainMenu() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		Menu menu = shell.getMenuBar();
		return menu;

	}

	public static void addMenuHideListener(IWorkbenchPart part) {
		Control c = ControlUtility.getPartControl(part);
		List<Menu> list = ControlUtility.getAllMenu(c);
		for (Menu m : list) {
			m.addMenuListener(menuListener);
		}

	}

	public static void removeMenuHideListener(IWorkbenchPart part) {
		Control c = ControlUtility.getPartControl(part);
		List<Menu> list = ControlUtility.getAllMenu(c);
		for (Menu m : list) {
			m.removeMenuListener(menuListener);
		}

	}

	private static MenuListener menuListener = new MenuListener() {

		public void menuShown(MenuEvent e) {

		}

		public void menuHidden(MenuEvent e) {

			if (AnalyzeNextMenuAction.checkMenu) {
				Menu menu = (Menu) e.widget;
				try {
					IViewPart view = PlatformUI
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage()
							.showView(
									"com.google.code.t4eclipse.tools.view.MenuAnalyzerView");
					if (view != null && view instanceof MenuAnalyzerView) {
						MenuAnalyzerView ma = (MenuAnalyzerView) view;
						ma.updateFromAuto(menu);
					}
				} catch (PartInitException ex) {
					// do nothing
				}

			}

		}
	};

	public static Menu getViewMenu(IWorkbenchPart part) {

		IViewSite site = (IViewSite) part.getSite();
		IActionBars bars = site.getActionBars();

		IMenuManager menuManager = bars.getMenuManager();
		MenuManager mm = (MenuManager) menuManager;

		return mm.getMenu();

	}

	public static ToolBar getviewToolBar(IWorkbenchPart part) {
		IViewSite site = (IViewSite) part.getSite();
		IActionBars bars = site.getActionBars();
		ToolBarManager toolBarManager = (ToolBarManager) bars
				.getToolBarManager();
		return toolBarManager.getControl();

	}

//	private static void printItem(IContributionItem tmpItem) {
//		System.out.println("id:" + tmpItem.getId());
//		System.out.println("class:" + tmpItem.getClass().getName());
//		if (tmpItem instanceof ActionContributionItem) {
//			ActionContributionItem aitem = (ActionContributionItem) tmpItem;
//			System.out.println("action:"
//					+ aitem.getAction().getClass().getName());
//			System.out.println("text:" + aitem.getAction().getText());
//
//		} else if (tmpItem instanceof GroupMarker) {
//			GroupMarker g = (GroupMarker) tmpItem;
//
//		}
//
//	}

}