package rcpmail.model;

import org.eclipse.core.databinding.observable.set.WritableSet;

public class Model extends ModelObject {
	private static Model model;
	private WritableSet servers = new WritableSet();

	public void addServer(Server server) {
		servers.add(server);
		server.setParent(this);
		


	}

	public void removeServer(Server child) {
		servers.remove(child);
		child.setParent(null);
	}

	public WritableSet getServers() {
		return servers;
	}

	public static Model getInstance() {

		if (model == null) {
			model = new Model();
		}
		return model;
	}
}
