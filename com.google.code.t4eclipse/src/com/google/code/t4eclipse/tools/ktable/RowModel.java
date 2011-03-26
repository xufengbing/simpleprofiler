package com.google.code.t4eclipse.tools.ktable;

import java.lang.reflect.Method;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.google.code.t4eclipse.tools.utility.CodeGenerateMgr;

import de.kupzog.ktable.t4eclipse.KTableCellRenderer;
import de.kupzog.ktable.t4eclipse.renderers.TextCellRenderer;

/**
 * this class is used together with <code>SimpleKTableModel</code><br>
 * to make life easier me to write special <code>KTableModel</code> every time<br>
 *
 * @author Ben.XU
 *
 */
public abstract class RowModel {

	private boolean bcolor=false;
	protected  TextCellRenderer m_textRenderer = new TextCellRenderer(
			TextCellRenderer.INDICATION_FOCUS);

	public static final String RUN_MENUITEM_METHOD = "runMenu";

	public static final String GET_COLUMN_CONTEXT_METHOD = "getCol";

	protected Object object;

	public RowModel(Object o) {
		this.object = o;
	}

	public RowModel(){}

	public Object getObject() {
		return this.object;
	}

	
	/**
	 * subclass can overwrite this method to provide special CellRender for each nonfixed cell.
	 * @param columName
	 * @return
	 */
	public KTableCellRenderer getCellRender(String columName,int col,int row){
 
 
		m_textRenderer = new TextCellRenderer(
				TextCellRenderer.INDICATION_FOCUS);
		return m_textRenderer;

	}

	
	
	protected Object getColumnContents(String columnName) {
		return invokeMethod(GET_COLUMN_CONTEXT_METHOD + columnName, this);
	}


	protected Object invokeMethod(String name, Object o) {
		Object returnObject = null;
		try {

			Method m = o.getClass().getDeclaredMethod(name);
			m.setAccessible(true);
			returnObject = m.invoke(o);
		} catch (Throwable e) {
			return null;
		}
		return returnObject;
	}

	protected void runMethod(String name,SimpleKTable table,int col,int row) {

		try {

			Method m = this.getClass().getDeclaredMethod(name,new Class[]{SimpleKTable.class,Integer.TYPE,Integer.TYPE});
			m.setAccessible(true);
			m.invoke(this,table,col,row);
		} catch (Throwable e) {

		}
	}



	protected void addMenuItem(final SimpleKTable table,final int col,final int row) {

		final Menu menu=table.getMenu();
        String[] items = getMenuItemNames();
		if (items != null && items.length > 0) {
			for (final String itemStr : items) {
				if (itemStr != null && checkStr(itemStr)) {
					final MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
					menuItem.setText(itemStr);
					menuItem.addSelectionListener(new SelectionListener() {

						public void widgetDefaultSelected(SelectionEvent e) {

						}

						public void widgetSelected(SelectionEvent e) {

							runMethod(RUN_MENUITEM_METHOD + itemStr,  table,   col,  row);

						}
					});
				}

			}
		}
	}


	protected static void generateCode(String comment, String message) {
		assert (message != null);

		if (comment != null) {
			CodeGenerateMgr.getDefault().printlnComment(
					"\n//" + getComment(comment));
		}

		if (message != null) {
			CodeGenerateMgr.getDefault().printlnCode(message);
		}

		// MessageDialog.openInformation(PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getShell(), "CodeGen", message);

	}

	private static String getComment(String comment) {
		assert (comment != null);
		return comment.replaceAll("\n", "\n//");
	}

	/**
	 * default row height is 25,subClass can overwrite this method
	 *
	 * @return
	 */
	public int getRowHeight() {
		return 30;
	}
	// TODO: check a str that only contains number and character
	private boolean checkStr(String str) {
		return true;
	}

	// colum names associate with this row
	// the content actually can get using java reflection.
	// but it's more clear using this way
	public abstract String[] getColumNames();

	// colum length associate with this row
	public abstract int[] getColumLengths();

	// menu item associate with this row
	// the content actually can get using java reflection.
	// but it's more clear using this way
	public abstract String[] getMenuItemNames();

}
