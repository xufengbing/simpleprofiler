package com.google.code.t4eclipse.tools.ktable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.t4eclipse.KTableCellEditor;
import de.kupzog.ktable.t4eclipse.KTableCellRenderer;
import de.kupzog.ktable.t4eclipse.KTableDefaultModel;
import de.kupzog.ktable.t4eclipse.editors.KTableCellEditorText;
import de.kupzog.ktable.t4eclipse.renderers.FixedCellRenderer;
import de.kupzog.ktable.t4eclipse.renderers.TextCellRenderer;

/**
 *
 * this class represent a very simple KTable model.<br>
 *
 * @author fengbixu
 *
 * @param <T>
 */
public class SimpleKTableModel<T extends RowModel> extends KTableDefaultModel {

	private List<T> rowModelList = null;

	private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(
			FixedCellRenderer.STYLE_FLAT
					| TextCellRenderer.INDICATION_FOCUS_ROW);

	private T instance;

	/**
	 * Initialize the base implementation.
	 */
	public SimpleKTableModel(Class<T> clazz) {
		// m_textRenderer.setForeground(Display.getCurrent().getSystemColor(
		// SWT.COLOR_DARK_GREEN));
		rowModelList = new ArrayList<T>();
		this.instance = createInstance(clazz);

	}
	/**
	 * row mdoel list
	 * @return
	 */
	public List<T> getRowModelList(){
		return this.rowModelList;
	}
	/**
	 * add a row to the simple ktable
	 * @param rowData
	 */
	public void addKTableRow(T rowData) {
		this.rowModelList.add(rowData);
	}

	public void clearTableRow(){
		this.rowModelList.clear();
	}
	
	public void deleteTableRow(T row){
		for(int i=0;i<this.rowModelList.size();i++){
			if(this.rowModelList.get(i)!=null&&this.rowModelList.get(i)==row){
				this.rowModelList.remove(i);
			}
		}
	}

	public void addSimpelKTableMenuItem(SimpleKTable table, int col, int row) {
		T t = getRowModel(row);
		t.addMenuItem(table, col, row);
	}

	/**
	 * update this katble model
	 */
	public void update(T[] tarray) {
		if (tarray != null && tarray.length > 0) {

			this.rowModelList.clear();

			for (int i = 0; i < tarray.length; i++) {
				this.rowModelList.add(tarray[i]);
			}
		}
	}

	private T createInstance(Class<T> clazz) {
		T instance;
		try {
			instance = clazz.newInstance();

		} catch (Exception e) {
		 
			instance = null;
		}
		return instance;
	}

	private T getRowModel(int rol) {
		return rowModelList.get(rol - 1);
	}

	public Object doGetContentAt(int col, int row) {
		try {

			if (row == 0) {
				if (col == 0) {
					return "";
				} else if (col > 0) {
					return this.instance.getColumNames()[col - 1];
				}
			} else if (row > 0) {
				if (col == 0) {
					return   Integer.valueOf(row).toString();
				} else if (col == -1) {
					return this.rowModelList.get(row - 1);
				} else if (col > 0) {
					String columName = this.instance.getColumNames()[col - 1];
					return this.rowModelList.get(row - 1).getColumnContents(
							columName);
				}

			}
		} catch (Throwable t) {
	 
			return "Error";
		}

		return "N/A";
	}

	/*
	 * overridden from superclass
	 */
	public KTableCellEditor doGetCellEditor(int col, int row) {

		return new KTableCellEditorText();

	}

	/*
	 * overridden from superclass
	 */
	public void doSetContentAt(int col, int row, Object value) {
	}

	// Table size:
	public int doGetRowCount() {

		return this.rowModelList.size() + 1;
	}

	public int getFixedHeaderRowCount() {
		return 1;
	}

	public int doGetColumnCount() {
		return this.instance.getColumNames().length + 1;
	}

	public int getFixedHeaderColumnCount() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.kupzog.ktable.KTableModel#getFixedSelectableRowCount()
	 */
	public int getFixedSelectableRowCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.kupzog.ktable.KTableModel#getFixedSelectableColumnCount()
	 */
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	public boolean isColumnResizable(int col) {
		if (col == 0)
			return false;
		return true;
	}

	public boolean isRowResizable(int row) {
		return false;
	}

	public int getRowHeightMinimum() {
		return 25;
	}

	// Rendering
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (isFixedCell(col, row))
			return m_fixedRenderer;
		String columName = this.instance.getColumNames()[col - 1];
		return this.rowModelList.get(row - 1)
				.getCellRender(columName, col, row);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.kupzog.ktable.KTableModel#belongsToCell(int, int)
	 */
	public Point doBelongsToCell(int col, int row) {
		// no cell spanning:
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.kupzog.ktable.KTableDefaultModel#getInitialColumnWidth(int)
	 */
	public int getInitialColumnWidth(int column) {
		if (column == 0)
			return 40;
		return this.instance.getColumLengths()[column - 1];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.kupzog.ktable.KTableDefaultModel#getInitialRowHeight(int)
	 */
	public int getInitialRowHeight(int row) {
		if (row == 0)
			return 30;
		return this.instance.getRowHeight();
	}
}
