package rcpmail;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rcpmail.model.Folder;
import rcpmail.model.Model;
import rcpmail.model.Server;

public class ServerTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object element) {
		if(element instanceof IProject){
			System.err.println("FG: I don't want this to happen...");
			return Model.getInstance().getServers().toArray();
		} else if (element instanceof Model) {
			return ((Model)element).getServers().toArray();
		} else if (element instanceof Server) {
			return ((Server) element).getFolders().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if(element instanceof Server ) {
			return Model.getInstance();
		} else if (element instanceof Folder) {
			((Folder) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof Model ) {
			return ! Model.getInstance().getServers().isEmpty();
		} else if (element instanceof Server) {
			return ! ((Server) element).getFolders().isEmpty();
		}
		return false;
	}

	@Override
	public Object[] getElements(Object element) {
		if(element instanceof Model ) {
			return Model.getInstance().getServers().toArray();
		} else if (element instanceof Server) {
			((Server) element).getFolders().toArray();
		}
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
	}
}
