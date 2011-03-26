package com.google.code.t4eclipse.tools.dialog;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;

import com.google.code.t4eclipse.tools.ktable.SimpleKTable;
import com.google.code.t4eclipse.tools.ktable.SimpleKTableModel;
import com.google.code.t4eclipse.tools.ktable.model.ControlAnalysisModel;
import com.google.code.t4eclipse.tools.utility.ReflctionProvider;

import de.kupzog.ktable.t4eclipse.KTableCellSelectionListener;

public class AnalyzeControlDialog extends Dialog {

	private Object obj;

	public AnalyzeControlDialog(Shell parentShell, Object c) {
		super(parentShell);
		this.obj = c;
 

		setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS);
        setBlockOnOpen(false);
	 
	}

	protected void configureShell(Shell newShell) {

		super.configureShell(newShell);
		newShell.setText("About T4Eclipse");
	
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		String[] methods = ReflctionProvider.getMethods(obj);

		String[] fields = ReflctionProvider.getFields(obj);

		Group analysis = new Group(parent, SWT.None);
		analysis.setText("Analysis");
		GridData analysisGroupgridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		analysisGroupgridData.minimumWidth = 600;
		analysisGroupgridData.minimumHeight = 500;

		analysis.setLayoutData(analysisGroupgridData);

		analysis.setLayout(new GridLayout(6, false));

		final Button runMethodButton = new Button(analysis, SWT.PUSH);
		runMethodButton.setText("RunMethod");
		GridData rumMethodData = new GridData();
		rumMethodData.horizontalAlignment = GridData.FILL;
		runMethodButton.setLayoutData(rumMethodData);

		final Label labelUseless = new Label(analysis, SWT.NULL);
		labelUseless.setText("  ");

		GridData isUIData = new GridData();
		isUIData.horizontalAlignment = GridData.FILL;
		labelUseless.setLayoutData(isUIData);

		final Combo methodCombo = new Combo(analysis, SWT.NULL);
		GridData methodComboData = new GridData(GridData.FILL_HORIZONTAL);
		methodComboData.horizontalSpan = 4;
		methodCombo.setLayoutData(methodComboData);

		final Button getFieldButton = new Button(analysis, SWT.PUSH);
		getFieldButton.setText("GetField");
		GridData getFieldData = new GridData();

		getFieldData.horizontalAlignment = GridData.FILL;
		getFieldButton.setLayoutData(getFieldData);
		// just for layout.
		Label label = new Label(analysis, SWT.NULL);
		label.setLayoutData(new GridData());

		final Combo fieldCombo = new Combo(analysis, SWT.NULL);
		GridData fieldComboData = new GridData(GridData.FILL_HORIZONTAL);
		fieldComboData.horizontalSpan = 4;
		fieldCombo.setLayoutData(fieldComboData);

		for (String f : fields) {
			fieldCombo.add(f);
		}
		for (String m : methods) {
			methodCombo.add(m);
		}

		final Text resultText = new Text(analysis, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		GridData textData = new GridData(GridData.FILL_VERTICAL
				| GridData.FILL_HORIZONTAL);
		textData.horizontalSpan = 6;
		resultText.setLayoutData(textData);

		// for run method
		runMethodButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				// run the selected method according to method name and ui
				// thread setup.
				String methodComboText = methodCombo.getText();
				if (methodComboText != null && methodComboText.length() > 0) {
					String result = ReflctionProvider.getMethodResultASStr(obj,
							methodComboText);
					resultText.setText(result);
				}

			}
		});

		// for get field
		getFieldButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				String fieldComboText = fieldCombo.getText();
				if (fieldComboText != null && fieldComboText.length() > 0) {
					String result = ReflctionProvider.getFieldContentAsStr(obj,
							fieldComboText);
					resultText.setText(result);
				}

			}
		});

		return parent;
	}

 
	protected boolean isResizable() {
		return true;
	}

}
