package com.google.code.t4eclipse.tools.ktable.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PartPane;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceNode;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.views.IViewCategory;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

import com.google.code.t4eclipse.core.eclipse.helper.EclipseWorkPartHelper;
import com.google.code.t4eclipse.tools.ktable.RowModel;

public class EclipseModelDataProvider {

	private static EclipseModelDataProvider instance;

	private EclipseModelDataProvider() {
	}

	public static synchronized EclipseModelDataProvider getDefault() {
		if (instance == null) {
			instance = new EclipseModelDataProvider();
		}
		return instance;
	}

	public MainMenuItemModel[] getMainMenuModelList() {

		List<MainMenuItemModel> list = new ArrayList<MainMenuItemModel>();

		WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		MenuManager menuMgr = window.getMenuManager();

		MenuItem[] menuItems = menuMgr.getMenu().getItems();
		for (int i = 0; i < menuItems.length; i++) {

			IContributionItem data = (IContributionItem) menuItems[i].getData();
			MainMenuItemModel model = new MainMenuItemModel();
			model.top = true;
			model.separator = false;
			model.group = false;
			model.menuStr = menuItems[i].getText();
			String topLevelPath = data.getId();
			model.path = topLevelPath;
			list.add(model);

			if (data instanceof MenuManager) {
				MenuManager mm = (MenuManager) data;

				// get items
				IContributionItem[] items = mm.getItems();
				for (IContributionItem item : items) {
				 
					if (item instanceof AbstractGroupMarker) {
						AbstractGroupMarker agm = (AbstractGroupMarker) item;
						if (agm.isGroupMarker()) {
							MainMenuItemModel groupModel = new MainMenuItemModel();
							groupModel.top = false;
							groupModel.separator = agm.isSeparator();
							groupModel.group = true;
							groupModel.path = topLevelPath + "/" + agm.getId();
							list.add(groupModel);
						} else {
							MainMenuItemModel sepModel = new MainMenuItemModel();
							sepModel.top = false;
							sepModel.separator = agm.isSeparator();;
							sepModel.group = false;
							list.add(sepModel);
						}

					} else if (item instanceof SubContributionItem) {
						SubContributionItem sub = (SubContributionItem) item;
						IContributionItem subInternal = sub.getInnerItem();
						if (subInternal instanceof AbstractGroupMarker) {
							AbstractGroupMarker agm = (AbstractGroupMarker) subInternal;
							if (agm.isGroupMarker()) {
								MainMenuItemModel groupModel = new MainMenuItemModel();
								groupModel.top = false;
								groupModel.separator = agm.isSeparator();
								groupModel.group = true;
								groupModel.path = topLevelPath + "/" + agm.getId();
								list.add(groupModel);
							} else {
								MainMenuItemModel sepModel = new MainMenuItemModel();
								sepModel.top = false;
								sepModel.separator = agm.isSeparator();;
								sepModel.group = false;
								list.add(sepModel);
							}
						} else {

							String menuStr = getMenuStr(item, menuItems[i]);
							if (menuStr != null) {
								MainMenuItemModel menuitemmodel = new MainMenuItemModel();
								menuitemmodel.top = false;
								menuitemmodel.separator = false;
								menuitemmodel.group = false;
								menuitemmodel.menuStr = menuStr;
								list.add(menuitemmodel);
							}
						}

					} else {
						String menuStr = getMenuStr(item, menuItems[i]);
						if (menuStr != null) {
							MainMenuItemModel sepModel = new MainMenuItemModel();
							sepModel.top = false;
							sepModel.separator = false;
							sepModel.group = false;
							sepModel.menuStr = menuStr;
							list.add(sepModel);
						}
					}

					// ////////////////function print menumanger
				}
			}
			if (data instanceof SubContributionItem) {
				SubContributionItem subi = (SubContributionItem) data;
				IContributionItem innerI = subi.getInnerItem();
				if (innerI instanceof MenuManager) {
					MenuManager mm = (MenuManager) innerI;
 
					IContributionItem[] items = mm.getItems();
					for (IContributionItem item : items) {
						// ////////////////function print menumanger

						if (item instanceof AbstractGroupMarker) {
							AbstractGroupMarker agm = (AbstractGroupMarker) item;
							if (agm.isGroupMarker()) {
								MainMenuItemModel groupModel = new MainMenuItemModel();
								groupModel.top = false;
								groupModel.separator = agm.isSeparator();
								groupModel.group = true;
								groupModel.path = topLevelPath + "/" + agm.getId();
								list.add(groupModel);
							} else {
								MainMenuItemModel sepModel = new MainMenuItemModel();
								sepModel.top = false;
								sepModel.separator = agm.isSeparator();;
								sepModel.group = false;
								list.add(sepModel);
							}

						} else if (item instanceof SubContributionItem) {
							SubContributionItem sub = (SubContributionItem) item;
							IContributionItem subInternal = sub.getInnerItem();
							if (subInternal instanceof AbstractGroupMarker) {
								AbstractGroupMarker agm = (AbstractGroupMarker) subInternal;
								if (agm.isGroupMarker()) {
									MainMenuItemModel groupModel = new MainMenuItemModel();
									groupModel.top = false;
									groupModel.separator = agm.isSeparator();
									groupModel.group = true;
									groupModel.path = topLevelPath + "/"
											+ agm.getId();
									list.add(groupModel);
								} else {
									MainMenuItemModel sepModel = new MainMenuItemModel();
									sepModel.top = false;
									sepModel.separator = agm.isSeparator();;
									sepModel.group = false;
									list.add(sepModel);
								}
							} else {
								String menuStr = getMenuStr(item, menuItems[i]);
								if (menuStr != null) {
									MainMenuItemModel sepModel = new MainMenuItemModel();
									sepModel.top = false;
									sepModel.separator = false;
									sepModel.group = false;
									sepModel.menuStr = menuStr;
									list.add(sepModel);
								}
							}

						} else {
							String menuStr = getMenuStr(item, menuItems[i]);
							if (menuStr != null) {
								MainMenuItemModel sepModel = new MainMenuItemModel();
								sepModel.top = false;
								sepModel.separator = false;
								sepModel.group = false;
								sepModel.menuStr = menuStr;
								list.add(sepModel);
							}

						}

					}
				}
			}

		}
		return list.toArray(new MainMenuItemModel[0]);
	}

