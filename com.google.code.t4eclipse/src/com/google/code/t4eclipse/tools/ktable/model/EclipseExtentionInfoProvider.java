package com.google.code.t4eclipse.tools.ktable.model;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

public class EclipseExtentionInfoProvider {

	/**
	 * return first the plug-in id, second class name null if can not find the
	 * id;
	 *
	 * @param id
	 * @return
	 */
	private static String[] getPluginInfo(String id, String attribute) {
		String[] returnStr = new String[] { "", "" };
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(
				PlatformUI.PLUGIN_ID, attribute);// IWorkbenchRegistryConstants.PL_NEW);

		final IExtension[] extensions = point.getExtensions();
		if (extensions.length == 0) {
			return returnStr;
		}
		for (IExtension ex : extensions) {
			IConfigurationElement[] eles = ex.getConfigurationElements();

			for (IConfigurationElement ele : eles) {
				if (id.equals(ele.getAttribute("id"))) {
					String pluginID = ex.getNamespaceIdentifier();
					returnStr[0] = pluginID;
					if (ele.getAttribute("class") != null) {

						returnStr[1] = ele.getAttribute("class");

					} else if (ele.getChildren("class") != null
							&& ele.getChildren("class").length > 0) {

						if (ele.getChildren("class")[0].getAttribute("class") != null) {
							returnStr[1] = ele.getChildren("class")[0]
									.getAttribute("class");
						}
					}
				}

			}
		}

		return returnStr;
	}

	// PL_PREFERENCES

	public static String[] getPrefPageClass(String id) {
		return getPluginInfo(id, IWorkbenchRegistryConstants.PL_PREFERENCES);
	}

	public static String[] getWizardClass(String id) {
		return getPluginInfo(id, IWorkbenchRegistryConstants.PL_NEW);
	}

	public static String[] getViewClass(String id) {
		return getPluginInfo(id, IWorkbenchRegistryConstants.PL_VIEWS);
	}

	// PL_PERSPECTIVES
	public static String[] getPerspectiveClass(String id) {
		return getPluginInfo(id, IWorkbenchRegistryConstants.PL_PERSPECTIVES);
	}

	public static String[] getWorkbecnhToolBarClass(String id) {

		return getAction(id, IWorkbenchRegistryConstants.PL_ACTION_SETS);
	}

	public static String[] getViewToolBarClass(String id) {
		return getAction(id, IWorkbenchRegistryConstants.PL_VIEW_ACTIONS);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public static String[] getEditorClass(String id) {
		return getPluginInfo(id, IWorkbenchRegistryConstants.PL_EDITOR);
	}

	private static String[] getAction(String id, String set) {

		String[] returnStr = new String[] { "", "" };
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(
				PlatformUI.PLUGIN_ID, set);// IWorkbenchRegistryConstants.PL_NEW);

		final IExtension[] extensions = point.getExtensions();
		if (extensions.length == 0) {
			return returnStr;
		}
		for (IExtension ex : extensions) {
			IConfigurationElement[] eles = ex.getConfigurationElements();
			if(set.equals(IWorkbenchRegistryConstants.PL_ACTION_SETS)){
				//TODO debug here
				//System.out.println("TOSTR:"+eles.);
			}

			for (IConfigurationElement ele : eles) {
			
				String actionSetID = ele.getAttribute("id");
				IConfigurationElement[] actions = ele.getChildren();
				if (actions != null && actions.length > 0) {
					for (IConfigurationElement tmp : actions) {

						if (id.equals(tmp.getAttribute("id"))) {

							returnStr[0] = actionSetID;
							if (tmp.getAttribute("class") != null) {

								returnStr[1] = tmp.getAttribute("class");

							} else if (tmp.getChildren("class") != null
									&& tmp.getChildren("class").length > 0) {

								if (tmp.getChildren("class")[0]
										.getAttribute("class") != null) {
									returnStr[1] = tmp.getChildren("class")[0]
											.getAttribute("class");
								}
							}
						}
						// //////////////////////////////////
					}
				}

			}

		}

		return returnStr;
	}

}
