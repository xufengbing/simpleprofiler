package com.google.code.t4eclipse.tools.ktable.model;


import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.code.t4eclipse.tools.dialog.LocateControlDialog;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;
import com.google.code.t4eclipse.tools.utility.Constants;
import com.google.code.t4eclipse.tools.utility.MainViewTabUtility;
import com.google.code.t4eclipse.tools.view.MainView;

import de.kupzog.ktable.t4eclipse.KTableCellRenderer;
import de.kupzog.ktable.t4eclipse.renderers.DefaultCellRenderer;
import de.kupzog.ktable.t4eclipse.renderers.TextCellRenderer;

public class WorkpartModel extends RowModel {

	private Control control;

	public WorkpartModel(Control c) {
		super();
		this.control = c;
	}

	public WorkpartModel() {
		super();
	}

	public KTableCellRenderer getCellRender(String columName, int col, int row) {
		if (!chechHasChild()) {
			m_textRenderer = new TextCellRenderer(
					TextCellRenderer.INDICATION_FOCUS);

		} else {
			m_textRenderer = new TextCellRenderer(
					TextCellRenderer.INDICATION_FOCUS);

			m_textRenderer.setForeground(DefaultCellRenderer.COLOR_COMMENTSIGN);
			m_textRenderer.setBackground(Display.getDefault().getSystemColor(
					SWT.COLOR_WIDGET_LIGHT_SHADOW));

		}

		if (this.control.isFocusControl()) {
			m_textRenderer.setBackground(Display.getDefault().getSystemColor(
					SWT.COLOR_YELLOW));
		}

		return m_textRenderer;
	}

	private boolean chechHasChild() {
		if (control instanceof Composite) {
			Composite comp = (Composite) control;
			Control[] childs = comp.getChildren();
			if (childs != null && childs.length > 0) {
				return true;
			}
		}
		return false;
	}

	public String getColLevel() {
		int level = this.getLevelNum();

		String preIndex = "";
		if (level == 0) {
			preIndex = "";
		}
		if (level == 1) {
			preIndex = "*";
		}
		if (level == 2) {
			preIndex = "**";
		}
		if (level == 3) {
			preIndex = "*3* ";
		}
		if (level == 4) {
			preIndex = "***** ";
		}
		if (level == 5) {
			preIndex = "**5** ";
		}
		if (level == 6) {
			preIndex = "******* ";
		}
		if (level == 7) {
			preIndex = "***7*** ";
		}
		if (level == 8) {
			preIndex = "********* ";
		}
		if (level == 9) {
			preIndex = "****9**** ";
		}
		if (level >= 10) {
			preIndex = "****" + level + "****";
		}
		return preIndex;
	}

	private int getLevelNum() {
		int num = 0;

		Control tmp = this.control;
		while (tmp != null) {
			if (tmp.getData(Constants.rootControlIDName) != null) {
				break;
			} else {
				tmp = tmp.getParent();
				num++;
			}

		}
		return num;
	}

	public String getClassName() {
		if (this.control != null && !this.control.isDisposed()) {
			return this.control.getClass().getName();
		} else {
			return null;
		}
	}

	// the Class Name column
	public String getColClassName() {
		String name = this.getClassName();
		if (name != null) {

			return getIndexSpace() + name;
		}
		return "N/A";
	}

	private String getIndexSpace() {
		String multiSpace = "";
		int i = 0;
		while (i < this.getLevelNum()) {
			multiSpace += " ";
			i++;
		}
		return multiSpace;
	}

	public String getColVisiable() {
		if (this.control != null && !this.control.isDisposed()) {
			return this.control.isVisible() ? "Y" : "";
		}
		return "N/A";

	}

	public String getColEnable() {
		if (this.control != null && !this.control.isDisposed()) {
			return this.control.isEnabled() ? "Y" : "";
		}
		return "N/A";

	}

	public boolean isFocus() {
		if (this.control != null && !this.control.isDisposed()) {
			return this.control.isFocusControl();
		}
		return false;

	}

	public String getColText() {
		if (this.control != null && !this.control.isDisposed()) {
			return getText();
		}
		return "--";
	}

	private String getText() {
		Object o = invokeMethod("getText", this.control);
		if (o == null)
			return "--";
		else if (o instanceof String)
			return o.toString();
		else
			return "--";
	}

	@Override
	public int[] getColumLengths() {
		return new int[] { 100, 250, 50, 50, 50 };
		// return new int[] { 100 };
	}

	@Override
	public String[] getColumNames() {
		return new String[] { "Level", "ClassName", "Visiable", "Enable",
				"Text" };

	}

	protected void runMenuFind(SimpleKTable table, int col, int row) {
		if (this.control != null) {

			Shell shell = new Shell(Display.getDefault());
			shell.setText("Locate a widget");
			LocateControlDialog dialog = new LocateControlDialog(shell,
					this.control);
			dialog.setText("Locate a Widget");
			shell.pack();
			shell.open();

		}

	}

	protected void runMenuAnalysis(SimpleKTable table, int col, int row) {
		this.control.getDisplay().setData(Constants.TABFOLDER_DATA_NAME,
				this.control);
	 
	 
	 String title="Analysis Control";
	 String message="Input information message for this control";
	 String proposal="";
	 
	    IInputValidator inputValidator = new IInputValidator() {
            public String isValid(String newText) {
                return (newText == null || newText.trim().length() == 0) ? " " : null; //$NON-NLS-1$
            }
        };
        InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(), title, message, proposal,
                inputValidator);
        if (dialog.open() != Window.CANCEL) {
            String name = dialog.getValue();
            if (name != null&& name.length()>0) {
            	//get name   
            	name = name.trim();
            	
            	ControlAnalysisModel model=new ControlAnalysisModel(this.control,name);
				MainView.getMainSWT().addToAnalysisControlTable(model);
				MainViewTabUtility.showTabInT4EclipseView("Control");
            	
                   	 
			}
         
        }
		
	}

	protected void runMenuVisiable(SimpleKTable table, int col, int row) {
		if (this.control != null) {
			this.control.setVisible(!this.control.getVisible());
		}
	}

	@Override
	public String[] getMenuItemNames() {
	//	return new String[] { "Find", "Visiable", "Analysis" };
		return new String[] {"Visiable", "Analysis" };
	}

	public int getRowHeight() {
		return 30;
	}
}
