package com.google.code.t4eclipse.tools.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PartPane;
import org.eclipse.ui.internal.PartSite;

import com.google.code.t4eclipse.core.utility.ExceptionUtility;
import com.google.code.t4eclipse.core.utility.ReflectionUtil;
import com.google.code.t4eclipse.core.utility.ReflectionUtil.ObjectResult;
import com.google.code.t4eclipse.tools.action.OpenJavaTypeAction;
import com.google.code.t4eclipse.tools.eventtrack.DisplayListener;
import com.google.code.t4eclipse.tools.eventtrack.Utility;
import com.google.code.t4eclipse.tools.ktable.RowModel;
import com.google.code.t4eclipse.tools.ktable.SimpleKTable;
import com.google.code.t4eclipse.tools.ktable.SimpleKTableModel;
import com.google.code.t4eclipse.tools.ktable.model.ControlAnalysisModel;
import com.google.code.t4eclipse.tools.ktable.model.EclipseExtentionInfoProvider;
import com.google.code.t4eclipse.tools.ktable.model.EclipseModelDataProvider;
import com.google.code.t4eclipse.tools.ktable.model.EditorModel;
import com.google.code.t4eclipse.tools.ktable.model.LocalToolBarItemModel;
import com.google.code.t4eclipse.tools.ktable.model.MainMenuItemModel;
import com.google.code.t4eclipse.tools.ktable.model.MainToolBarItemModel;
import com.google.code.t4eclipse.tools.ktable.model.PerferenceModel;
import com.google.code.t4eclipse.tools.ktable.model.PerspectiveModel;
import com.google.code.t4eclipse.tools.ktable.model.ShellInfoModel;
import com.google.code.t4eclipse.tools.ktable.model.ViewModel;
import com.google.code.t4eclipse.tools.ktable.model.WizardModel;
import com.google.code.t4eclipse.tools.ktable.model.WorkpartModel;
import com.google.code.t4eclipse.tools.utility.Constants;
import com.google.code.t4eclipse.tools.utility.ReflctionProvider;
import com.google.code.t4eclipse.tools.utility.ShellListnerChecker;

import de.kupzog.ktable.t4eclipse.KTableCellSelectionListener;
import de.kupzog.ktable.t4eclipse.KTableModel;

public class MainSWT {
	private IViewPart view;

	private TabFolder tabFolder;

	private Composite composite;

	private SimpleKTable wizardTable;

	private SimpleKTable perfTale;

	private SimpleKTable perspectiveTable;

	private SimpleKTable toolBarTable;

	private SimpleKTable menuTable;

	private SimpleKTable viewTable;

	private SimpleKTable editorTable;

	// view action part var
	private SimpleKTable actionViewTable;

	private Label viweIDLabelInViewActionTab;

	// end of view action

	private SimpleKTable shellInfoKTable;

	// active part vars
	private SimpleKTable activePartTable;

	private Label activepartType;

	private Label activePartID;

	private Label activePartClassName;

	// end of active part
	private SimpleKTable analysisControlTable;

	private Listener shellActivatelistener;

	public StyledText utilityViewText;

	public StyledText ktableText;

	// /variables that need to move out of the class scope by refactoring
	public static final Text[] texts = new Text[2];

	public static final ArrayList<String> strList = new ArrayList<String>();

	// end of variables

	// public static final String[] TabNames = new String[] { "Wizards",
	// "Preference", "Menu", "ToolBar", "Perspective", "View", "Editor",
	// "ViewActions", "ActivePart", "Shell", "Control"};

	public TabFolder getTabFolder() {
		return this.tabFolder;
	}

