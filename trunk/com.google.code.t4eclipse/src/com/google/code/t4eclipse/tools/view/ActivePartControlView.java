package com.google.code.t4eclipse.tools.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.code.t4eclipse.core.utility.ControlUtility;
import com.google.code.t4eclipse.core.utility.MenuUtility;
import com.google.code.t4eclipse.selection.ControlSelection;
import com.google.code.t4eclipse.selection.RootControlSelection;
import com.google.code.t4eclipse.tools.utility.SelectionUtility;
import com.google.code.t4eclipse.tools.view.provider.ControlContentProvider;
import com.google.code.t4eclipse.tools.view.provider.ControlLabelProvider;
import com.google.code.t4eclipse.tools.action.AnalysisMenuAction;

public class ActivePartControlView extends ViewPart {
	public static ActivePartControlView view;
	private Text headText;
	private Text infoSashText;
	private SashForm sashForm;
	private Composite treeViewerComposite;
	private TreeViewer viewer;
	private IWorkbenchPart part;
	private IPartListener listener;

	public void showRootControl(Control rootControl) {
		headText.setText("");
		this.part = null;
		infoSashText.setText("");
		viewer.setSelection(StructuredSelection.EMPTY);
		viewer.setInput(new RootControlSelection(rootControl));
		viewer.expandAll();

	}

	public void showAcitivePart(IWorkbenchPart part) {
		Control c = ControlUtility.getPartControl(part);
		this.part = part;

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPartListener(listener);

		headText.setText(ControlUtility.getActivePartDiscription(this.part));

		infoSashText.setText(SelectionUtility.getSelectionInfo(this.part));
		viewer.setInput(new RootControlSelection(c));
		viewer.expandAll();
		Control focusControl = ControlUtility.getFocusControl(c);
		if (focusControl != null) {
			Tree tree = viewer.getTree();
			ControlUtility.selectFocusedTreeItem(tree);
		}

	}

	@Override
	public void createPartControl(Composite root) {
		root.setLayout(new FillLayout());
		sashForm = new SashForm(root, SWT.HORIZONTAL);

		treeViewerComposite = new Composite(sashForm, SWT.NONE);
		infoSashText = new Text(sashForm, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);

		// Composite composite = new Composite(parent, SWT.NONE); // in method
		// use parent
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 1;
		treeViewerComposite.setLayout(layout);
		treeViewerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		headText = new Text(treeViewerComposite, SWT.SINGLE | SWT.READ_ONLY);

		headText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createViewer(treeViewerComposite);

		sashForm.setWeights(new int[] { 6, 4 });

		getSite().setSelectionProvider(viewer);
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);

		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				if (ActivePartControlView.this.part != null
						&& ActivePartControlView.this.part instanceof IViewPart) {

					manager.add(new Separator());
					Action openToolBarAction = new Action() {
						@Override
						public void run() {

							ToolBar bar = MenuUtility
									.getviewToolBar(ActivePartControlView.this.part);

							if (bar.getItems() == null
									|| bar.getItemCount() == 0) {
								MessageDialog
										.openConfirm(Display.getCurrent()
												.getActiveShell(), "Confirm",
												"No View toolBar action contributed in this view!\n");

							}

							else {
								try {
									IViewPart view = PlatformUI
											.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage()
											.showView(
													"com.google.code.t4eclipse.tools.view.ToolBarAnalyzerView");
									if (view != null
											&& view instanceof ToolBarAnalyzerView) {
										ToolBarAnalyzerView ma = (ToolBarAnalyzerView) view;
										ma.update(bar);
									}
								} catch (PartInitException e) {
								}
							}

						}
					};

					openToolBarAction.setText("analyze view toolbar");

					Action openViewMenuAction = new Action() {
						@Override
						public void run() {

							Menu menu = MenuUtility
									.getViewMenu(ActivePartControlView.this.part);
							if (menu == null || menu.getItemCount() == 0) {
								MessageDialog
										.openConfirm(
												Display.getCurrent()
														.getActiveShell(),
												"Confirm",
												"No View menu action contributed in this view\n"
														+ "Or the menu is not showed before!\n"
														+ "Please mannually show the toolbar menu before analyze it\n"
														+ " if toolbar menu exists for the view "
														+ "");
							}
							if (menu != null) {
								try {
									IViewPart view = PlatformUI
											.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage()
											.showView(
													"com.google.code.t4eclipse.tools.view.MenuAnalyzerView");
									if (view != null
											&& view instanceof MenuAnalyzerView) {
										MenuAnalyzerView ma = (MenuAnalyzerView) view;
										ma.update(menu);
									}
								} catch (PartInitException e) {
								}
							}
							//

						}
					};

					openViewMenuAction.setText("analyze view menu");
					manager.add(openToolBarAction);

					manager.add(openViewMenuAction);
					manager.add(new Separator());
				}

			}
		});
		Menu menu = menuMgr.createContextMenu(this.viewer.getTree());

		this.viewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.viewer);

		view = this;

		listener = new IPartListener() {

			public void partOpened(IWorkbenchPart part) {

			}

			public void partDeactivated(IWorkbenchPart part) {

			}

			public void partClosed(IWorkbenchPart part) {

				if (part != null && !(part instanceof ActivePartControlView)) {
					if (part == ActivePartControlView.this.part) {
						ActivePartControlView.this.cleanContent();
					}
				}

			}

			public void partBroughtToTop(IWorkbenchPart part) {

			}

			public void partActivated(IWorkbenchPart part) {

			}
		};

		sashForm.setMaximizedControl(treeViewerComposite);
	}

	private void createViewer(Composite parent) {
		viewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL
				| SWT.V_SCROLL);

		viewer.getTree().setBackground(
				parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		// fFilteredTree.setInitialText(Messages.LogView_show_filter_initialText);

		viewer.getTree().setLinesVisible(true);
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		createColumns(viewer.getTree());
		viewer.setContentProvider(new ControlContentProvider());

		viewer.setLabelProvider(new ControlLabelProvider());
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {

				ISelection sel = viewer.getSelection();
				ActivePartControlView.this.infoSashText.setText(ControlUtility
						.getSelectionText(sel));

			}
		});

	}

	private void createColumns(Tree tree) {

		for (int i = 0; i < ControlUtility.COLUMNS.length; i++) {
			TreeColumn col = new TreeColumn(tree, SWT.LEFT);
			col.setText(ControlUtility.COLUMNS[i]);
			col.setWidth(ControlUtility.COL_WIDTH[i]);
		}

		tree.setHeaderVisible(true);

	}

	@Override
	public void setFocus() {
		// DO nothing
	}

	@Override
	public void dispose() {
		view = null;
		super.dispose();
	}

	public TreeViewer getTreeViewer() {

		return this.viewer;
	}

	public void toggleProp(IAction action) {
		sashForm.setMaximizedControl(action.isChecked() ? null
				: treeViewerComposite);
	}

	public void cleanContent() {
		if (!this.headText.isDisposed())
			this.headText.setText("");
		if (!this.infoSashText.isDisposed())
			this.infoSashText.setText("");
		if (!this.viewer.getTree().isDisposed()) {
			this.viewer.setSelection(StructuredSelection.EMPTY);
			this.viewer.setInput(null);
		}

	}
}
