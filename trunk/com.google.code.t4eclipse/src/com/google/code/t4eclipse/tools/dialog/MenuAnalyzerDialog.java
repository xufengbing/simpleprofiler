package com.google.code.t4eclipse.tools.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class MenuAnalyzerDialog extends Dialog {

	public MenuAnalyzerDialog(Shell parentShell) {
		super(parentShell);
//		setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS);
		setBlockOnOpen(false);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Menu Analyzer Dialog");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
 
 		Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText("Column 1");
		column1.setWidth(200);
		TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
		column2.setText("Column 2");
		column2.setWidth(200);
		TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
		column3.setText("Column 3");
		column3.setWidth(200);
		for (int i = 0; i < 4; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(new String[] { "item " + i, "abc", "defghi" });
			for (int j = 0; j < 4; j++) {
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				subItem.setText(new String[] { "subitem " + j, "jklmnop", "qrs" });
				for (int k = 0; k < 4; k++) {
					TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
					subsubItem.setText(new String[] { "subsubitem " + k, "tuv", "wxyz" });
				}
			}
		}
 		parent.pack();
		return parent;
	}

}
