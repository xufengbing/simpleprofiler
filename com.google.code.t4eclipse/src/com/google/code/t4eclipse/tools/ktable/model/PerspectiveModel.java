package com.google.code.t4eclipse.tools.ktable.model;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.wizards.IWizardDescriptor;

import com.google.code.t4eclipse.core.eclipse.helper.EclipsePrefHelper;
import com.google.code.t4eclipse.core.eclipse.helper.EclipseWorkPartHelper;
import com.google.code.t4eclipse.tools.action.OpenJavaTypeAction;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;


public class PerspectiveModel extends RowModel {

	public PerspectiveModel(IPerspectiveDescriptor o) {
		super(o);
		// TODO Auto-generated constructor stub
	}

	public PerspectiveModel() {
		super();
	}

	public String getColID() {
		IPerspectiveDescriptor discriptor = (IPerspectiveDescriptor) this.object;
		return discriptor.getId();
	}

	protected String getColClass() {
		IPerspectiveDescriptor discriptor = (IPerspectiveDescriptor) this.object;
		String id = discriptor.getId();
		return EclipseExtentionInfoProvider.getPerspectiveClass(id)[1];
	}

	// folloiwng method are used to add popup menuitem
	protected void runMenuOpen_Perspective(SimpleKTable table, int col, int row) {
		try {
			String perpectiveID = this.getColID();
			String comment = "Open perspective:\n" + perpectiveID;
			String message = "EclipseWorkPartHelper.getDefault().actionSetPerspective(\""
					+ perpectiveID + "\")";
			generateCode(comment, message);

			EclipseWorkPartHelper.getDefault().actionSetPerspective(
					perpectiveID);

		} catch (Throwable t) {
			MessageDialog.openError(table.getShell(), "Error",
					"Error when open this perspective");
		}
	}

	protected void runMenuOpen_Class(SimpleKTable table, int col, int row) {
		try {
			String className = this.getColClass();
			if (className != null && className.trim().length() > 0) {
				new OpenJavaTypeAction(className).run();
			}

		} catch (Throwable t) {
		}
	}

	@Override
	public int[] getColumLengths() {
		return new int[] { 200, 200 };
	}

	@Override
	public String[] getColumNames() {
		return new String[] { "Class", "ID" };
	}

	@Override
	public String[] getMenuItemNames() {
		// TODO Auto-generated method stub
		return new String[] { "Open_Perspective", "Open_Class" };
	}
}
