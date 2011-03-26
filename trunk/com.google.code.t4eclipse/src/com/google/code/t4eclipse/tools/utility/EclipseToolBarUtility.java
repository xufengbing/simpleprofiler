package com.google.code.t4eclipse.tools.utility;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.tools.model.EclipseToolBarModel;

public class EclipseToolBarUtility {

	public static List<EclipseToolBarModel> getToolBarModel() {

		ArrayList<EclipseToolBarModel> list = new ArrayList<EclipseToolBarModel>();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		Object data = shell.getData();
		if (data instanceof ApplicationWindow) {
			ApplicationWindow window = (ApplicationWindow) data;
			CoolBarManager coolMng = window.getCoolBarManager();
			if (coolMng != null) {
				IContributionItem[] items = coolMng.getItems();

				if (items != null) {
					for (int i = 0; i < items.length; i++) {
						if (items[i] instanceof ToolBarContributionItem) {
							ToolBarContributionItem item = (ToolBarContributionItem) items[i];
							ToolBarManager toolMng = (ToolBarManager) item
									.getToolBarManager();

							ToolBar tb = toolMng.getControl();
							if (tb != null) {
								ToolItem[] toolItems = tb.getItems();
								if (toolItems != null) {

									for (int j = 0; j < toolItems.length; j++) {
										EclipseToolBarModel m = new EclipseToolBarModel();
										ToolItem toolItem = toolItems[j];
										m.Enabled = toolItem.isEnabled();
										m.ID = getString(getActionId(toolItem));
										m.ToolTip = getString(toolItem
												.getToolTipText());
										if ((toolItem.getStyle() & SWT.CHECK) != 0
												|| (toolItem.getStyle() & SWT.RADIO) != 0)
											m.Selected =   Boolean.valueOf(toolItem
													.getSelection()).toString();
										else {
											m.Selected = "";
										}
										list.add(m);
									}

								}
							}
						}
					}
				}
			}
		}
		return list;

	}

	public static String getString(String str) {
		if (str != null)
			return str;
		return "";
	}

	private static String getActionId(ToolItem toolItem) {
		Object data = toolItem.getData();
		if (data != null && (data instanceof IContributionItem)) {
			String result = getActionId((IContributionItem) data);
			if (!result.equals("")) {
				return result;
			}
		}

		return null;
	}

	private static String getActionId(IContributionItem contrib) {
		return contrib.getId();
	}
}
