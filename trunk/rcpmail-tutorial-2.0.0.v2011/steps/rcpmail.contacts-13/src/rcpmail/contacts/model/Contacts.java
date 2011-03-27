package rcpmail.contacts.model;

import java.util.ArrayList;
import java.util.List;

public class Contacts {

	private static Contacts instance;

	private List<Contact> contacts;

	public static Contacts getInstance() {
		if (instance == null) {
			instance = new Contacts();
		}
		return instance;
	}

	private Contacts() {
		contacts = new ArrayList<Contact>();
		addContact(new Contact("Fred Flintstone", "+1 415 391 9911",
				"fred@flintstone.com"));
		addContact(new Contact("Barney Rubble", "+1 408 725 8111",
				"barney@rubble.com"));
	}

	public void addContact(Contact c) {
		contacts.add(c);
	}

	public List<Contact> getContacts() {
		return contacts;
	}

}
