package rcpmail;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import rcpmail.model.Folder;
import rcpmail.model.Server;

public class ServerLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(Object element) {
		if(element instanceof Server)
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		if(element instanceof Folder)
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		return null;
	}

	@Override
	public String getText(Object element) {
		if(element instanceof Folder)
			return ((Folder)element).getName();
		if(element instanceof Server)
			return ((Server)element).getHostname();
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
