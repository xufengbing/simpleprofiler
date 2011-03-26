package com.google.code.t4eclipse.tools.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.google.code.t4eclipse.tools.tree.TreeItemAnalysisModel;

/**
 * this view is created to analysis a pure tree element<br>
 * first version show text only.
 *
 * @author fengbixu
 *
 */
public class TreeAnalysisView extends ViewPart {

	public final static String VIEW_ID="net.sourceforge.eclipse-testlib.tools.view.treeview";

	private Text text;
	private Tree tree;
	public TreeAnalysisView() {
		super();
	}

	public void setTree(Tree tree) {
		this.tree = tree;
		refreshContent();
	}

	private void refreshContent() {
		if(tree!=null&&!tree.isDisposed()){
			TreeItem[] items=tree.getSelection();
			if(items!=null&&items.length==1){

				TreeItem first=items[0];
				TreeItemAnalysisModel m=new TreeItemAnalysisModel(first);
				this.text.setText(m.getItemDetail());
				return;
			}

		}
		this.text.setText("");
	}







	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		final Button b = new Button(parent, SWT.PUSH);
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b.setText("Refresh");

		final Button c = new Button(parent, SWT.PUSH);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c.setText("Clean");

		text = new Text(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		b.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {


			}

			public void widgetSelected(SelectionEvent e) {
				refreshContent();
			}
		});
		c.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				tree=null;
				text.setText("");

			}
		});

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	/**
	 * Called when the View is to be disposed
	 */
	public void dispose() {
		this.tree=null;
		this.text.dispose();
		super.dispose();
	}
}
