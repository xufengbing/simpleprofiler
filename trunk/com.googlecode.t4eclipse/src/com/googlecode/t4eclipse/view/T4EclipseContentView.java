package com.googlecode.t4eclipse.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.googlecode.t4eclipse.extension.ContentExtentionProvider;
import com.googlecode.t4eclipse.extension.IToolPageContent;
import com.googlecode.t4eclipse.model.TreeModel;
import com.googlecode.t4eclipse.view.content.T4EclipseLabelProvider;
import com.googlecode.t4eclipse.view.content.T4EclipseTreeContentProvider;

public class T4EclipseContentView extends ViewPart {

	private TreeViewer tview;

	private Composite stackComposite;
	private SashForm sashForm;
	private Composite treeViewerComposite;

	private Map<String, Composite> compositeMap;
	private DrillDownAdapter drillDownAdapter;
	private Action doubleClickAction;
	private TreeModel treeModel;
	private StackLayout slayout;
	private Label initialContent;

	public T4EclipseContentView() {
		this.compositeMap = new HashMap<String, Composite>();
	}

	@Override
	public void createPartControl(Composite root) {

		root.setLayout(new FillLayout());
		sashForm = new SashForm(root, SWT.HORIZONTAL);

		treeViewerComposite = new Composite(sashForm, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 1;
		treeViewerComposite.setLayout(layout);
		treeViewerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite treeModelComposite = new Composite(sashForm, SWT.NONE);
		treeModelComposite.setLayout(new GridLayout());

		stackComposite = new Composite(treeModelComposite, SWT.NONE);
		stackComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		slayout = new StackLayout();
		stackComposite.setLayout(slayout);
		initialContent = new Label(stackComposite, SWT.NONE);
		initialContent.setText("initial content");

		slayout.topControl = initialContent;
		stackComposite.layout();

		createViewer(treeViewerComposite);

		makeActions();
		hookDoubleClickAction();
		hookContextMenu();
		fillLocalToolBar();
		sashForm.setWeights(new int[] { 3, 7 });

	}

	private void createViewer(Composite parent) {

		tview = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL
				| SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(tview);

		tview.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tview.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		tview.setSorter(new ViewerSorter());
		tview.setLabelProvider(new T4EclipseLabelProvider());
		tview.setContentProvider(new T4EclipseTreeContentProvider());
		tview.setInput(ContentExtentionProvider.getDefault());
		getSite().setSelectionProvider(tview);

	}

	@Override
	public void setFocus() {
	}

	protected void showContentInStack(TreeModel model) {
		// when no selection, model will be null
		if (model != null) {
			Composite composite = this.compositeMap.get(model.getId());
			if (composite == null) {
				// not create before
				composite = createComposite(model);
				this.compositeMap.put(model.getId(), composite);
			}
			slayout.topControl = composite;
		} else {
			slayout.topControl = this.initialContent;
		}
		stackComposite.layout();
	}

	@SuppressWarnings("unchecked")
	private Composite createComposite(TreeModel model) {
		try {

			// TODO: consider in other plug-ins
			Class s = Class.forName(model.getClassID());
			IToolPageContent instance = (IToolPageContent) s.newInstance();
			Composite p = instance.createControl(this.stackComposite);

			return p;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private void fillLocalToolBar() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = bars.getToolBarManager();
		toolBarManager.add(new Separator());
		drillDownAdapter.addNavigationActions(toolBarManager);
		toolBarManager
				.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void hookDoubleClickAction() {
		tview.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				ISelection selection = event.getSelection();
				TreeModel model = (TreeModel) ((IStructuredSelection) selection)
						.getFirstElement();

				if (model != T4EclipseContentView.this.treeModel) {
					T4EclipseContentView.this.treeModel = model;
					if (model != null) {
						T4EclipseContentView.this.showContentInStack(model);
					} else {

					}

				}

			}
		});

		tview.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				T4EclipseContentView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tview.getControl());
		tview.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tview);
	}

	private void fillContextMenu(IMenuManager manager) {

		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = tview.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(tview.getControl().getShell(),
				"Sample View", message);
	}
}
