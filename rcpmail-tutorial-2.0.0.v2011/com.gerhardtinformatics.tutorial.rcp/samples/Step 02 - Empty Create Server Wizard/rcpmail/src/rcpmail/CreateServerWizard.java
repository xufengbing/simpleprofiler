package rcpmail;

import org.eclipse.jface.wizard.Wizard;

import rcpmail.model.Model;
import rcpmail.model.Server;

class CreateServerWizard extends Wizard {

	Server server = new Server();

	public void addPages() {
		addPage(new CreateServerPage(server));
	}

	public String getWindowTitle() {
		return "Create Mail Server";
	}

	public boolean performFinish() {
		Model.getInstance().addServer(server);
		return true;
	}

}