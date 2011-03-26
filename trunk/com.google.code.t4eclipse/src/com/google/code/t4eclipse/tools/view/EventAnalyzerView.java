package com.google.code.t4eclipse.tools.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;

import com.google.code.t4eclipse.tools.ktable.SimpleKTable;
import com.google.code.t4eclipse.tools.ktable.SimpleKTableModel;
import com.google.code.t4eclipse.tools.ktable.model.ControlAnalysisModel;

enum ListenScope {
	NONE, ALL, SELECTED
};

public class EventAnalyzerView extends ViewPart {
	private byte[] lock = new byte[0];

	public static final String T4ECLIPSE_LISTEN_EVNET = "T4ECLIPSE_LISTEN_EVNET";
	public static final String T4ECLIPSE_LISTEN_EVNET_NEVER_VALUE = "NEVER";
	public static final String T4ECLIPSE_LISTEN_EVNET_YES_VALUE = "YES";

	public static final int[] eventTypes = new int[] { SWT.Paint,
			SWT.Selection, SWT.DefaultSelection, SWT.Dispose, SWT.FocusIn,
			SWT.FocusOut, SWT.Hide, SWT.Show, SWT.KeyDown, SWT.KeyUp,
			SWT.MouseDown, SWT.MouseUp, SWT.MouseDoubleClick, SWT.MouseMove,
			SWT.Resize, SWT.Move, SWT.Close, SWT.Activate, SWT.Deactivate,
			SWT.Iconify, SWT.Deiconify, SWT.Expand, SWT.Collapse, SWT.Modify,
			SWT.Verify, SWT.Help, SWT.Arm, SWT.MouseExit, SWT.MouseEnter,
			SWT.MouseHover, SWT.Traverse };
	public static final String[] eventTypesStr = new String[] { "SWT.Paint",
			"SWT.Selection", "SWT.DefaultSelection", "SWT.Dispose",
			"SWT.FocusIn", "SWT.FocusOut", "SWT.Hide", "SWT.Show",
			"SWT.KeyDown", "SWT.KeyUp", "SWT.MouseDown", "SWT.MouseUp",
			"SWT.MouseDoubleClick", "SWT.MouseMove", "SWT.Resize", "SWT.Move",
			"SWT.Close", "SWT.Activate", "SWT.Deactivate", "SWT.Iconify",
			"SWT.Deiconify", "SWT.Expand", "SWT.Collapse", "SWT.Modify",
			"SWT.Verify", "SWT.Help", "SWT.Arm", "SWT.MouseExit",
			"SWT.MouseEnter", "SWT.MouseHover", "SWT.Traverse" };

	// event array will store SWT event that need to be listened
	// here we assume that all event value is >=0 && <=49, it is working on all
	// swt version now.
	private boolean[] selectedEventArray = new boolean[50];

	private volatile List<Control> controlList;
	private Map<Integer, Boolean> map;
	private Listener filter;
	private ListenScope scope = ListenScope.NONE;
	private SashForm sashForm;
	private Composite eventTableComposite;
	private Text eventLogText;

	public EventAnalyzerView() {
		this.controlList = new ArrayList<Control>();
		this.map = new HashMap<Integer, Boolean>();
		for (boolean b : this.selectedEventArray) {
			b = false;
		}
		this.filter = new Listener() {

			public void handleEvent(Event event) {

				// check scope, if none return
				ListenScope lscope = EventAnalyzerView.this.scope;
				if (lscope.equals(ListenScope.NONE)) {
					return;
				}

				// else check event, if not registered, return
				int eventType = event.type;
				boolean eventSelected = EventAnalyzerView.this.selectedEventArray[eventType];
				if (!eventSelected) {
					return;
				}

				// check scope, if selected widgets. check widgets.

				if (lscope.equals(ListenScope.SELECTED)) {

					// TODO: analyze widget to related controls
					Widget w = event.widget;
					boolean inSelected = false;
					for (Control c : EventAnalyzerView.this.controlList) {
						if (w == c) {
							inSelected = true;
							break;
						}
					}
					if (inSelected == false) {
						return;
					}
				}

				// if not return until now, we need to log this event
				EventAnalyzerView.this.logEvent(event);

			}
		};

		addListener();
	}

