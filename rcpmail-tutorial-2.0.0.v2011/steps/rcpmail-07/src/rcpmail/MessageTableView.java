package rcpmail;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;

public class MessageTableView extends ViewPart {

	private Table table;
	private TableViewer tableViewer;
	private LocalResourceManager resourceManager = new LocalResourceManager(
			JFaceResources.getResources());
	private Color gray;
	private Font italics;

	public static final String ID = "rcpmail.MessageTableView";

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		tableViewer = new TableViewer(container, SWT.FULL_SELECTION
				| SWT.MULTI | SWT.BORDER) {
			// The following is a workaround for bug 269264.
			public void remove(Object[] elements) {
				int oldIndex = -1;
				Table table = tableViewer.getTable();
				int[] selectionIndices = table.getSelectionIndices();
				if (selectionIndices.length > 0) {
					oldIndex = selectionIndices[0];
				}
				super.remove(elements);
				if (oldIndex != -1) {
					if (table.getItemCount() > oldIndex) {
						table.setSelection(oldIndex);
					} else if (table.getItemCount() > 0) {
						table.setSelection(table.getItemCount() - 1);
					}
				}
				setSelection(getSelection(), false);
			}
		};
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn columnFrom = new TableViewerColumn(tableViewer, SWT.NONE);
		columnFrom.getColumn().setWidth(95);
		columnFrom.getColumn().setText("From");
		
		TableViewerColumn columnSubject = new TableViewerColumn(tableViewer, SWT.NONE);
		columnSubject.getColumn().setWidth(300);
		columnSubject.getColumn().setText("Subject");
		
		TableViewerColumn columnDate = new TableViewerColumn(tableViewer, SWT.NONE);
		columnDate.getColumn().setWidth(85);
		columnDate.getColumn().setText("Date");
		
		createActions();
		initializeToolBar();
		initializeMenu();
		initializeContextMenu();

		gray = resourceManager.createColor(new RGB(100, 100, 100));
		italics = resourceManager.createFont(FontDescriptor.createFrom(
				JFaceResources.getDialogFont()).setStyle(SWT.ITALIC));

		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(contentProvider);
		
		tableViewer.setInput(null);
		
		getSite().setSelectionProvider(tableViewer);

		IContextService contextService = (IContextService) getSite()
				.getService(IContextService.class);
		contextService.activateContext("rcpmail.context.messages");
	}

	private void initializeContextMenu() {
		MenuManager manager = new MenuManager();
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(new GroupMarker(
						IWorkbenchActionConstants.MB_ADDITIONS));
			}
		});
		Menu contextMenu = manager.createContextMenu(table);
		table.setMenu(contextMenu);
		getSite().registerContextMenu(manager, tableViewer);
	}

	private void createActions() {
	}

	private void initializeToolBar() {
		getViewSite().getActionBars().getToolBarManager();
	}

	private void initializeMenu() {
		getViewSite().getActionBars().getMenuManager();
	}

	// it is important to implement setFocus()!
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	public void dispose() {
		super.dispose();
		resourceManager.dispose();
	}
}