	public void addFolder(Composite parent) {
		// TODO Auto-generated constructor stub
		tabFolder = new TabFolder(parent, SWT.NONE);
		final TabItem item0 = new TabItem(tabFolder, SWT.NONE);
		item0.setText("Wizard");
		Composite composite0 = new Composite(tabFolder, SWT.NULL);
		composite0.setLayout(new FillLayout());
		createWizardTable(composite0);
		item0.setControl(composite0);

		final TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Preference");
		Composite composite1 = new Composite(tabFolder, SWT.NULL);
		composite1.setLayout(new FillLayout());
		createPreftable(composite1);
		item1.setControl(composite1);

	

//		final TabItem item3 = new TabItem(tabFolder, SWT.NONE);
//		Composite composite3 = new Composite(tabFolder, SWT.NULL);
//		composite3.setLayout(new FillLayout());
//		createToolBarTable(composite3);
//		item3.setControl(composite3);
//		item3.setText("ToolBar");

		final TabItem item5 = new TabItem(tabFolder, SWT.NONE);
		Composite composite5 = new Composite(tabFolder, SWT.NULL);
		composite5.setLayout(new FillLayout());
		createPerspectiveTable(composite5);
		item5.setControl(composite5);
		item5.setText("Perspective");

		final TabItem item4 = new TabItem(tabFolder, SWT.NONE);
		Composite composite4 = new Composite(tabFolder, SWT.NULL);
		composite4.setLayout(new FillLayout());
		createViewTable(composite4);
		item4.setControl(composite4);
		item4.setText("View");

		final TabItem item7 = new TabItem(tabFolder, SWT.NONE);
		Composite composite7 = new Composite(tabFolder, SWT.NULL);
		composite7.setLayout(new FillLayout());
		createEditorTable(composite7);
		item7.setControl(composite7);
		item7.setText("Editor");

		final TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		Composite composite2 = new Composite(tabFolder, SWT.NULL);
		composite2.setLayout(new FillLayout());
		createMenuTable(composite2);
		item2.setControl(composite2);
		item2.setText("Menu Path");
		// final TabItem itemViewAction = new TabItem(tabFolder, SWT.NONE);
		// Composite compositeViewActions = new Composite(tabFolder, SWT.NULL);
		// compositeViewActions.setLayout(new FillLayout());
		// createViewActionTable(compositeViewActions);
		// itemViewAction.setControl(compositeViewActions);
		// itemViewAction.setText("ViewAction");

		// final TabItem item13 = new TabItem(tabFolder, SWT.NONE);
		// Composite composite13 = new Composite(tabFolder, SWT.NULL);
		// composite13.setLayout(new FillLayout());
		// createActivePart(composite13);
		// item13.setControl(composite13);
		// item13.setText("ActivePart");

		final TabItem item11 = new TabItem(tabFolder, SWT.NONE);
		Composite composite11 = new Composite(tabFolder, SWT.NULL);
		composite11.setLayout(new FillLayout());
		createShellLogger(composite11);
		item11.setControl(composite11);
		item11.setText("Shell");

		 final TabItem item12 = new TabItem(tabFolder, SWT.NONE);
		 Composite composite12 = new Composite(tabFolder, SWT.NULL);
		 composite12.setLayout(new FillLayout());
		 createEventDebugger(composite12);
		 item12.setControl(composite12);
		 item12.setText("Event");

		 final TabItem item14 = new TabItem(tabFolder, SWT.NONE);
		 Composite composite14 = new Composite(tabFolder, SWT.NULL);
		 composite14.setLayout(new FillLayout());
		 createControl(composite14);
		 item14.setControl(composite14);
		 item14.setText("OldControl");

		 final TabItem item15 = new TabItem(tabFolder, SWT.NONE);
		 Composite composite15 = new Composite(tabFolder, SWT.NULL);
		
		 composite15.setLayout(new FillLayout());
		
		 createControlAnalysis(composite15);
		 item15.setControl(composite15);
		 item15.setText("Control");

	}

	public MainSWT(Composite parent, IViewPart view) {
		this.view = view;
		this.composite = parent;
		addFolder(this.composite);
		addFilterDisplay();
	}

