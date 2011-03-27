package rcpmail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;

import rcpmail.model.Folder;
import rcpmail.model.Message;

public class MessageTableView extends ViewPart implements ISelectionListener {

	private Table table;
	private TableViewer tableViewer;
	private Folder folder;
	private PropertyChangeListener propertyChangeListener;
	public static final String ID = "rcpmail.MessageTableView"; //$NON-NLS-1$
	
	class MessageLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			Message message=(Message) element;
			switch (columnIndex) {
			case 0:
				return message.isSpam()+"";
			case 1:
				return message.getSubject();
			case 2:
				return message.getFrom();
			case 3:
				return message.getDate();

			default:
				return null;
			}
		}	
	}

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

		tableViewer.setLabelProvider(new MessageLabelProvider());
		tableViewer.setContentProvider(new IStructuredContentProvider(){
			public void dispose() {			
			}
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			public Object[] getElements(Object inputElement) {
				if(folder==null)
					return new Object[0];
				return folder.getMessages().toArray();
			}});
		IWorkbenchWindow workbenchWindow = getSite().getWorkbenchWindow();
		ISelectionService selectionService = workbenchWindow.getSelectionService();
		selectionService.addSelectionListener(this);	
		getSite().setSelectionProvider(tableViewer);
		propertyChangeListener=new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if(folder!=null)
					tableViewer.refresh(true);
			}
			
		};
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
	void setFolder(Folder folder) {
		if(folder!=null) {
			for (Message message : folder.getMessages()) {
				message.removePropertyChangeListener(propertyChangeListener);
			}
		}
		this.folder=folder;
		if(folder!=null) {
			for (Message message : folder.getMessages()) {
				message.addPropertyChangeListener(propertyChangeListener);
			}
		}
		tableViewer.setInput(folder);
	}
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof NavigationView) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object sel = treeSelection.getFirstElement();
			if(sel instanceof Folder) {
				Folder folder=(Folder) sel;
				setFolder(folder);
			}
		}
	}

	@Override
	public void dispose() {
		if(folder!=null) {
			for (Message message : folder.getMessages()) {
				message.removePropertyChangeListener(propertyChangeListener);
			}
		}
		super.dispose();
	}

}

