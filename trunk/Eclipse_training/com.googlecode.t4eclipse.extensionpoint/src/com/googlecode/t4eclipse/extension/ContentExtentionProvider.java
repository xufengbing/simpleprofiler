package com.googlecode.t4eclipse.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

import com.googlecode.t4eclipse.T4EclipseActivator;
import com.googlecode.t4eclipse.model.TreeModel;

public class ContentExtentionProvider {
	private static ContentExtentionProvider defaultContent;
	public static final String ITEM_ID = "item";
	public static final String ITEM_ELEmENT_ID = "Item";

	public static final String ID = "id";
	public static final String PID = "pid";
	public static final String LABEL = "label";
	public static final String IMAGE = "image";
	public static final String CLASS = "class";
	private List<TreeModel> list = new ArrayList<TreeModel>();

	private ContentExtentionProvider() {
	}

	public static ContentExtentionProvider getDefault() {
		if (defaultContent == null) {
			defaultContent = new ContentExtentionProvider();
			defaultContent.getModel();
			defaultContent.resolveModel();
		}
		return defaultContent;
	}

	private void getModel() {
		String id;
		String pid;
		String label;
		String image;
		String cls;
		String myPluginId = T4EclipseActivator.PLUGIN_ID;
		IExtension points[] = Platform.getExtensionRegistry()
				.getExtensionPoint(myPluginId, ITEM_ID).getExtensions();
		for (IExtension extension : points) {
			IConfigurationElement elements[] = extension
					.getConfigurationElements();
			String bundleID = extension.getContributor().getName();
			for (IConfigurationElement element : elements) {
				if (ITEM_ELEmENT_ID.equals(element.getName())) {
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

	public List<TreeModel> getAllRootModel() {

		List<TreeModel> parentList = new ArrayList<TreeModel>();
		for (TreeModel tm : this.list) {
			if (tm.getParent() == null) {
				parentList.add(tm);
			}
		}
		return parentList;
	}

	private void resolveModel() {
		TreeModel[] items = list.toArray(new TreeModel[0]);
		for (int i = 0; i < items.length; i++) {
			TreeModel tm = items[i];
			checkParentAndChildren(tm, list);

		}
	 
		
	}

	private void print() {
		for(TreeModel tm:this.list){
			 tm.print();
		}
		
	}

	private void checkParentAndChildren(TreeModel target, List<TreeModel> list) {
		TreeModel[] items = list.toArray(new TreeModel[0]);
		for (int i = 0; i < items.length; i++) {
			TreeModel tmp = items[i];
			if (tmp == target) {
				continue;
			}
			if (target.getPid() != null && target.getPid().equals(tmp.getId())) {
				target.setParent(tmp);
			}
			if (target.getId().equals(tmp.getPid())) {
				target.addChildren(tmp);
			}

		}
	}

}
