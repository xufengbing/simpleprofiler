package com.google.code.t4eclipse.tools.tree;

import org.eclipse.core.internal.runtime.AdapterManager;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.TreeItem;

public class TreeItemAnalysisModel {

	private TreeItem item;

	public String getItemDetail() {

		if (item == null || item.isDisposed()) {
			return null;
		}

		String text = "ItemText     : " + item.getText() + "\n";
		String expanded = "ItemExpanded : " + item.getExpanded() + "\n";

		Object data = item.getData();
		String dataClass = "DataClass: " + data.getClass() + "\n";
		String classStr = "DataStr  : " + data.toString() + "\n";

		String result = text + expanded + dataClass + classStr;

		boolean bAdapt = false;
		String adapterStr = "Adapters:\n";
		if (data instanceof IAdaptable) {
			bAdapt = true;
			IAdaptable adaptable = (IAdaptable) data;
			IAdapterManager manager = Platform.getAdapterManager();
			AdapterManager m = (AdapterManager) manager;
			String[] types = m.computeAdapterTypes(adaptable.getClass());
			for (String type : types) {
				adapterStr += "  *" + type + "\n";
				Object adapter = Platform.getAdapterManager().getAdapter(
						adaptable, type);
				if (adapter != null) {
					adapterStr += "    ->" + adapter.getClass().getName()
							+ "\n";
					adapterStr += "    ->" + adapter.toString() + "\n";
				}
			}
			adapterStr += "End of Adapters:\n";
		}

		if (bAdapt) {
			result += adapterStr;
		}
		return result;

	}

	public TreeItemAnalysisModel(TreeItem item) {
		super();
		this.item = item;
	}

}
