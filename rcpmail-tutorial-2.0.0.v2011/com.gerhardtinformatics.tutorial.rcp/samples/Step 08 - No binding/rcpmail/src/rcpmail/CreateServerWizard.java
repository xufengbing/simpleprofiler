package rcpmail;

import org.eclipse.jface.wizard.Wizard;

import rcpmail.model.Model;
import rcpmail.model.Server;

class CreateServerWizard extends Wizard {
	private final Server server = new Server();
	private CreateServerPage createServerPage;
	public CreateServerWizard() {
		// set some defaults
		server.setHostname("localhost");
		server.setPort(23);
	}
	public void addPages() {
		addPage(createServerPage=new CreateServerPage(server));
	}

	public String getWindowTitle() {
		return "Create Mail Server";
	}

	public boolean performFinish() {
		Model.getInstance().addServer(server);
		createServerPage.updateServer();
		System.out.println(server);
		return true;
	}

}