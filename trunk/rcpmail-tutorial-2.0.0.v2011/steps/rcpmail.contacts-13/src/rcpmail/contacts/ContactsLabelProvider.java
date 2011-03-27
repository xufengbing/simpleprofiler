package rcpmail.contacts;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcpmail.contacts.model.Contact;
import rcpmail.contacts.model.Contacts;

public class ContactsLabelProvider extends LabelProvider {

	private LocalResourceManager resourceManager = new LocalResourceManager(
			JFaceResources.getResources());
	
	private ImageDescriptor contactsImage;
	private ImageDescriptor contactImage;

	public ContactsLabelProvider() {
		initializeImageDescriptors();
	}

	private void initializeImageDescriptors() {
		contactsImage = getDescriptor("folder_user.png");
		contactImage = getDescriptor("user.png");
	}
	
	private ImageDescriptor getDescriptor(String fileName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Constants.PLUGIN_ID,
				"icons/silk/" + fileName);
	}

	public Image getImage(Object element) {
		if (element instanceof Contacts) {
			return (Image) resourceManager.get(contactsImage);
		} else if (element instanceof Contact) {
			return (Image) resourceManager.get(contactImage);
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof Contacts) {
			return "Contacts";
		} else if (element instanceof Contact) {
			return ((Contact) element).toString();
		}
		return null;
	}

	public void dispose() {
		super.dispose();
		resourceManager.dispose();
	}

}
