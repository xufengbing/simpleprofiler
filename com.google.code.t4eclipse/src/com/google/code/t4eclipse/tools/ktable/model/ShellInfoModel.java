package com.google.code.t4eclipse.tools.ktable.model;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.wizards.IWizardDescriptor;

import com.google.code.t4eclipse.core.eclipse.helper.EclipseWizardHelper;
import com.google.code.t4eclipse.tools.action.OpenJavaTypeAction;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;
import com.google.code.t4eclipse.tools.utility.Constants;
import com.google.code.t4eclipse.tools.utility.ShellListnerChecker;

public class ShellInfoModel extends RowModel {

	private String shellDataName;

	private String shellTitle;

	private String info;

	public ShellInfoModel(String shellDataName, String shellTitle) {
		super();
		this.shellDataName = shellDataName;
		this.shellTitle = shellTitle;
		this.info = "";
	}

	public ShellInfoModel(String shellDataName, String shellTitle, String info) {
		super();
		this.shellDataName = shellDataName;
		this.shellTitle = shellTitle;
		this.info = info;
	}

	public ShellInfoModel() {
		super();
		this.shellDataName = "";
		this.shellTitle = "";
		this.info = "";
	}

	@Override
	public int[] getColumLengths() {
		return new int[] { 250, 300, 200 };
	}

	@Override
	public String[] getColumNames() {
		// TODO Auto-generated method stub
		// return new String[] { "GroupID", "WizardID","Plugin","Class" };
		return new String[] { "Class", "Title", "Info" };
	}

	// the content actually can get using java reflection.
	@Override
	public String[] getMenuItemNames() {
		return new String[] { "Open_Class" };
	}

	// folloiwng method are used to get colum content.
	protected String getColClass() {
		return this.shellDataName;
	}

	protected String getColTitle() {
		return this.shellTitle;
	}

	protected String getColInfo() {
		return this.info;
	}

	// folloiwng method are used to add popup menuitem
	protected void runMenuOpen_Class(SimpleKTable table, int col, int row) {
		boolean oldvalue=ShellListnerChecker.getCheck();
		try {
			ShellListnerChecker.setCheck(false);
			String className = this.getColClass();
			if (!this.info.equals("")) {

					int start = this.info.indexOf(Constants.classStartInInfo);
					int end = this.info.indexOf(Constants.classEndInInfo);
					if(start>=0&&end>=0&&start+7<end){
						className=this.info.substring(start+7,end);
					}


			}

			new OpenJavaTypeAction(className).run();
		} catch (Throwable t) {

		}
		finally{
			ShellListnerChecker.setCheck(oldvalue);

		}
	}
}
