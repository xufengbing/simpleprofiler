package com.google.code.t4eclipse.tools.ktable.model;


import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolItem;

import com.google.code.t4eclipse.tools.action.OpenJavaTypeAction;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;

import de.kupzog.ktable.t4eclipse.KTableCellRenderer;

public class LocalToolBarItemModel extends RowModel {

	ContributionItem citem;

	public LocalToolBarItemModel(ToolItem o,ContributionItem item) {
		super(o);
		 this.citem=item;
	}

	public LocalToolBarItemModel() {
		super();
	}

	public String getColID() {
		if (citem != null) {

			String id = citem.getId();
			if (id != null) {
				return id;
			}
		}

		return "";
	}

	public String getColSelected() {

		ToolItem item = (ToolItem) this.object;
		if (item != null && !item.isDisposed()) {
			if ((item.getStyle() & SWT.CHECK) != 0
					|| (item.getStyle() & SWT.RADIO) != 0)
				return  Boolean.valueOf(item.getSelection()).toString();
		}
		return "";
	}

	public boolean getColEnabled() {

		ToolItem item = (ToolItem) this.object;
		if (item != null && !item.isDisposed()) {
			return item.isEnabled();
		}
		return false;
	}

	public String getColToolTip() {
		ToolItem item = (ToolItem) this.object;
		if (item != null && !item.isDisposed()) {
			String text = item.getToolTipText();
			if (text != null) {
				return text;
			}
		}
		return "";
	}
	public String getColClass() {

		String id = getColID();
		// not "" and null
		if (id != null && id.length() > 0) {
			return EclipseExtentionInfoProvider.getViewToolBarClass(id)[1];
		}
		//this.citem.
		if(this.citem instanceof ActionContributionItem){

			ActionContributionItem actionItem=(ActionContributionItem) this.citem;
			 IAction action = actionItem.getAction();
			return action.getClass().getName() ;
		}
		
		return "";
	}
	public KTableCellRenderer getCellRender(String columName, int col, int row) {
		if (getColEnabled()) {
			m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
					SWT.COLOR_BLACK));
		} else {
			m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
					SWT.COLOR_RED));
		}

		return m_textRenderer;

	}



	@Override
	public int[] getColumLengths() {
		return new int[] { 50, 50, 100, 200, 50 };
	}
	@Override
	public String[] getColumNames() {
		return new String[] { "Enabled", "Selected", "ToolTip",
				  "Class" ,"ID"};
	}

	@Override
	public String[] getMenuItemNames() {
	  return new String[] { "Open_Class" };
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
}

