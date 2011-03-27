package rcpmail.model;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class Model extends ModelObject implements IAdaptable{
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

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == Model.class) return this;
		if (adapter == IWorkbenchAdapter.class)
			return ResourcesPlugin.getWorkspace().getRoot().getAdapter(adapter);
		return null;
	}
}
