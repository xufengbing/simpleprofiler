package com.google.code.t4eclipse.tools.ktable.model;

import com.google.code.t4eclipse.tools.action.OpenJavaTypeAction;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;

public class EditorModel extends RowModel {
	private String id;
	private String className;
	public EditorModel(String id,String className) {
		this.id=id;
		this.className=className;
	}

	public EditorModel() {
		super();
	}

	public String getColID() {
		return  this.id;

	}

	protected String getColClass() {
		return this.className;
	}

	protected void runMenuOpen_Class(SimpleKTable table, int col, int row) {
		try {
			String className = this.getColClass();
			new OpenJavaTypeAction(className).run();

		} catch (Throwable t) {

		}
	}

	@Override
	public int[] getColumLengths() {
		return new int[] { 200, 200 };
		// return new int[] { 100, 30,100,100, 200 };
	}

	@Override
	public String[] getColumNames() {

		return new String[] { "ID", "Class" };
		// return new String[] { "CatID", "IsShow", "Plugin","Class","ID" };
	}

	@Override
	public String[] getMenuItemNames() {
		return new String[] { "Open_Class" };

	}

}
