package com.google.code.t4eclipse.tools.ktable.model;

import org.eclipse.swt.widgets.Control;

import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;
import com.google.code.t4eclipse.tools.ktable.SimpleKTableModel;

import de.kupzog.ktable.t4eclipse.KTableCellRenderer;

public class ControlAnalysisModel extends RowModel {
	Object object;

	String info;

	@Override
	public int[] getColumLengths() {
		return new int[] { 100, 150 };
	}

	@Override
	public String[] getColumNames() {
		return new String[] { "Type", "Info" };
	}

	public Object getObject(){
		return this.object;
	}
	public ControlAnalysisModel(Object o, String info) {
		this.object = o;
		this.info = info;
	}

	public ControlAnalysisModel() {
		super();
	}

	public String getColType() {
		if (this.object == null) {
			return "null";
		} else {
			if (this.object instanceof Control) {
				Control c = (Control) this.object;
				if (c.isDisposed()) {
					return "disposed:" + this.object.getClass().getSimpleName();
				}
			}
			return this.object.getClass().getSimpleName();
		}
	}

	public String getColInfo() {
		return this.info == null ? "null" : this.info;
	}

	public KTableCellRenderer getCellRender(String columName, int col, int row) {
		// if (this.top) {
		// m_textRenderer.setBackground(Display.getCurrent().getSystemColor(
		// SWT.COLOR_YELLOW));
		// m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
		// SWT.COLOR_BLACK));
		//
		// } else if (this.separator||this.group) {
		// m_textRenderer.setBackground(Display.getCurrent().getSystemColor(
		// SWT.COLOR_GRAY));
		// m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
		// SWT.COLOR_BLACK));
		// } else {
		// m_textRenderer.setBackground(Display.getCurrent().getSystemColor(
		// SWT.COLOR_WHITE));
		// m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
		// SWT.COLOR_BLACK));
		// }
		return m_textRenderer;

	}

	protected void runMenuAnalysis(SimpleKTable table, int col, int row) {
		//transfer this object to the MainView?
		
	}
	
	

	protected void runMenuDelete(SimpleKTable table, int col, int row) {
		SimpleKTableModel  model=(SimpleKTableModel) table.getModel();
		model.deleteTableRow(this);
		table.redraw();
	}
	


	protected void runMenuClear(SimpleKTable table, int col, int row) {
		SimpleKTableModel  model=(SimpleKTableModel) table.getModel();
		model.clearTableRow();
		table.redraw();
	}
	


	@Override
	public String[] getMenuItemNames() {
		return new String[] { "Delete", "Clear" };
	}

}