	@Override
	public void createPartControl(Composite root) {
		root.setLayout(new FillLayout());
		sashForm = new SashForm(root, SWT.HORIZONTAL);

		eventTableComposite = new Composite(sashForm, SWT.NONE);

		Composite typesCompositeParent = new Composite(sashForm, SWT.NONE);
		typesCompositeParent.setLayout(new FillLayout());

		ScrolledComposite sc = new ScrolledComposite(typesCompositeParent,
				SWT.V_SCROLL | SWT.H_SCROLL);

		Composite typesComposite = new Composite(sc, SWT.NONE);
		typesComposite.setLayout(new RowLayout(SWT.VERTICAL));

		for (int i = 0; i < EventAnalyzerView.eventTypes.length; i++) {
			final Button eventButton = new Button(typesComposite, SWT.CHECK);
			eventButton.setText(EventAnalyzerView.eventTypesStr[i]);
			final int eventValue = eventTypes[i];
			eventButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					boolean selected = eventButton.getSelection();
					EventAnalyzerView.this.selectedEventArray[eventValue] = selected;
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					// do nothing
				}
			});
		}

		// use parent
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 1;
		eventTableComposite.setLayout(layout);
		eventTableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTable(eventTableComposite);

		sashForm.setWeights(new int[] { 7, 3 });

		sc.setContent(typesComposite);
		typesComposite.setSize(300, 1000);

		// Set the minimum size
		sc.setMinSize(300, 1000);

		// Expand both horizontally and vertically
		sc.setExpandVertical(true);
		sc.setExpandVertical(true);
	}

	private void createTable(Composite composite) {
		GridLayout gridLayout = new GridLayout(2, false);

		composite.setLayout(gridLayout);

		// controls group
		Group controls = new Group(composite, SWT.None);
		controls.setText("Controls");
		GridData controlGroupgridData = new GridData(GridData.FILL_VERTICAL);
		controlGroupgridData.widthHint = 300;

		controls.setLayoutData(controlGroupgridData);

		GridLayout conrolGroupgridLayout = new GridLayout();
		controls.setLayout(conrolGroupgridLayout);
		// TODO: use ktable that has three column 1.discription 2. short
		// className, 3. listen (checkbox)
		// check box related to getData("T4ECLIPSE_LISTEN_EVNET") 's value;

		SimpleKTable analysisControlTable = new SimpleKTable(controls);
		GridData d2 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		analysisControlTable.setLayoutData(d2);
		SimpleKTableModel<ControlAnalysisModel> model = new SimpleKTableModel<ControlAnalysisModel>(
				ControlAnalysisModel.class);
		analysisControlTable.setModel(model);

		Group analysis = new Group(composite, SWT.None);
		analysis.setText("Events");
		GridData analysisGroupgridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		analysis.setLayoutData(analysisGroupgridData);

		analysis.setLayout(new GridLayout(1, false));

		final Button clearAllButton = new Button(analysis, SWT.PUSH);
		clearAllButton.setText("Clear All Event Logs");
		GridData rumMethodData = new GridData();
		rumMethodData.horizontalAlignment = GridData.FILL;
		clearAllButton.setLayoutData(rumMethodData);
		clearAllButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronized (EventAnalyzerView.this.lock) {
					EventAnalyzerView.this.eventLogText.setText("");
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Group eventSourceGroup = new Group(analysis, SWT.None);
		eventSourceGroup.setText("listening scope");
		GridData eventSourceGroupGrid = new GridData();
		rumMethodData.horizontalAlignment = GridData.FILL;
		eventSourceGroup.setLayoutData(eventSourceGroupGrid);

		GridLayout eventSourceGroupGridLayout = new GridLayout(3, true);
		eventSourceGroup.setLayout(eventSourceGroupGridLayout);

		Button b_all = new Button(eventSourceGroup, SWT.RADIO);
		b_all.setText("all in workbench");
		Button b_selected = new Button(eventSourceGroup, SWT.RADIO);
		b_selected.setText("selected control");
		Button b_none = new Button(eventSourceGroup, SWT.RADIO);
		b_none.setText("none");
		b_all.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronized (EventAnalyzerView.this.lock) {

					EventAnalyzerView.this.scope = ListenScope.ALL;
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		b_none.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronized (EventAnalyzerView.this.lock) {

					EventAnalyzerView.this.scope = ListenScope.NONE;
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		b_selected.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronized (EventAnalyzerView.this.lock) {

					EventAnalyzerView.this.scope = ListenScope.SELECTED;
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		eventLogText = new Text(analysis, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		GridData textData = new GridData(GridData.FILL_VERTICAL
				| GridData.FILL_HORIZONTAL);
		textData.horizontalSpan = 6;
		eventLogText.setLayoutData(textData);

		unListenThis(composite);
	}

	private void unListenThis(Composite composite) {
		composite.setData(T4ECLIPSE_LISTEN_EVNET, false);
		// TODO: set all tree to false
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void dispose() {
		// remove the listener.
		removeListener();
		// set all control's T4ECLIPSE_LISTEN_EVNET to false

		super.dispose();
	}

	private void removeListener() {
		int[] eventTypes = getAllEventType();
		for (int eventType : eventTypes) {
			Display.getDefault().removeFilter(eventType, this.filter);
		}
	}

	private int[] getAllEventType() {
		return eventTypes;
	}

	private void addListener() {
		int[] eventTypes = getAllEventType();
		for (int eventType : eventTypes) {
			Display.getDefault().addFilter(eventType, this.filter);
		}

	}

	protected void logEvent(Event event) {
		synchronized (this.lock) {
			String currentText = this.eventLogText.getText();
			this.eventLogText.setText(currentText + "\n"
					+ getEventContent(event));
		}
	}

	private String getEventContent(Event event) {

		return getEevntTypeStr(event.type) + " "
				+ event.widget.getClass().getSimpleName();
	}

	private String getEevntTypeStr(int type) {
		for (int i = 0; i < eventTypes.length; i++) {
			if (eventTypes[i] == type) {
				return eventTypesStr[i];
			}
		}
		return "";

	}

}