	private String getMenuStr(IContributionItem item, MenuItem item2) {
		if (item != null && item2.getMenu() != null) {
			MenuItem[] is = item2.getMenu().getItems();
			if (is != null && is.length > 0) {
				for (MenuItem i : is) {
					Object data = i.getData();
					if (item == data) {
						return i.getText();
					}
				}
			}
		}

		return null;
	}

	public MainMenuItemModel[] getMenuModelList() {
		Vector<MainMenuItemModel> menuList = new Vector<MainMenuItemModel>();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		Menu menu = shell.getMenuBar();

		addMenu(menu, menuList);
		return menuList.toArray(new MainMenuItemModel[] {});
	}

	public WorkpartModel[] getWorkpartModelList() {
		Vector<WorkpartModel> workPartList = new Vector<WorkpartModel>();
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		Control rootControl = getPartControl(part);

		Vector<Control> controls = new ControlFinder()
				.getAllChilds(rootControl);
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i) != null) {
				workPartList.add(new WorkpartModel(controls.get(i)));
			}
		}
		return workPartList.toArray(new WorkpartModel[] {});
	}

	private Control getPartControl(IWorkbenchPart part) {
		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			PartPane pane = ((PartSite) site).getPane();
			return pane.getControl();
		} else {
			return null;
		}
	}

	private void addMenu(Menu menu, Vector<MainMenuItemModel> menuList) {

		MenuItem[] items = menu.getItems();
		for (int i = 0; i < items.length; i++)
			addMenuItem(items[i], menuList);
	}

	private void addMenuItem(MenuItem item, Vector<MainMenuItemModel> menuList) {
		Menu subMenu = item.getMenu();
		if (subMenu != null) {

			if (item.getData() != null) {

			}

			forceMenuOpen(subMenu);
			addMenu(subMenu, menuList);
		} else {
			MainMenuItemModel model = new MainMenuItemModel(item);
			menuList.add(model);
		}

	}

	private void forceMenuOpen(Menu menu) {
		Event e = new Event();
		e.type = SWT.Show;
		e.widget = menu;
		menu.notifyListeners(e.type, e);

	}

	public MainToolBarItemModel[] getToolBarModelList() {

		Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0]
				.getShell();

		Object data = shell.getData();
		if (data instanceof ApplicationWindow) {
			Vector<MainToolBarItemModel> v = new Vector<MainToolBarItemModel>();
			ApplicationWindow window = (ApplicationWindow) data;
			CoolBarManager coolMng = window.getCoolBarManager();
			if (coolMng != null) {
				//coolMng.
				IContributionItem[] items = coolMng.getItems();

				if (items != null) {
					for (int i = 0; i < items.length; i++) {
						//System.out.println(items[i].getClass().getName());
						if (items[i] instanceof ToolBarContributionItem) {
							ToolBarContributionItem item = (ToolBarContributionItem) items[i];
							ToolBarManager toolMng = (ToolBarManager) item
									.getToolBarManager();

							ToolBar tb = toolMng.getControl();
							if (tb != null) {
								ToolItem[] toolItems = tb.getItems();
								if (toolItems != null) {

									for (int j = 0; j < toolItems.length; j++) {
										MainToolBarItemModel model = new MainToolBarItemModel(
												toolItems[j]);
										v.add(model);

									}

								}
							}
						}
					}

					return v.toArray(new MainToolBarItemModel[] {});
				}
			}
		}

		return null;
	}

	public LocalToolBarItemModel[] getLocalToolBarList(String viewID) {

		IViewPart vp = EclipseWorkPartHelper.getDefault().getViewInCurrentpage(
				viewID);
		 IActionBars bars = vp.getViewSite().getActionBars();
		 //bars.getToolBarManager()
		if (vp != null) {
			Vector<LocalToolBarItemModel> v = new Vector<LocalToolBarItemModel>();
			IToolBarManager toolbarM = vp.getViewSite().getActionBars()
					.getToolBarManager();
			if (toolbarM instanceof ToolBarManager) {
				ToolBarManager tbm = (ToolBarManager) toolbarM;
				ToolBar tb = tbm.getControl();
				ToolItem[] items = tb.getItems();
				for (ToolItem item : items) {
					LocalToolBarItemModel model = getToolBarModel(item);
					if (model != null) {
						v.add(model);
					}
				}
				return v.toArray(new LocalToolBarItemModel[] {});
			}
		}

		return null;

	}

	private LocalToolBarItemModel getToolBarModel(ToolItem item) {

		ContributionItem tbcon = null;
		Object data = item.getData();
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
			LocalToolBarItemModel model = new LocalToolBarItemModel(item,tbcon);
			return model;
		}
		return null;
	}

	public PerspectiveModel[] getPerspectiveList() {
		Vector<PerspectiveModel> v = new Vector<PerspectiveModel>();
		IPerspectiveRegistry perspectives = PlatformUI.getWorkbench()
				.getPerspectiveRegistry();
		IPerspectiveDescriptor[] s = perspectives.getPerspectives();

		for (int i = 0; i < s.length; i++) {
			v.add(new PerspectiveModel(s[i]));
		}
		return v.toArray(new PerspectiveModel[] {});
	}

	/**
	 * get all the perference page list
	 * 
	 * @return
	 */
	public PerferenceModel[] getPrefList() {
		Vector<PerferenceModel> v = new Vector<PerferenceModel>();
		List nodes = PlatformUI.getWorkbench().getPreferenceManager()
				.getElements(0);
		for (Object node : nodes) {
			try {
				WorkbenchPreferenceNode pnode = (WorkbenchPreferenceNode) node;
				v.add(new PerferenceModel(pnode));
			} catch (Throwable t) {

			}
		}
		return v.toArray(new PerferenceModel[] {});
	}

	/**
	 * get all the WizardModel in current eclipse
	 * 
	 * @return
	 */
	public WizardModel[] getWizardList() {

		List<IWizardDescriptor> wizards = getAllWizard();

		WizardModel[] model = new WizardModel[wizards.size()];
		for (int i = 0; i < model.length; i++) {
			model[i] = new WizardModel(wizards.get(i));
		}
		return model;
	}

	private List<IWizardDescriptor> getAllWizard() {
		List<IWizardDescriptor> wizards = new ArrayList<IWizardDescriptor>();
		try {

			IWorkbench workbench = PlatformUI.getWorkbench();
			IWizardRegistry wizardRegister = workbench.getNewWizardRegistry();
			IWizardCategory rootCategory = wizardRegister.getRootCategory();
			addWizardModel(rootCategory, wizards);
		} catch (Throwable t) {

		}
		return wizards;
	}

	private void addWizardModel(IWizardCategory cat,
			List<IWizardDescriptor> list) {
		if (cat == null)
			return;
		else {
			IWizardDescriptor[] Wizards = cat.getWizards();
			for (IWizardDescriptor tmp : Wizards) {
				list.add(tmp);
			}
			IWizardCategory[] cats = cat.getCategories();
			for (IWizardCategory tmpCat : cats) {
				addWizardModel(tmpCat, list);
			}
		}
	}

	public ViewModel[] getViewModelList() {
		Vector<ViewModel> v = new Vector<ViewModel>();
		IViewRegistry viewReg = WorkbenchPlugin.getDefault().getViewRegistry();
		IViewCategory[] cats = viewReg.getCategories();
		for (IViewCategory cat : cats) {
			IViewDescriptor[] views = cat.getViews();
			for (IViewDescriptor view : views) {
				ViewModel model = new ViewModel(view);
				model.setViewCat(cat);
				v.add(model);
			}
		}
		return v.toArray(new ViewModel[] {});

	}

	public RowModel[] getEditorModelList() {

		Vector<EditorModel> v = new Vector<EditorModel>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(
				PlatformUI.PLUGIN_ID, IWorkbenchRegistryConstants.PL_EDITOR);// IWorkbenchRegistryConstants.PL_NEW);

		final IExtension[] extensions = point.getExtensions();
		if (extensions.length != 0) {
			for (IExtension ex : extensions) {
				IConfigurationElement[] eles = ex.getConfigurationElements();
				for (IConfigurationElement ele : eles) {
					String id = ele.getAttribute("id");
					String className = null;
					if (ele.getAttribute("class") != null) {

						className = ele.getAttribute("class");

					} else if (ele.getChildren("class") != null
							&& ele.getChildren("class").length > 0) {

						if (ele.getChildren("class")[0].getAttribute("class") != null) {
							className = ele.getAttribute("class");
						}
					}

					if (id != null && className != null && id.length() > 0
							&& className.length() > 0) {
						EditorModel tmp = new EditorModel(id, className);
						v.add(tmp);
					}
					// //////////////////////////

				}
			}
		}
		return v.toArray(new EditorModel[] {});
	}
}
