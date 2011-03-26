package com.google.code.t4eclipse.tools.dialog;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.google.code.t4eclipse.core.finder.Finder;
import com.google.code.t4eclipse.tools.utility.Constants;

public class LocateControlDialog extends Dialog {

	List<Class> list = new ArrayList<Class>();

	public LocateControlDialog(Shell parentShell, Control control) {
		super(parentShell);
		createContent(parentShell, control);

	}

	private void createContent(final Shell shell, final Control control) {

		GridLayout grid = new GridLayout();
		grid.numColumns = 6;

		shell.setLayout(grid);

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 1;
		final Label labelReturn = new Label(shell, SWT.NONE);
		labelReturn.setText("ReturnType:");
		labelReturn.setLayoutData(data);

		GridData dataC1 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		dataC1.horizontalSpan = 5;
		final Combo combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);

		getParentWidgets(list, control);

		for (int i = 0; i < list.size(); i++)
			combo.add(list.get(i).getName());
		combo.select(0);
		combo.setLayoutData(dataC1);

		GridData dataFind = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		dataFind.horizontalSpan = 1;
		final Label labelFind = new Label(shell, SWT.NONE);
		labelFind.setText("FindType:");
		labelFind.setLayoutData(dataFind);

		GridData dataC2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		dataC1.horizontalSpan = 5;

		final Combo combofind = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		List<Class> listfind = new ArrayList<Class>();
		getParentWidgets(listfind, control);

		for (int i = 0; i < listfind.size(); i++)
			combofind.add(listfind.get(i).getName());
		combofind.select(0);
		dataC2.horizontalSpan = 5;
		combofind.setLayoutData(dataC2);

		GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		textData.horizontalSpan = 6;
		textData.verticalSpan = 5;
		textData.heightHint = 400;
		textData.widthHint = 600;

		final Text text = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
		text.setLayoutData(textData);

		GridData dataButtonFind = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);

		dataButtonFind.horizontalIndent = 200;

		final Button checkCode = new Button(shell, SWT.PUSH);
		checkCode.setLayoutData(dataButtonFind);

		checkCode.setText("Find    ");

		checkCode.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				int returni = combo.getSelectionIndex();
				int findi = combofind.getSelectionIndex();
				Class returnClass = list.get(returni);
				Class findClass = list.get(findi);

				String program = findProgram(returnClass, findClass, control);
				text.setText(program);

			}

		});

		final Button cancel = new Button(shell, SWT.PUSH);
		GridData dataButton = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);

		cancel.setLayoutData(dataButton);
		cancel.setText("    Exit");
		cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				shell.dispose();

			}
		});

	}

	private String findProgram(Class returnClass, Class findClass,
			Control control) {
		Control rootControl = getRootControl(control);
		String id = getPartID(control);
		String type = getPartType(control);

		if (rootControl == null && id == null && type == null) {
			return "can not figure out!";
		}
		if (!"view".equals(type) && !"editor".equals(type)) {
			return "not in view or editor";
		}

		Widget[] widgets = Finder.getDefault().findAllByClass(
				getRootControl(control), findClass);
		if (widgets != null && widgets.length > 0) {
			for (int i = 0; i < widgets.length; i++) {
				if (widgets[i] == control) {

					return constructMethod(type, id, i + 1, returnClass,
							findClass);
				}
			}
		}
		return "not found";

	}

	private String constructMethod(String type, String id, int index,
			Class returnClass, Class findClass) {

		//String possibleImportConment = "//the following may needed to add to import part\n\n";
		String importReturnClass = "import " + returnClass.getName() + ";\n";
		String importfindClass = returnClass != findClass ? "import "
				+ findClass.getName() + ";\n\n" : "\n";
		String methodViewName = "findIndexByClassInEclipseView";
		String methodEditorName = "findIndexByClassInEclipseActiveEditor";
		String methodName = "view".equals(type) ? methodViewName
				: methodEditorName;

		
		String seTvar=returnClass.getSimpleName()+ " returnControl=\n";
		String returnTypeStr = returnClass == Control.class ? "" : "("
				+ returnClass.getSimpleName() + ")";
		String methodHead = "Finder.getDefault()." + methodName + "(\n";
		String idParaPart = "view".equals(type) ? ("\"" + id + "\",") : "";
		String otherPara = findClass.getSimpleName() + ".class" + "," + index;
		String methodEnd = ");\n\n";

		String assertNotNullDeclare="assertNotNull(returnControl);\n\n";
		
		return  importReturnClass + importfindClass+seTvar+returnTypeStr
				+ methodHead + idParaPart + otherPara + methodEnd+assertNotNullDeclare;
	}

	private Control getRootControl(Control control) {
		Control tmpControl = control;
		while (tmpControl != null) {
			if (tmpControl.getData(Constants.rootControlIDName) != null) {
				return tmpControl;
			}
			tmpControl = tmpControl.getParent();
		}
		return null;
	}

	private String getPartType(Control control) {
		Control rootControl = getRootControl(control);
		if (rootControl != null) {
			return (String) rootControl.getData(Constants.rootControlTypeName);
		}
		return null;
	}

	private String getPartID(Control control) {
		Control rootControl = getRootControl(control);
		if (rootControl != null) {
			return (String) rootControl.getData(Constants.rootControlIDName);
		}
		return null;
	}

	private void getParentWidgets(List<Class> list, Control model) {
		Class c = model.getClass();
		while (c != Widget.class) {
			list.add(c);
			c = c.getSuperclass();
		}

	}

}
