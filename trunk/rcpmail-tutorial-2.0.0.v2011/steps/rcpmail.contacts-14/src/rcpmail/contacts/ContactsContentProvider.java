package rcpmail.contacts;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rcpmail.contacts.model.Contact;
import rcpmail.contacts.model.Contacts;
import rcpmail.model.Model;

public class ContactsContentProvider implements ITreeContentProvider {

	public ContactsContentProvider() {
		super();
		Contacts.getInstance();
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Contacts)
			return Contacts.getInstance().getContacts().toArray();
		throw new RuntimeException("getChildren() called on: " + parentElement);
	}

	public Object getParent(Object element) {
		if (element instanceof Contact)
			return Contacts.getInstance();
		if (element instanceof Contacts)
			return Model.getInstance();
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof Contacts)
			return getChildren(element).length > 0;
		if (element instanceof Contact)
			return false;
		throw new RuntimeException("hasChildren() called on: " + element);
	}

	public Object[] getElements(Object inputElement) {

		if (inputElement == Model.getInstance()) {
			return new Object[] { Contacts.getInstance() };
		}
		return null;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
