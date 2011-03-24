package com.demo.jface.databining.view;

import org.eclipse.core.databinding.*;
import org.eclipse.core.databinding.beans.*;
import org.eclipse.core.databinding.observable.value.*;
import org.eclipse.core.databinding.validation.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.internal.databinding.provisional.fieldassist.ControlDecorationSupport;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.ui.part.ViewPart;


public class DatabiningView extends ViewPart {
	public static final String ID = "de.vogella.databinding.person.swt.View";
	private Person person;

	private Text firstName;
	private Text ageText;
	private Button marriedButton;
	private Combo genderCombo;
	private Text countryText;
	public DatabiningView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		person = new Person();
		Address address = new Address();
		address.setCountry("Deutschland");
		person.setAddress(address);
		person.setFirstName("John");
		person.setLastName("Doo");
		person.setGender("Male");
		person.setAge(12);
		person.setMarried(true);
		// Lets put thing to order
		Layout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		Label firstLabel = new Label(parent, SWT.NONE);
		firstLabel.setText("Firstname: ");
		firstName = new Text(parent, SWT.BORDER);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		firstName.setLayoutData(gridData);

		Label ageLabel = new Label(parent, SWT.NONE);
		ageLabel.setText("Age: ");
		ageText = new Text(parent, SWT.BORDER);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		ageText.setLayoutData(gridData);

		Label marriedLabel = new Label(parent, SWT.NONE);
		marriedLabel.setText("Married: ");
		marriedButton = new Button(parent, SWT.CHECK);

		Label genderLabel = new Label(parent, SWT.NONE);
		genderLabel.setText("Gender: ");
		genderCombo = new Combo(parent, SWT.NONE);
		genderCombo.add("Male");
		genderCombo.add("Female");

		Label countryLabel = new Label(parent, SWT.NONE);
		countryLabel.setText("Country");
		countryText = new Text(parent, SWT.BORDER);

		Button button1 = new Button(parent, SWT.PUSH);
		button1.setText("Write model");
		button1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Firstname: " + person.getFirstName());
				System.out.println("Age " + person.getAge());
				System.out.println("Married: " + person.isMarried());
				System.out.println("Gender: " + person.getGender());
				System.out.println("Country: "
						+ person.getAddress().getCountry());
			}
		});

		Button button2 = new Button(parent, SWT.PUSH);
		button2.setText("Change model");
		button2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				person.setFirstName("Lars");
				person.setAge(person.getAge() + 1);
				person.setMarried(!person.isMarried());
				if (person.getGender().equals("Male")) {

				} else {
					person.setGender("Male");
				}
				if (person.getAddress().getCountry().equals("Deutschland")) {
					person.getAddress().setCountry("USA");
				} else {
					person.getAddress().setCountry("Deutschland");
				}
			}
		});

		// Now lets do the binding
		bindValues();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	private void bindValues() {
		// The DataBindingContext object will manage the databindings
		// Lets bind it
		DataBindingContext bindingContext = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify)
				.observe(firstName);
		IObservableValue modelValue = BeanProperties.value(Person.class,
				"firstName").observe(person);
		bindingContext.bindValue(widgetValue, modelValue);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(ageText);
		IObservableValue	modelValue1 = BeanProperties.value(Person.class, "age").observe(person);
		// We want that age is a number
		IValidator numberValidator = new IValidator() {

			@Override
			public IStatus validate(Object value) {
				String s = String.valueOf(value);
				boolean matches = s.matches("\\d*");
				if (matches) {
					return ValidationStatus.ok();
				}
				return ValidationStatus.error("Only Number permitted");
			}
		};
		UpdateValueStrategy targetToModel = new UpdateValueStrategy();
		targetToModel.setAfterConvertValidator(numberValidator);
		org.eclipse.core.databinding.Binding bindValue = bindingContext.bindValue(widgetValue, modelValue1, targetToModel, null);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);
		
		
		widgetValue = WidgetProperties.selection().observe(marriedButton);
		modelValue = BeanProperties.value(Person.class, "married").observe(
				person);
		bindingContext.bindValue(widgetValue, modelValue);

		widgetValue = WidgetProperties.selection().observe(genderCombo);
		modelValue = BeansObservables.observeValue(person, "gender");

		bindingContext.bindValue(widgetValue, modelValue);

		// Address field is bound to the Ui
		widgetValue = WidgetProperties.text(SWT.Modify).observe(countryText);

		modelValue = BeanProperties.value(Person.class, "address.country")
				.observe(person);
		bindingContext.bindValue(widgetValue, modelValue);

	
	}
}
