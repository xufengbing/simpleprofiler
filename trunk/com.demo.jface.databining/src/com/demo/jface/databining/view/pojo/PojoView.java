package com.demo.jface.databining.view.pojo;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class PojoView extends ViewPart {

	private PersonPojo person;
	private Text firstName;
	private Button married;

	public PojoView() {
		person = new PersonPojo();
		person.setFirstName("Joe");
		person.setLastName("Jim");
	}

	@Override
	public void createPartControl(Composite parent) {
		Layout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		Label label = new Label(parent, SWT.NONE);
		label.setText("FirstName");
		firstName = new Text(parent, SWT.BORDER);
		firstName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		Label marriedLabel = new Label(parent, SWT.NONE);
		marriedLabel.setText("Maried");
		married = new Button(parent, SWT.CHECK);
		bindValues();

		Button button1 = new Button(parent, SWT.PUSH);
		button1.setText("Write model");
		button1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(person.getFirstName());
				System.out.println(person.isMarried());
			}
		});

	}

	private void bindValues() {
		DataBindingContext bindingContext = new DataBindingContext();
		IObservableValue myModel = PojoProperties.value(PersonPojo.class,
				"firstName").observe(person);
		IObservableValue myWidget = WidgetProperties.text(SWT.Modify).observe(
				firstName);
		bindingContext.bindValue(myWidget, myModel);
		myModel = PojoProperties.value(PersonPojo.class, "married").observe(
				person);
		myWidget = WidgetProperties.selection().observe(married);
		bindingContext.bindValue(myWidget, myModel);
	}

	@Override
	public void setFocus() {

	}

}
