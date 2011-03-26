package com.google.code.t4eclipse.tools.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.google.code.t4eclipse.selection.SystemImageModel;

public class SytemImageView extends ViewPart {

	public static final String[] COLUMNS = new String[] { "Name", "Image",
			"Code Snippet" };

	public static final int[] COL_WIDTH = new int[] { 200, 100, 300 };

	private TableViewer viewer;

	private Action CopyProgramAction;

	private Vector<Image> imageList;

	public SytemImageView() {
		imageList = new Vector<Image>();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createViewer(composite);
		viewer.setInput(SystemImageModel.getAllModels());

		getSite().setSelectionProvider(viewer);

		hookContextMenu();

		IActionBars actionBar = getViewSite().getActionBars();
		actionBar.setGlobalActionHandler(ActionFactory.COPY.getId(),
				this.CopyProgramAction);

		this.viewer.setSelection(StructuredSelection.EMPTY);
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);

		Menu menu = menuMgr.createContextMenu(this.viewer.getTable());
		CopyProgramAction = new Action("&Copy Code Snippet        Ctrl+C") {

			@Override
			public void run() {

				ISelection sel = SytemImageView.this.viewer.getSelection();
				if (sel instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) sel;
					Object model = selection.getFirstElement();

					if (model != null && model instanceof SystemImageModel) {
						SystemImageModel imageModel = (SystemImageModel) selection
								.getFirstElement();
						TextTransfer plainTextTransfer = TextTransfer
								.getInstance();
						String plainText = imageModel.getCode();
						if (plainText == null || plainText.length() == 0) {// check

							return;
						}
						Clipboard clipboard = new Clipboard(Display
								.getDefault());
						try {
							clipboard.setContents(new String[] { plainText },
									new Transfer[] { plainTextTransfer });
						} catch (SWTError error) {
							if (error.code != DND.ERROR_CANNOT_SET_CLIPBOARD) {
								throw error;
							}
						} finally {
							clipboard.dispose();
						}

					}
				}

			}

		};

		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				menuMgr.add(CopyProgramAction);
			}

		});

		this.viewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.viewer);
	}

	private void createViewer(Composite parent) {

		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		viewer.getTable().setBackground(
				parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.getTable().setLinesVisible(true);
		createColumns(viewer.getTable());

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new SystemImageTableLabelProvider());

	}

	private void createColumns(Table table) {

		for (int i = 0; i < COLUMNS.length; i++) {
			TableColumn col = new TableColumn(table, SWT.LEFT);
			col.setText(COLUMNS[i]);
			col.setWidth(COL_WIDTH[i]);
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		for (Image image : this.imageList) {
			image.dispose();
		}
		super.dispose();
	}

	class SystemImageTableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			SystemImageModel model = (SystemImageModel) element;

			if (columnIndex == 1) {
				Image image = model.getImage();
				if (image != null) {
					// fix the problem image is displayed smaller than expected
					// because of size different
					Image image32 = new Image(image.getDevice(), 32, 32);
					Color white = Display.getDefault().getSystemColor(
							SWT.COLOR_WHITE);
					GC gc = new GC(image32);
					gc.setBackground(white);
					gc.drawImage(image, 0, 0);
					gc.dispose();
					SytemImageView.this.imageList.add(image32);
					return image32;
				} else {
					// image may be null in eclipse 3.5 release. 3.6 is ok
					// System.out
					// .println("image is null.........................");

				}
			}

			return null;
		}

		public String getColumnText(Object element, int columnIndex) {

			SystemImageModel model = (SystemImageModel) element;

			if (columnIndex == 0)
				return model.getName();
			if (columnIndex == 2) {
				if (model.getImage() != null) {
					return model.getCode();
				}
			}
			if (columnIndex == 1) {
				Image image = model.getImage();
				if (image != null)
					return image.getBounds().width + "x"
							+ image.getBounds().height;
			}

			return "";
		}

	}
}
