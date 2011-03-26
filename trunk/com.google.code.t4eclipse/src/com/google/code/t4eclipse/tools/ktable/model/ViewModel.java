package com.google.code.t4eclipse.tools.ktable.model;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceNode;
import org.eclipse.ui.views.IViewCategory;
import org.eclipse.ui.views.IViewDescriptor;

import com.google.code.t4eclipse.core.eclipse.helper.EclipseWorkPartHelper;
import com.google.code.t4eclipse.tools.action.OpenJavaTypeAction;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;
import com.google.code.t4eclipse.tools.view.MainView;

import de.kupzog.ktable.t4eclipse.KTableCellRenderer;
import de.kupzog.ktable.t4eclipse.renderers.TextCellRenderer;

public class ViewModel extends RowModel {

	private IViewCategory iViewCat;

	public ViewModel(IViewDescriptor o) {
		super(o);
		// TODO Auto-generated constructor stub
	}

	public ViewModel() {
		super();
	}

	public String getColID() {
		IViewDescriptor discriptor = (IViewDescriptor) this.object;
		return discriptor.getId();
	}

	public String getColCatID() {
		return iViewCat.getId();
	}

	public void setViewCat(IViewCategory cat) {
		this.iViewCat = cat;
	}

	protected String getColPlugin() {
		IViewDescriptor discriptor = (IViewDescriptor) this.object;
		String id = discriptor.getId();
		return EclipseExtentionInfoProvider.getViewClass(id)[0];
	}

	protected String getColClass() {
		IViewDescriptor discriptor = (IViewDescriptor) this.object;
		String id = discriptor.getId();
		return EclipseExtentionInfoProvider.getViewClass(id)[1];
	}

	public KTableCellRenderer getCellRender(String columName, int col, int row) {

		m_textRenderer = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS);
		if (!getColIsShow()) {
			m_textRenderer.setBackground(Display.getDefault().getSystemColor(
					SWT.COLOR_WIDGET_LIGHT_SHADOW));
		}
		return m_textRenderer;

	}

	public boolean getColIsShow() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IViewReference[] refs = page.getViewReferences();
		boolean isShow = false;
		for (IViewReference ref : refs) {
			if (ref.getId().equals(this.getColID())) {
				
				isShow = true;
			}

		}
		return isShow;
	}

	protected void runMenuOpen_View(SimpleKTable table, int col, int row) {
		try {
			String viewID = getColID();
			String comment = "Open View:\n" + viewID;
			String message = "EclipseWorkPartHelper.getDefault().actionOpenView(\""
					+ viewID + "\")";
			generateCode(comment, message);
			EclipseWorkPartHelper.getDefault().actionOpenView(viewID);

		} catch (Throwable t) {
			MessageDialog.openError(table.getShell(), "Error",
					"Error when open this view");
		}
	}

	// folloiwng method are used to add popup menuitem
	protected void runMenuShow_View_Local_ToolBar(SimpleKTable table, int col,
			int row) {
		try {
			String viewID = getColID();
			if(!this.getColIsShow()){
				MessageDialog.openInformation(table.getShell(),"View Not Opened yet!", "Please open this view first!");
				return;
			}
			LocalToolBarItemModel[] list = EclipseModelDataProvider
					.getDefault().getLocalToolBarList(viewID);
			if (list != null && list.length != 0) {
				TabFolder tab = MainView.getMainSWT().getTabFolder();
				tab.setSelection(7);

				MainView.getMainSWT().updateLocalToolBarTable(list, viewID);
			}else{
				MessageDialog.openInformation(table.getShell(),"No View Action", "No view action exist for this view");
				return ;
			}
		} catch (Throwable t) {

			MessageDialog
					.openInformation(table.getShell(), "Problem", "View is on this perspective,\nBut hasn't init yet.\nOpen this view first!");
		}
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
		return new int[] { 150, 200, 200 };
		// return new int[] { 100, 30,100,100, 200 };
	}

	@Override
	public String[] getColumNames() {

		return new String[] { "CatID", "Class", "ID" };
		// return new String[] { "CatID", "IsShow", "Plugin","Class","ID" };
	}

	@Override
	public String[] getMenuItemNames() {
		return new String[] { "Open_View", "Show_View_Local_ToolBar",
				"Open_Class" };

	}

}
