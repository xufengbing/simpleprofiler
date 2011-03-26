package com.google.code.t4eclipse.tools.model;

import java.util.*;


import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceNode;

import com.google.code.t4eclipse.core.eclipse.helper.EclipseCommonHelper;
import com.google.code.t4eclipse.core.eclipse.helper.EclipseWizardHelper;
import com.google.code.t4eclipse.core.eclipse.helper.EclipseWorkPartHelper;
import com.google.code.t4eclipse.tools.utility.CodeGenerateMgr;
import com.google.code.t4eclipse.tools.utility.EclipseToolBarUtility;

public class ModelData {

	// private wizardList;
	//
	// private List<EclipsePrefModel> prefList;
	//
	// private List<EclipseMenuModel> menufList;
	//
	// private List<EclipseToolBarModel> toolBarfList;
	// prefList = new ArrayList<EclipsePrefModel>();
	// menufList = new ArrayList<EclipseMenuModel>();
	// toolBarfList = new ArrayList<EclipseToolBarModel>();

	public static List<EclipseWizardModel> getWizardModel() {
		List<EclipseWizardModel> wizardList = new ArrayList<EclipseWizardModel>();
		setWizard(wizardList);
		return wizardList;

	}

	public static List<EclipseToolBarModel> getToolBarModel() {

		return EclipseToolBarUtility.getToolBarModel();

	}

	/**
	 * this method contruct the LocalViewToolBarModel of a specail view<br>
	 * assert: a view with viewID as its id exist on current eclipse page.<br>
	 *
	 * @param viewID
	 *            the id of the view.
	 * @return A not null arraylist. (but may be empty)
	 */
	public static List<LocalViewToolBarModel> getLocalToolBarModel(String viewID) {

		List<LocalViewToolBarModel> list = new ArrayList<LocalViewToolBarModel>();

		// add content of the list.
		IViewPart vp = EclipseWorkPartHelper.getDefault().getViewInCurrentpage(
				viewID);
		if (vp != null) {
			IToolBarManager toolbarM = vp.getViewSite().getActionBars()
					.getToolBarManager();

			if (toolbarM instanceof ToolBarManager) {
				ToolBarManager tbm = (ToolBarManager) toolbarM;
				ToolBar tb = tbm.getControl();
				ToolItem[] items = tb.getItems();
				for (ToolItem item : items) {
					LocalViewToolBarModel model = getToolBarModel(item);
					if (model != null) {
						list.add(model);
					}
				}
			}
		}
		// end of add

		return list;
	}

	/**
	 *
	 * @param item
	 * @return a good model when it is a ActionContributionItem and not
	 *         Separator or GroupMarker<BR>
	 *         null if it is a separator or groupmarker or other conditions<br>
	 */
	private static LocalViewToolBarModel getToolBarModel(ToolItem item) {

		Object data = item.getData();

		ContributionItem tbcon = null;

		if (data != null) {
			if (data instanceof ContributionItem) {
				tbcon = (ContributionItem) data;
			}
			if (data instanceof SubContributionItem) {
				SubContributionItem sub = (SubContributionItem) data;
				IContributionItem internlItem = sub.getInnerItem();
				if (internlItem instanceof ContributionItem) {
					tbcon = (ContributionItem) internlItem;
				}

			}
		}

		if (tbcon != null) {
			if (tbcon.isSeparator() || tbcon.isGroupMarker()) {
				return null;
			}
			LocalViewToolBarModel model = new LocalViewToolBarModel();
			if (tbcon.getId() != null) {
				model.ID = tbcon.getId();
			}
			if (item.getToolTipText() != null) {
				model.ToolTip = item.getToolTipText();
			}
			model.Enabled = tbcon.isEnabled();

			model.item = item;
			// model.Style = item.getStyle();
			model.Selected = item.getSelection();

			return model;
		}
		return null;
	}

