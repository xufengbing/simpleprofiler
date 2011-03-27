package rcpmail.model;

import java.util.ArrayList;
import java.util.List;

public class Model extends ModelObject {
	private static Model model;
	private List<Server> servers = new ArrayList<Server>();

	public void addServer(Server server) {
		servers.add(server);
		server.setParent(this);
		firePropertyChange("servers", null, null);
	}

	public void removeServer(Server child) {
		servers.remove(child);
		child.setParent(null);
		firePropertyChange("servers", null, null);
	}

	public  List<Server> getServers() {
		return servers;
	}

	public static Model getInstance() {

		if (model == null) {
			model = new Model();
		}
		return model;
	}
}
