package com.googlecode.t4eclipse.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;

import com.googlecode.t4eclipse.Activator;
import com.googlecode.t4eclipse.model.TreeModel;

public class ContentExtentionProvider {

	public static final String ITEM_ID = "item";
	public static final String ID = "id";
	public static final String PID = "pid";
	public static final String LABEL = "label";
	public static final String IMAGE = "image";
	public static final String CLASS = "class";
	List<TreeModel> list = new ArrayList<TreeModel>();

	public void getAllModel() {
		String id;
		String pid;
		String label;
		String image;
		String cls;

		// IPath path;
		// ImageDescriptor desc;

		String myPluginId = Activator.PLUGIN_ID;
		IExtension points[] = Platform.getExtensionRegistry()
				.getExtensionPoint(myPluginId, ITEM_ID).getExtensions();
		for (IExtension extension : points) {
			IConfigurationElement elements[] = extension
					.getConfigurationElements();
			String bundleID = extension.getContributor().getName();
			// bundle = Platform.getBundle(symbolicName);
			for (IConfigurationElement element : elements) {
				if (ITEM_ID.equals(element.getName())) {
					id = element.getAttribute(ID);
					pid = element.getAttribute(PID);
					label = element.getAttribute(LABEL);
					image = element.getAttribute(IMAGE);
					cls = element.getAttribute(CLASS);
					TreeModel tm = new TreeModel(id, pid, label, image,
							bundleID, cls);
					this.list.add(tm);

				}
			}
		}
	}

}
