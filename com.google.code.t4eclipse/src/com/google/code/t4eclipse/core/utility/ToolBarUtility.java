package com.google.code.t4eclipse.core.utility;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.PluginAction;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

import com.google.code.t4eclipse.core.utility.ReflectionUtil.ObjectResult;

public class ToolBarUtility {
	public static final String TEXT = "Text";

	public static final String ACTION = "Action";

	public static final String ACTION_ID = "ActionID";
	private static final String GET_CONFIG_ELEMENT = "getConfigElement";

	public static final String[] COLUMNS = new String[] { TEXT, ACTION_ID,
			ACTION };

	public static final int[] COL_WIDTH = new int[] { 150, 300, 400 };

	public static String getToolBarRowDataStr(ToolItem item, String str) {

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

			if (str.equals(ACTION)) {
				return getToolBarItemActionClassStr(item);
			}

			if (str.equals(ACTION_ID)) {
				return getToolBarItemActionID(item);
			}

		}
		return "";

	}

	private static String getToolBarItemActionID(ToolItem item) {
		Object data = item.getData();
		if (data != null && data instanceof ActionContributionItem) {
			ActionContributionItem ci = (ActionContributionItem) data;
			IAction ac = ci.getAction();
			if (ac != null)
				return ac.getId() != null ? ac.getId() : "";
		}
		if (data != null && data instanceof SubContributionItem) {

			SubContributionItem sub = (SubContributionItem) data;
			IContributionItem innerItem = sub.getInnerItem();
			return innerItem.getId();

		}

		return "";
	}

	public static String getToolBarItemActionClassStr(ToolItem item) {
		Object data = item.getData();
		if (data != null && data instanceof ActionContributionItem) {
			return getActionContributioItemClass(data);

		}
		if (data != null && data instanceof SubContributionItem) {

			SubContributionItem sub = (SubContributionItem) data;
			IContributionItem innerItem = sub.getInnerItem();
			if(innerItem instanceof ActionContributionItem ){
				return getActionContributioItemClass(innerItem);
			}

		}

		return "";
	}

	private static String getActionContributioItemClass(Object data) {
		ActionContributionItem ci = (ActionContributionItem) data;

		IAction ac = ci.getAction();
		if (ac != null) {
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

			return ac.getClass().getName();
		}
		return null;
	}

	public static CoolBar getEclipseCoolBar() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		Object data = shell.getData();
		if (data instanceof ApplicationWindow) {
			ApplicationWindow window = (ApplicationWindow) data;
			CoolBarManager coolMng = window.getCoolBarManager();
			return coolMng.getControl();
		}
		return null;
	}
}