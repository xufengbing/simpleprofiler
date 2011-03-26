package com.google.code.t4eclipse.tools.ktable.model;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceNode;
import org.eclipse.ui.wizards.IWizardDescriptor;

import com.google.code.t4eclipse.core.eclipse.helper.EclipsePrefHelper;
import com.google.code.t4eclipse.core.eclipse.helper.EclipseWizardHelper;
import com.google.code.t4eclipse.tools.action.OpenJavaTypeAction;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;

public class PerferenceModel extends RowModel {

	public PerferenceModel(WorkbenchPreferenceNode o) {
		super(o);
	}

	public PerferenceModel() {
		super();
	}

	public String getColID() {
		WorkbenchPreferenceNode node = (WorkbenchPreferenceNode) this.object;
		return node.getId();
	}
	protected String getColPlugin() {
		WorkbenchPreferenceNode discriptor = (WorkbenchPreferenceNode) this.object;
		String id= discriptor.getId();
		return EclipseExtentionInfoProvider.getPrefPageClass(id)[0];
	}
	protected String getColClass() {
		WorkbenchPreferenceNode discriptor = (WorkbenchPreferenceNode) this.object;
		String id=  discriptor.getId();
		return EclipseExtentionInfoProvider.getPrefPageClass(id)[1];
	}


	// folloiwng method are used to add popup menuitem
	protected void runMenuOpen_Preference(SimpleKTable table, int col, int row) {
		try {
			String perfID = this.getColID();

			String comment = "Open prefenence page:\n" + perfID;
			String message = "EclipsePrefHelper.getDefault().openPrefPage(\""
					+ perfID + "\");";
			generateCode(comment, message);
			EclipsePrefHelper.getDefault().openPrefPage(perfID);

		} catch (Throwable t) {
			MessageDialog.openError(table.getShell(), "Error",
					"Error when open this preference page");
		}
	}

	protected void runMenuOpen_Class(SimpleKTable table,int col,int row) {
		 try {
			String className =this.getColClass();
			new OpenJavaTypeAction(className).run();

		} catch (Throwable t) {

		}
	}
	@Override
	public int[] getColumLengths() {
		return new int[] {200, 200 };
	}

	@Override
	public String[] getColumNames() {
		return new String[] {"Class","ID"  };
	}

	@Override
	public String[] getMenuItemNames() {
		return new String[] { "Open_Preference","Open_Class" };
	}

}
