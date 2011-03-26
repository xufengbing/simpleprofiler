/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/
package com.google.code.t4eclipse.core.eclipse.helper;

import java.util.ArrayList;


import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.core.utility.GuilibUtility;


/**
 * this class is used to locate the eclipse ToolItem in the toolbar under the
 * eclipse main menu. TODO: add support for find a ToolItem from the ToolTip.
 *
 * @author xufengbing
 *
 */
public class EclipseToolBarHelper {

	public static EclipseToolBarHelper getDefault() {
		return new EclipseToolBarHelper();
	}

	/**
	 * only for debugging purpose
	 *
	 * @return all eclipse toolbars
	 */
	public String[] printAllToolBar() {

		ArrayList<String> list = new ArrayList<String>();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		Object data = shell.getData();
		if (data instanceof ApplicationWindow) {
			ApplicationWindow window = (ApplicationWindow) data;
			CoolBarManager coolMng = window.getCoolBarManager();
			if (coolMng != null) {
				IContributionItem[] items = coolMng.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i] instanceof ToolBarContributionItem) {
						ToolBarContributionItem item = (ToolBarContributionItem) items[i];
						ToolBarManager toolMng = (ToolBarManager) item
								.getToolBarManager();
						ToolBar tb = toolMng.getControl();
						ToolItem[] toolItems = tb.getItems();
						for (int j = 0; j < toolItems.length; j++) {
							ToolItem toolItem = toolItems[j];
							list.add(getToolItemStr(toolItem));
						}

					}
				}
			}
		}
		return list.toArray(new String[0]);
	}

	public ToolItem findEclipseToolItemFromActionID(String actionID) {

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		Object data = shell.getData();
		if (data instanceof ApplicationWindow) {
			ApplicationWindow window = (ApplicationWindow) data;
			CoolBarManager coolMng = window.getCoolBarManager();
			if (coolMng != null) {
				return locateToolItem(coolMng, actionID);
			}
			ToolBarManager toolMng = window.getToolBarManager();
			if (toolMng != null) {
				return locateToolItem(toolMng, actionID);
			}

			throw new RuntimeException("no coolbar and toolbar");
		}
		throw new RuntimeException("no correct object in shell");
	}

	private ToolItem locateToolItem(ICoolBarManager coolMng, String id) {
		IContributionItem[] items = coolMng.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i] instanceof ToolBarContributionItem) {
				ToolBarContributionItem item = (ToolBarContributionItem) items[i];
				IToolBarManager toolMng = item.getToolBarManager();
				ToolItem target = locateToolItem((ToolBarManager) toolMng, id);
				if (target != null)
					return target;
			}
		}
		return null;
	}

	private ToolItem locateToolItem(ToolBarManager toolMng, String id) {
		return locateToolItem(toolMng.getControl(), id);
	}

	private ToolItem locateToolItem(ToolBar toolBar, String id) {
		ToolItem[] items = toolBar.getItems();
		for (int i = 0; i < items.length; i++) {
			ToolItem item = items[i];
			String itemId = getActionId(item);

			if (itemId != null && itemId.equals(id))
				return item;
		}
		return null;
	}

	private String getToolItemStr(ToolItem item) {

		return "ToolItem:  " + " actionid=" + getActionId(item) + " tooltip="
				+ item.getToolTipText() + " enabled=" + item.isEnabled()
				+ " style=" + GuilibUtility.getStyle(item);
	}

	private String getActionId(ToolItem toolItem) {
		Object data = toolItem.getData();
		if (data != null && (data instanceof IContributionItem)) {
			String result = getActionId((IContributionItem) data);
			if (!result.equals("")) {
				return result;
			}
		}

		return null;
	}

	private String getActionId(IContributionItem contrib) {
		return contrib.getId();
	}

}