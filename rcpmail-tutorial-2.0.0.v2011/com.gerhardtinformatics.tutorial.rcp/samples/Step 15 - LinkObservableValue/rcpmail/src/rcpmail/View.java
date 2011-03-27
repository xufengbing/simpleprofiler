package rcpmail;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import rcpmail.model.Message;

public class View extends ViewPart implements ISelectionListener {

	public static final String ID = "rcpmail.view";
	private Label subjectLabel;
	private Link link;
	private Text bodyText;
	private DataBindingContext dbc;

	public void createPartControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		top.setLayout(layout);
		// top banner
		Composite banner = new Composite(top, SWT.NONE);
		banner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 3;
		banner.setLayout(layout);

		// setup bold font
		Font boldFont = JFaceResources.getFontRegistry().getBold(
				JFaceResources.DEFAULT_FONT);

		Label l = new Label(banner, SWT.WRAP);
		l.setText("Subject:");
		l.setFont(boldFont);
		subjectLabel = new Label(banner, SWT.WRAP);
		subjectLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final Button spamButton = new Button(banner, SWT.CHECK);
		spamButton.setLayoutData(new GridData());
		spamButton.setText("Spam");

		l = new Label(banner, SWT.WRAP);
		l.setText("From:");
		l.setFont(boldFont);

		link = new Link(banner, SWT.NONE);
		final GridData gd_link = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1);
		gd_link.widthHint = 423;
		link.setLayoutData(gd_link);
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageDialog
						.openInformation(getSite().getShell(),
								"Not Implemented",
								"Imagine the address book or a new message being created now.");
			}
		});

		l = new Label(banner, SWT.WRAP);
		l.setText("Date:");
		l.setFont(boldFont);
		Label date = new Label(banner, SWT.WRAP);
		date.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		// message contents
		bodyText = new Text(top, SWT.MULTI | SWT.WRAP);
		bodyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		ISelectionService selectionService = getSite().getPage()
				.getWorkbenchWindow().getSelectionService();
		selectionService.addSelectionListener(this);

		dbc = new DataBindingContext();
		Realm realm = SWTObservables.getRealm(parent.getShell().getDisplay());

		IObservableValue subjectObservable = BeansObservables
				.observeDetailValue(realm, messageValue, "subject",
						String.class);
		ISWTObservableValue subjectLabelObservable = SWTObservables
				.observeText(subjectLabel);
		dbc.bindValue(subjectLabelObservable, subjectObservable, null, null);

		dbc.bindValue(
				SWTObservables.observeText(bodyText, SWT.Modify),
				BeansObservables.observeDetailValue(realm, messageValue,"body", String.class), 
				null, 
				null);
		dbc.bindValue(
				SWTObservables.observeSelection(spamButton),
				BeansObservables.observeDetailValue(realm, messageValue,"spam", boolean.class), 
				null, 
				null);
		dbc.bindValue(
				SWTObservables.observeText(date),
				BeansObservables.observeDetailValue(realm, messageValue,"date", null), 
				null, 
				null);
		dbc.bindValue(
				new LinkObservableValue(link),
				BeansObservables.observeDetailValue(realm, messageValue,"from", null), 
				null, 
				null);
	}

	public void setFocus() {
	}

	private WritableValue messageValue = new WritableValue();

	void setMessage(Message message) {
		messageValue.setValue(message);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof MessageTableView
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection treeSelection = (IStructuredSelection) selection;
			Object message = treeSelection.getFirstElement();
			if (message instanceof Message) {
				setMessage((Message) message);
			}
		}
	}
	@Override
	public void dispose() {
		ISelectionService selectionService = getSite().getPage().getWorkbenchWindow().getSelectionService();
		selectionService.removeSelectionListener(this);
		dbc.dispose();
		super.dispose();
	}
}
