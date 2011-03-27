package rcpmail;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.part.ViewPart;

import rcpmail.model.Folder;
import rcpmail.model.Message;

public class MessageTableView extends ViewPart implements ISelectionListener {

	private Table table;
	private TableViewer tableViewer;
	public static final String ID = "rcpmail.MessageTableView"; //$NON-NLS-1$

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		tableViewer = new TableViewer(container, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(51);
		newColumnTableColumn_3.setText("Spam");

		final TableColumn newColumnTableColumn = new TableColumn(table,
				SWT.NONE);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("Subject");

		final TableColumn newColumnTableColumn_1 = new TableColumn(table,
				SWT.NONE);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("From");

		final TableColumn newColumnTableColumn_2 = new TableColumn(table,
				SWT.NONE);
		newColumnTableColumn_2.setWidth(100);
		newColumnTableColumn_2.setText("Date");
		//
		createActions();
		initializeToolBar();
		initializeMenu();

		// Create a standard content provider
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(contentProvider);

		// And a standard label provider that maps columns
		IObservableSet knownElements = contentProvider.getKnownElements();
		IObservableMap[] attributeMaps = BeansObservables.observeMaps(
				knownElements, Message.class, new String[] { "spam", "subject", "from",
						"date" });
		tableViewer.setLabelProvider(new ObservableMapLabelProvider(
				attributeMaps));

		IWorkbenchWindow workbenchWindow = getSite().getWorkbenchWindow();
		ISelectionService selectionService = workbenchWindow.getSelectionService();
		selectionService.addSelectionListener(this);	
		getSite().setSelectionProvider(tableViewer);
		
	}

	/**
	 * Create the actions
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar
	 */
	private void initializeToolBar() {
		getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu() {
		getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof NavigationView || part instanceof CommonNavigator) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object folder = treeSelection.getFirstElement();
			if(folder instanceof Folder) {
				tableViewer.setInput(((Folder) folder).getMessages());
				tableViewer.refresh(true);
			}
		}
	}
}