	private void addFilterDisplay() {
		final SimpleKTable table = shellInfoKTable;
		shellActivatelistener = new Listener() {

			public void handleEvent(Event event) {

				if (ShellListnerChecker.getCheck() && table != null
						&& !table.isDisposed() && event.widget != null
						&& !event.widget.isDisposed()
						&& event.widget instanceof Shell) {

					Shell shell = (Shell) event.widget;

					String title = shell.getText() == null ? "" : shell
							.getText();
					String classData = shell.getData() == null ? "" : shell
							.getData().getClass().getName();
					String info = "";
					if ("org.eclipse.jface.wizard.WizardDialog"
							.endsWith(classData)) {
						org.eclipse.jface.wizard.WizardDialog dialog = (WizardDialog) shell
								.getData();

						IWizard wizard = (IWizard) ReflctionProvider
								.invokeMethod("getWizard", dialog);
						if (wizard != null)
							info = Constants.classStartInInfo
									+ wizard.getClass().getName()
									+ Constants.classEndInInfo;
					}

					ShellInfoModel shellIModel = new ShellInfoModel(classData,
							title, info);
					SimpleKTableModel model = (SimpleKTableModel) table
							.getModel();
					model.addKTableRow(shellIModel);
					table.redraw();
					table.update();

				}
			}
		};
		Display.getDefault().addFilter(SWT.Dispose, shellActivatelistener);
	}

	private void createWizardTable(Composite composite) {
		composite.setLayout(new GridLayout());

		wizardTable = new SimpleKTable(composite);
		wizardTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<WizardModel> model = new SimpleKTableModel<WizardModel>(
				WizardModel.class);
		wizardTable.setModel(model);
		wizardTable.addMenuListener();
		// model.update(EclipseModelDataProvider.getDefault().getWizardList());
		// wizardTable.redraw();
	}

	private void createPreftable(Composite composite) {
		composite.setLayout(new GridLayout());

		perfTale = new SimpleKTable(composite);
		perfTale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<PerferenceModel> model = new SimpleKTableModel<PerferenceModel>(
				PerferenceModel.class);
		perfTale.setModel(model);
		perfTale.addMenuListener();
		// model.update(EclipseModelDataProvider.getDefault().getPrefList());
		// perfTale.redraw();
	}