	public static List<EclipseViewModel> getViewModel() {
		List<EclipseViewModel> list = new ArrayList<EclipseViewModel>();

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			IViewReference[] refs = page.getViewReferences();
			for (int i = 0; i < refs.length; i++) {
				EclipseViewModel mdoel = new EclipseViewModel();

				mdoel.ID = refs[i].getId();
				list.add(mdoel);
			}
		}
		return list;
	}

	public static List<EclipsePerspectiveModel> getPerspectiveModel() {

		List<EclipsePerspectiveModel> list = new ArrayList<EclipsePerspectiveModel>();
		IPerspectiveRegistry perspectives = PlatformUI.getWorkbench()
				.getPerspectiveRegistry();
		IPerspectiveDescriptor[] s = perspectives.getPerspectives();
		for (IPerspectiveDescriptor dis : s) {
			EclipsePerspectiveModel model = new EclipsePerspectiveModel();
			model.ID = dis.getId();
			list.add(model);

		}

		return list;

	}

	public static List<EclipseMenuModel> getMenuModel() {
		List<EclipseMenuModel> menuList = new ArrayList<EclipseMenuModel>();

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		Menu menu = shell.getMenuBar();
		addMenu(menu, menuList);

		return menuList;

	}

	private static void addMenu(Menu menu, List<EclipseMenuModel> menuList) {
		MenuItem[] items = menu.getItems();
		for (int i = 0; i < items.length; i++)
			addMenuItem(items[i], menuList);
	}

	private static void addMenuItem(MenuItem item,
			List<EclipseMenuModel> menuList) {
		Menu subMenu = item.getMenu();
		if (subMenu != null) {
			 forceMenuOpen(subMenu);
			addMenu(subMenu, menuList);
		} else {
			if ((item.getStyle() & SWT.SEPARATOR) == 0) {
				EclipseMenuModel m = new EclipseMenuModel();
				m.Enabled = item.isEnabled();
				m.ID = getItemString(item);

				if ((item.getStyle() & SWT.CHECK) != 0
						|| (item.getStyle() & SWT.RADIO) != 0)
					m.Selected =   Boolean.valueOf(item.getSelection()).toString();
				else {
					m.Selected = "";
				}
				menuList.add(m);
			}
		}

	}

	private static void forceMenuOpen(Menu menu) {
		Event e = new Event();
		e.type = SWT.Show;
		e.widget = menu;
		menu.notifyListeners(e.type, e);

	}



	private static String getItemString(MenuItem item) {

		StringBuffer descriptiveText = new StringBuffer();
		findItemText(item, descriptiveText);

		/* Remove the ampersands */
		String itemText = descriptiveText.toString().replaceAll("\\&", "");
		return itemText;
	}

	/**
	 * Walk through the item and return a descriptive text that corresponds to
	 * all item selections leading to the item. (e.g. File-New-Project) works
	 * both for tree and menu
	 *
	 * @param item
	 *            The item
	 * @param descriptiveText
	 *            In the end, this buffer will contain a descriptive text of all
	 *            items leading to the item that is passed in as argument
	 */
	private static void findItemText(Item item, StringBuffer descriptiveText) {
		if (item == null)
			return;

		String descriptiveTextStr = item.getText();
		if (descriptiveText.length() > 0)
			descriptiveTextStr += "-";

		descriptiveText.insert(0, descriptiveTextStr);

		Item parentItem = null;
		if (item instanceof MenuItem) {
			Menu menu = ((MenuItem) item).getParent();
			parentItem = (menu == null ? null : menu.getParentItem());
		} else if (item instanceof TreeItem) {
			parentItem = ((TreeItem) item).getParentItem();
		}

		if (parentItem != null)
			findItemText(parentItem, descriptiveText);
	}

	public static List<EclipsePrefModel> getPrefModel() {
		List<EclipsePrefModel> perfList = new ArrayList<EclipsePrefModel>();
		// o is pre order
		List nodes = PlatformUI.getWorkbench().getPreferenceManager()
				.getElements(0);
		for (Object node : nodes) {
			try {
				EclipsePrefModel preModel = new EclipsePrefModel();
				WorkbenchPreferenceNode pnode = (WorkbenchPreferenceNode) node;

				preModel.ID = pnode.getId();
				perfList.add(preModel);
			} catch (Throwable t) {

			}

		}
		return perfList;

	}

	private static void setWizard(List<EclipseWizardModel> wizards) {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWizardRegistry wizardRegister = workbench.getNewWizardRegistry();
			IWizardCategory rootCategory = wizardRegister.getRootCategory();
			addWizardModel(rootCategory, wizards);
		} catch (Throwable t) {
		}
	}

	private static void addWizardModel(IWizardCategory cat,
			List<EclipseWizardModel> list) {
		if (cat == null)
			return;
		else {
			IWizardDescriptor[] Wizards = cat.getWizards();
			for (IWizardDescriptor tmp : Wizards) {
				EclipseWizardModel model = new EclipseWizardModel();
				model.ID = tmp.getId();
				model.CatID = cat.getId();
				list.add(model);
			}
			IWizardCategory[] cats = cat.getCategories();
			for (IWizardCategory tmpCat : cats) {
				addWizardModel(tmpCat, list);
			}
		}
	}

}
