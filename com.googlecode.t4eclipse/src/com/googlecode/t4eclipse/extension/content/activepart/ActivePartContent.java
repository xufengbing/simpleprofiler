package com.googlecode.t4eclipse.extension.content.activepart;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.core.utility.ControlUtility;
import com.google.code.t4eclipse.tools.view.provider.ControlContentProvider;
import com.google.code.t4eclipse.tools.view.provider.ControlLabelProvider;
import com.googlecode.t4eclipse.extension.IToolPageContent;

public class ActivePartContent implements IToolPageContent {
	private TreeViewer viewer;
	
	@Override
	public Composite createControl(Composite parent) {
		Composite p=new Composite(parent,SWT.NONE);
		p.setLayout(new GridLayout());
		
		Composite treeViewerComposite = new Composite(p, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 1;
		treeViewerComposite.setLayout(layout);
		treeViewerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Text headText = new Text(treeViewerComposite, SWT.SINGLE | SWT.READ_ONLY);
		headText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createViewer(treeViewerComposite);

		return p;
 
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
	public void fillLocalToolBar(IToolBarManager manager) {
		Action action1;
		Action action2;
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		manager.add(action1);
		manager.add(action2);

	}

	private void showMessage(String message) {
		MessageDialog.openInformation(Display.getDefault().getActiveShell(),
				"DEMO:", message);
	}
}