	private void createPerspectiveTable(Composite composite) {
		composite.setLayout(new GridLayout());
		perspectiveTable = new SimpleKTable(composite);
		perspectiveTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));

		SimpleKTableModel<PerspectiveModel> model = new SimpleKTableModel<PerspectiveModel>(
				PerspectiveModel.class);
		perspectiveTable.setModel(model);
		perspectiveTable.addMenuListener();
		// model
		// .update(EclipseModelDataProvider.getDefault()
		// .getPerspectiveList());
		// perspectiveTable.redraw();

	}

	private void createViewTable(Composite composite) {
		composite.setLayout(new GridLayout());
		viewTable = new SimpleKTable(composite);
		viewTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<ViewModel> model = new SimpleKTableModel<ViewModel>(
				ViewModel.class);
		viewTable.setModel(model);
		viewTable.addMenuListener();
		// model.update(EclipseModelDataProvider.getDefault().getViewModelList());
		// viewTable.redraw();
	}

	// this class is used to create the toolitem menu and actions for view.

	private void createViewActionTable(Composite composite) {
		composite.setLayout(new GridLayout());
		viweIDLabelInViewActionTab = new Label(composite, SWT.None);
		viweIDLabelInViewActionTab.setText("");
		viweIDLabelInViewActionTab.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		actionViewTable = new SimpleKTable(composite);
		actionViewTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<LocalToolBarItemModel> model = new SimpleKTableModel<LocalToolBarItemModel>(
				LocalToolBarItemModel.class);
		actionViewTable.setModel(model);
		actionViewTable.addMenuListener();
	}

	/**
	 * eclipse toolbar
	 * 
	 * @param composite
	 */
	private void createToolBarTable(Composite composite) {
		composite.setLayout(new GridLayout());
		toolBarTable = new SimpleKTable(composite);
		toolBarTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<MainToolBarItemModel> model = new SimpleKTableModel<MainToolBarItemModel>(
				MainToolBarItemModel.class);
		toolBarTable.setModel(model);
		toolBarTable.addMenuListener();
	}

	/**
	 * eclipse menu
	 * 
	 * @param composite
	 */
	private void createMenuTable(Composite composite) {

		// menuTable = new KTable(composite, SWT.V_SCROLL | SWT.H_SCROLL
		composite.setLayout(new GridLayout());
		menuTable = new SimpleKTable(composite);
		menuTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<MainMenuItemModel> model = new SimpleKTableModel<MainMenuItemModel>(
				MainMenuItemModel.class);
		menuTable.setModel(model);
		menuTable.addMenuListener();

	}

	private void createEditorTable(Composite composite6) {
		// use a label to print the view ID
		composite6.setLayout(new GridLayout());
		editorTable = new SimpleKTable(composite6);
		editorTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<EditorModel> model = new SimpleKTableModel<EditorModel>(
				EditorModel.class);
		editorTable.setModel(model);
		editorTable.addMenuListener();
	}

	// private void createActivePartDetailTable(Composite composite6) {
	// composite6.setLayout(new GridLayout());
	//
	// activePartTypeLabelInViewDetailTab = new Label(composite6, SWT.None);
	// activePartTypeLabelInViewDetailTab.setText("");
	// activePartTypeLabelInViewDetailTab.setLayoutData(new GridData(
	// GridData.FILL_HORIZONTAL));
	//
	// activePartIDLabelInViewDetailTab = new Label(composite6, SWT.None);
	// activePartIDLabelInViewDetailTab.setText("");
	// activePartIDLabelInViewDetailTab.setLayoutData(new GridData(
	// GridData.FILL_HORIZONTAL));
	//
	// detailActivePartTable = new KTableTree(composite6, SWT.V_SCROLL
	// | SWT.H_SCROLL | SWT.MULTI | SWTX.EDIT_ON_KEY);
	// detailActivePartTable.setLayoutData(new GridData(
	// GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
	//
	// detailActivePartTable.setModel(getKTableTreeDetailModelForActivePart());
	//
	// final Menu menu = new Menu(PlatformUI.getWorkbench()
	// .getWorkbenchWindows()[0].getShell());
	// detailActivePartTable.setMenu(menu);
	//
	// detailActivePartTable
	// .addCellSelectionListener(new KTableCellSelectionListener() {
	//
	// public void cellSelected(int col, int row, int statemask) {
	// KTableMenuUtility.addMenuItem(detailActivePartTable,
	// col, row, menu);
	// }
	//
	// public void fixedCellSelected(int col, int row,
	// int statemask) {
	//
	// }
	// });
	// }

	private void createActivePart(Composite composite6) {

		composite6.setLayout(new GridLayout());
		activepartType = new Label(composite6, SWT.None);
		activepartType.setText("");
		activepartType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		activePartID = new Label(composite6, SWT.None);
		activePartID.setText("");
		activePartID.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		activePartClassName = new Label(composite6, SWT.None);
		activePartClassName.setText("");
		activePartClassName
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		activePartClassName.addListener(SWT.MouseDoubleClick, new Listener() {

			public void handleEvent(Event event) {
				Label l = (Label) event.widget;
				String str = l.getText();
				if (str != null && str.startsWith("class:")) {
					String className = str.substring(6, str.length());
					new OpenJavaTypeAction(className).run();
				}
			}
		});

		this.activePartTable = new SimpleKTable(composite6);
		activePartTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		SimpleKTableModel<WorkpartModel> model = new SimpleKTableModel<WorkpartModel>(
				WorkpartModel.class);
		activePartTable.setModel(model);

		activePartTable.addMenuListener();
	}

	private void createControlAnalysis(Composite composite) {

		GridLayout gridLayout = new GridLayout(3, false);
		;
		composite.setLayout(gridLayout);

		// controls group
		Group controls = new Group(composite, SWT.None);
		controls.setText("Controls");
		GridData controlGroupgridData = new GridData(GridData.FILL_VERTICAL);
		controlGroupgridData.widthHint = 300;

		controls.setLayoutData(controlGroupgridData);

		GridLayout conrolGroupgridLayout = new GridLayout();
		controls.setLayout(conrolGroupgridLayout);

		analysisControlTable = new SimpleKTable(controls);
		GridData d2 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);

		analysisControlTable.setLayoutData(d2);
		SimpleKTableModel<ControlAnalysisModel> model = new SimpleKTableModel<ControlAnalysisModel>(
				ControlAnalysisModel.class);
		analysisControlTable.setModel(model);
		analysisControlTable.addMenuListener();

		final Button analysisB = new Button(composite, SWT.PUSH);
		analysisB.setText("=>");
		analysisB.setToolTipText("Analysis the selected control");
		analysisB.setLayoutData(new GridData());
		analysisB.setEnabled(false);

		analysisControlTable.setLayoutData(d2);

		Group analysis = new Group(composite, SWT.None);
		analysis.setText("Analysis");
		GridData analysisGroupgridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
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

		final Text resultText = new Text(analysis, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		GridData textData = new GridData(GridData.FILL_VERTICAL
				| GridData.FILL_HORIZONTAL);
		textData.horizontalSpan = 6;
		resultText.setLayoutData(textData);

		// the selection listener.

		// bor button =>
		analysisB.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {

				// add all the method and field to the method combo and field
				Point[] selects = analysisControlTable.getCellSelection();
				if (selects != null && selects.length > 0) {
					methodCombo.removeAll();
					fieldCombo.removeAll();
					int row = selects[0].y;
					SimpleKTableModel<ControlAnalysisModel> model = (SimpleKTableModel<ControlAnalysisModel>) analysisControlTable
							.getModel();
					ControlAnalysisModel obj = (ControlAnalysisModel) model
							.getContentAt(-1, row);
					// System.err.println(obj.getObject());
					// set the object to be analysised to the data.
					analysisB.setData(obj.getObject());

					String[] methods = ReflctionProvider.getMethods(obj
							.getObject());

					for (String m : methods) {
						methodCombo.add(m);
					}
					String[] fields = ReflctionProvider.getFields(obj
							.getObject());
					for (String f : fields) {
						fieldCombo.add(f);
					}

				} else {
					methodCombo.removeAll();
					fieldCombo.removeAll();
					MessageDialog.openWarning(analysisB.getShell(), "Warning",
							"Select object to analysis first!");
				}
				analysisB.setEnabled(false);

			}
		});

		//
		analysisControlTable
				.addCellSelectionListener(new KTableCellSelectionListener() {

					public void cellSelected(int col, int row, int statemask) {
						analysisControlTable.redraw();
						analysisB.setEnabled(true);
					}

					public void fixedCellSelected(int col, int row,
							int statemask) {

					}
				});

		// for run method
		runMethodButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// run the selected method according to method name and ui
				// thread setup.
				String methodComboText = methodCombo.getText();
				if (methodComboText != null && methodComboText.length() > 0) {
					String result = ReflctionProvider.getMethodResultASStr(
							analysisB.getData(), methodComboText);
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
					String result = ReflctionProvider.getFieldContentAsStr(
							analysisB.getData(), fieldComboText);
					resultText.setText(result);
				}

			}
		});

	}

	private void createControl(Composite composite14) {

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;

		composite14.setLayout(gridLayout);

		final Text method = new Text(composite14, SWT.SINGLE);
		method.setSize(200, 40);
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 1;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.minimumWidth = 300;
		method.setLayoutData(gridData2);

		final Button runMethodbutton = new Button(composite14, SWT.PUSH);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		runMethodbutton.setLayoutData(gridData);
		runMethodbutton.setText("&Run Method");
		final Button NonUIRunButton = new Button(composite14, SWT.PUSH);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		NonUIRunButton.setLayoutData(gridData);
		NonUIRunButton.setText("&NonUIRun");

		final Text field = new Text(composite14, SWT.SINGLE);
		field.setSize(200, 40);
		gridData2 = new GridData();
		gridData2.horizontalSpan = 1;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.minimumWidth = 300;
		field.setLayoutData(gridData2);

		final Button getFiledButton = new Button(composite14, SWT.PUSH);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		getFiledButton.setLayoutData(gridData);
		getFiledButton.setText("&Get Field");

		final Text result = new Text(composite14, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		result.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));

		getFiledButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				result.setText("");
				Widget w = getWidgetToAnalysis();
				if (w == null) {
					result.setText("Nothing to Analysis");
					return;
				}
				String text = field.getText().trim();

				if (text.length() > 0) {
					ObjectResult oresult = ReflectionUtil.getField(text, w);
					if (oresult.result != null) {
						String resultText = "----------------Field----------------\n";
						resultText += "Return Type:     "
								+ oresult.result.getClass().getName() + "\n";
						resultText += "Result toStr:    "
								+ oresult.result.toString() + "\n";
						result.setText(resultText);
						return;
					} else if (oresult.methodOrFieldFound == false) {
						result.setText("Field can not be found!");
						return;
					} else if (oresult.ex != null) {
						result.setText("Exception Throwed:\n"
								+ oresult.ex.getMessage());
						return;
					} else if (oresult.result == null) {
						result.setText("returned a null!");
					}
				}

			}

		});

		NonUIRunButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// Tree tree;
				m();
			}

			private void m() {
				result.setText("");
				Widget w = getWidgetToAnalysis();
				if (w == null) {
					result.setText("Nothing to Analysis");
					return;
				}
				String text = method.getText().trim();
				if (text.length() > 0) {
					ObjectResult oresult = ReflectionUtil
							.invokeMethodInNewThread(text, w);
					if (oresult.result != null) {
						String resultText = "--------------Method Run In non-UI------------------\n";
						resultText += "Return Type:     "
								+ oresult.result.getClass().getName() + "\n";
						resultText += "Result toStr:    "
								+ oresult.result.toString() + "\n";
						result.setText(resultText);
						return;
					} else if (oresult.forbiddenMethod == true) {
						result.setText("this method is forbidden to be invoked!");
						return;
					} else if (oresult.methodOrFieldFound == false) {
						result.setText("method can not be found!");
						return;
					} else if (oresult.ex != null) {
						result.setText("Exception Throwed:\n"
								+ ExceptionUtility.getErrorMessage(oresult.ex));
						return;
					}
				}
			}
		});
		runMethodbutton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// Tree tree;
				m();
			}

			private void m() {
				result.setText("");
				Widget w = getWidgetToAnalysis();
				if (w == null) {
					result.setText("Nothing to Analysis");
					return;
				}
				String text = method.getText().trim();
				if (text.length() > 0) {
					ObjectResult oresult = ReflectionUtil.invokeMethod(text, w);
					if (oresult.result != null) {
						String resultText = "------------Method Run In UI--------------------\n";
						resultText += "Return Type:     "
								+ oresult.result.getClass().getName() + "\n";
						resultText += "Result toStr:    "
								+ oresult.result.toString() + "\n";
						result.setText(resultText);
						return;
					} else if (oresult.forbiddenMethod == true) {
						result.setText("this method is forbidden to be invoked!");
						return;
					} else if (oresult.methodOrFieldFound == false) {
						result.setText("method can not be found!");
						return;
					} else if (oresult.ex != null) {
						result.setText("Exception Throwed:\n"
								+ oresult.ex.getMessage());
						return;
					}
				}
			}

		});
	}

	private Widget getWidgetToAnalysis() {

		Object data = this.tabFolder.getDisplay().getData(
				Constants.TABFOLDER_DATA_NAME);
		if (data != null && data instanceof Widget) {
			Widget w = (Widget) data;
			if (!w.isDisposed()) {
				return (Widget) data;
			}
		}
		return null;
	}

	private void createEventDebugger(Composite composite12) {
		composite12.setLayout(new GridLayout());
		composite12.setData(Utility.WidgetData_Exclude, "");
		final Button b = new Button(composite12, SWT.CHECK);
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b.setText("Listen");

		final Button c = new Button(composite12, SWT.PUSH);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c.setText("Clear");

		// final Button d = new Button(composite12, SWT.PUSH);
		// d.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// d.setText("ForDebug");
		final Text text = new Text(composite12, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		texts[1] = text;

		b.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				if (b.getSelection()) {
					Utility.addDisplayFilter(DisplayListener.getDefault());

				} else {
					Utility.removeDisplayFilter(DisplayListener.getDefault());

				}

			}
		});
		c.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				text.setText("");

			}
		});

	}

	private void createShellLogger(Composite composite11) {
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		composite11.setLayout(gl);

		final Button b = new Button(composite11, SWT.CHECK);
		b.setLayoutData(new GridData());
		b.setText("Listen");

		final Button c = new Button(composite11, SWT.PUSH);
		c.setLayoutData(new GridData());
		c.setText("Clear");

		shellInfoKTable = new SimpleKTable(composite11);
		shellInfoKTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));

		final SimpleKTableModel<ShellInfoModel> model = new SimpleKTableModel<ShellInfoModel>(
				ShellInfoModel.class);

		shellInfoKTable.setModel(model);
		shellInfoKTable.addMenuListener();
		b.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ShellListnerChecker.setCheck(b.getSelection());
			}
		});

		c.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				boolean oldValue = b.getSelection();
				try {
					ShellListnerChecker.setCheck(false);
					model.clearTableRow();
					shellInfoKTable.redraw();

				} catch (Exception ex) {

				} finally {
					ShellListnerChecker.setCheck(oldValue);
				}

			}
		});
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		if (shellActivatelistener != null)
			Display.getDefault().removeFilter(SWT.Dispose,
					shellActivatelistener);
		shellActivatelistener = null;
		Utility.removeDisplayFilter(DisplayListener.getDefault());
		texts[0] = null;
		texts[1] = null;
		if (this.composite != null) {
			this.perfTale.dispose();
			this.perspectiveTable.dispose();
			this.wizardTable.dispose();
			this.viewTable.dispose();
			this.actionViewTable.dispose();
		}

	}

	// following methods are called by update action in the local view bar
	public void updateWizardTable() {
		try {
			SimpleKTableModel model = (SimpleKTableModel) this.wizardTable
					.getModel();
			model.update(EclipseModelDataProvider.getDefault().getWizardList());
			this.wizardTable.redraw();
		} catch (Throwable e) {

		}

	}

	public void updatePrefTable() {
		try {
			SimpleKTableModel model = (SimpleKTableModel) this.perfTale
					.getModel();
			model.update(EclipseModelDataProvider.getDefault().getPrefList());
			this.perfTale.redraw();
		} catch (Throwable e) {

		}
	}

	public void updatePerspectiveTable() {
		try {
			SimpleKTableModel model = (SimpleKTableModel) this.perspectiveTable
					.getModel();
			model.update(EclipseModelDataProvider.getDefault()
					.getPerspectiveList());
			this.perspectiveTable.redraw();
		} catch (Throwable e) {

		}
	}

	public void updateViewTable() {
		try {
			SimpleKTableModel model = (SimpleKTableModel) this.viewTable
					.getModel();
			model.update(EclipseModelDataProvider.getDefault()
					.getViewModelList());
			this.viewTable.redraw();
		} catch (Throwable e) {

		}
	}

	public void updateEditorTable() {
		try {
			SimpleKTableModel model = (SimpleKTableModel) this.editorTable
					.getModel();
			model.update(EclipseModelDataProvider.getDefault()
					.getEditorModelList());
			this.editorTable.redraw();
		} catch (Throwable e) {

		}

	}

	public void updateToolbarTable() {
		try {
			SimpleKTableModel model = (SimpleKTableModel) this.toolBarTable
					.getModel();
			MainToolBarItemModel[] list = EclipseModelDataProvider.getDefault()
					.getToolBarModelList();

			model.update(list);
			this.toolBarTable.redraw();
		} catch (Throwable e) {

		}
	}

	public void updateMainMenuTable() {
		try {
			SimpleKTableModel model = (SimpleKTableModel) this.menuTable
					.getModel();
			MainMenuItemModel[] list = EclipseModelDataProvider.getDefault()
					.getMainMenuModelList();
			model.update(list);
			this.menuTable.redraw();
		} catch (Throwable e) {

		}
	}

	// end of methods are called by update action in the local view bar

	public void updateLocalToolBarTable(RowModel[] rows, String viewID) {
		try {
			if (rows != null && rows.length > 0) {

				this.viweIDLabelInViewActionTab.setText(viewID);
				SimpleKTableModel model = (SimpleKTableModel) this.actionViewTable
						.getModel();
				model.update(rows);
				this.actionViewTable.redraw();

			}
		} catch (Throwable e) {

		}
	}

	public void addToAnalysisControlTable(ControlAnalysisModel row) {

		if (row != null) {
			SimpleKTableModel<ControlAnalysisModel> model = (SimpleKTableModel<ControlAnalysisModel>) this.analysisControlTable
					.getModel();
			model.addKTableRow(row);
		}
	}

	public void clearAnalysisControlTable() {
		SimpleKTableModel<ControlAnalysisModel> model = (SimpleKTableModel<ControlAnalysisModel>) this.analysisControlTable
				.getModel();
		model.clearTableRow();
		this.analysisControlTable.redraw();
	}

	public void updateActivepartTable() {
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		Control rootControl = getPartControl(part);

		if (rootControl != null) {
			String partID = part.getSite().getId();
			String partType = "editor";
			String className;
			if (part instanceof IViewPart) {
				partType = "view";
				className = EclipseExtentionInfoProvider.getViewClass(partID)[1];
			} else {
				className = EclipseExtentionInfoProvider.getEditorClass(partID)[1];
			}

			rootControl.setData(Constants.rootControlIDName, partID);
			rootControl.setData(Constants.rootControlTypeName, partType);

			this.activepartType.setText(partType);
			this.activePartID.setText("id:   " + partID);
			this.activePartClassName.setText("class:" + className);

			try {
				SimpleKTableModel model = (SimpleKTableModel) this.activePartTable
						.getModel();
				WorkpartModel[] list = EclipseModelDataProvider.getDefault()
						.getWorkpartModelList();
				model.update(list);
				this.activePartTable.redraw();
				int focusRow = 0;
				SimpleKTableModel sm = (SimpleKTableModel) this.activePartTable
						.getModel();
				List<RowModel> mlist = sm.getRowModelList();
				for (int i = 0; i < mlist.size(); i++) {

					WorkpartModel wtmp = (WorkpartModel) mlist.get(i);
					if (wtmp.isFocus()) {
						focusRow = i + 1;
						break;
					}
				}
				if (focusRow != 0) {
					this.activePartTable.setSelection(1, focusRow, true);
				}
			} catch (Throwable e) {

			}

		}
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
}
