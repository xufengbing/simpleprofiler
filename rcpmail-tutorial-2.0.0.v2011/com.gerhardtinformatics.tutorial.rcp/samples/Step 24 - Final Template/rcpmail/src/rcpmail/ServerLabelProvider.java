package rcpmail;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcpmail.model.Folder;
import rcpmail.model.Server;

public class ServerLabelProvider implements ILabelProvider {

	private Image createImage(String fileName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"icons/silk/" + fileName).createImage();
	}

	private final Image serverImage;
	private final Image folderImage;
	private final Image junkFolderImage;
	private final Image draftsFolderImage;
	private final Image sentFolderImage;
	private final Image trashFolderImage;

	public ServerLabelProvider() {
		serverImage = createImage("server.png");
		folderImage = createImage("folder.png");
		junkFolderImage = createImage("folder_bug.png");
		draftsFolderImage = createImage("folder_edit.png");
		sentFolderImage = createImage("folder_go.png");
		trashFolderImage = createImage("folder_delete.png");
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Server) {
			return serverImage;
		} else if (element instanceof Folder) {
			// Kai: Hack to get different images for different folders
			Folder folder = (Folder) element;
			if ("junk".equalsIgnoreCase(folder.getName())) {
				return junkFolderImage;
			} else if ("drafts".equalsIgnoreCase(folder.getName())) {
				return draftsFolderImage;
			} else if ("sent".equalsIgnoreCase(folder.getName())) {
				return sentFolderImage;
			} else if ("trash".equalsIgnoreCase(folder.getName())) {
				return trashFolderImage;
			}
			return folderImage;
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Folder) {
			return ((Folder) element).getName();
		} else if (element instanceof Server) {
			return ((Server) element).getHostname();
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// Kai: Probably we should put the images in a registry, then we don't
		// need to dispose them
		serverImage.dispose();
		folderImage.dispose();
		junkFolderImage.dispose();
		draftsFolderImage.dispose();
		sentFolderImage.dispose();
		trashFolderImage.dispose();
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
