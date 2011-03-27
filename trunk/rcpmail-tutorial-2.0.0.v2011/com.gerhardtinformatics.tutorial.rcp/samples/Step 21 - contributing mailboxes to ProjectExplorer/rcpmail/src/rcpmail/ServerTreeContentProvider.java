package rcpmail;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.internal.databinding.observable.tree.IUnorderedTreeProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.internal.databinding.provisional.viewers.UnorderedTreeContentProvider;

import rcpmail.model.Model;
import rcpmail.model.Server;

public class ServerTreeContentProvider extends UnorderedTreeContentProvider {

	public ServerTreeContentProvider() {
		super(new ServerTreeProvider(), "pending...", false);
	}
}

class ServerTreeProvider implements IUnorderedTreeProvider {

	@Override
	public IObservableSet createChildSet(Object element) {
		if(element instanceof IProject){
			return Model.getInstance().getServers();
		} else if (element instanceof Model) {
			return ((Model)element).getServers();
		} else if (element instanceof Server) {
			return ((Server) element).getFolders();
		}
		return null;
	}

	@Override
	public Realm getRealm() {
		return null;
	}
	
}
