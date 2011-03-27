package rcpmail.persistent;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import rcpmail.Controller;
import rcpmail.model.Folder;
import rcpmail.model.Message;
import rcpmail.model.Model;
import rcpmail.model.Server;

public class PersistentModelManager extends Controller {

	private boolean _reading;

	public PersistentModelManager() {
		super();
		
		Model.resetInstance();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource[] members;
		_reading = true;
		try {
			members = root.members();
			for (int i = 0; i < members.length; i++) {
				Server server = readServer((IProject) members[i]);
				Model.getInstance().addServer(server);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		_reading = false;
	}

	//
	// Create and delete resources for model objects
	//

	public void addServer(Server server) {
		super.addServer(server);
		if (_reading)
			return;
		IProject serverProj = getServerProject(server);
		QualifiedName qname;
		try {
			if (!serverProj.exists())
				serverProj.create(null);
			serverProj.open(null);
			qname = new QualifiedName(Activator.PLUGIN_ID, "User");
			serverProj.setPersistentProperty(qname, server.getUsername());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void removeServer(Server server) {
		super.removeServer(server);
		IProject serverProj = getServerProject(server);
		try {
			serverProj.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addFolder(Server server, Folder folder) {
		if (_reading)
			return;
		try {
			getFolderFolder(folder).create(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeFolder(Folder folder) {
		try {
			getFolderFolder(folder).delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	// FIXME - this needs to be persisted across sessions
	private static int _messageId;

	@Override
	public void addMessage(Folder folder, Message message) {
		if (_reading)
			return;
		message.setId(++_messageId);
		IFile messageFile = getMessageFile(message);
		try {
			// FIXME - do this with a non deprecated thing
			InputStream is = new StringBufferInputStream(message.getBody());
			messageFile.create(is, true, null);
			QualifiedName qname;
			qname = new QualifiedName(Activator.PLUGIN_ID, "Subject");
			messageFile.setPersistentProperty(qname, message.getSubject());
			qname = new QualifiedName(Activator.PLUGIN_ID, "From");
			messageFile.setPersistentProperty(qname, message.getFrom());
			qname = new QualifiedName(Activator.PLUGIN_ID, "Date");
			messageFile.setPersistentProperty(qname, message.getDate());
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeMessage(Message message) {
		try {
			getMessageFile(message).delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	//
	// Read collections of model objects from resources
	//

	@Override
	public void readFolders(Server server) {
		_reading = true;
		IProject serverProj = getServerProject(server);
		IResource members[];
		try {
			members = serverProj.members();
			// Newly created project, create the default folders
			if (members.length == 1) {
				super.readFolders(server);
				return;
			}
			for (int i = 0; i < members.length; i++) {
				if (!(members[i] instanceof IFolder))
					continue;
				server.addFolder(readFolder((IFolder) members[i]));
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		_reading = false;
	}

	@Override
	public void readMessages(Folder folder) {
		_reading = true;
		IFolder folderResource = getFolderFolder(folder);
		IResource members[];
		try {
			members = folderResource.members();
			if (members.length == 0) {
				super.readMessages(folder);
				return;
			}
			for (int i = 0; i < members.length; i++) {
				folder.addMessage(readMessage((IFile) members[i]));
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		_reading = false;
	}

	//
	// Read single model objects from resources
	//

	private Server readServer(IProject serverProj) {
		Server server = new Server();
		server.setHostname(serverProj.getName());
		QualifiedName qname;
		try {
			qname = new QualifiedName(Activator.PLUGIN_ID, "User");
			server.setUsername(serverProj.getPersistentProperty(qname));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return server;
	}

	private Folder readFolder(IFolder folderResource) {
		Folder folder = new Folder(folderResource.getName());
		return folder;
	}

	private Message readMessage(IFile messageFile) {
		QualifiedName qname;
		String subject;
		String from;
		String date;
		try {
			InputStream is = messageFile.getContents();
			StringBuffer sb = new StringBuffer();
			byte[] buf = new byte[8192];
			while (true) {
				int bytesRead = is.read(buf);
				if (bytesRead <= 0)
					break;
				sb.append(new String(buf, 0, bytesRead));
			}
			qname = new QualifiedName(Activator.PLUGIN_ID, "Subject");
			subject = messageFile.getPersistentProperty(qname);
			qname = new QualifiedName(Activator.PLUGIN_ID, "From");
			from = messageFile.getPersistentProperty(qname);
			qname = new QualifiedName(Activator.PLUGIN_ID, "Date");
			date = messageFile.getPersistentProperty(qname);
			Message message = new Message(subject, from, date, sb.toString());
			message.setId(Integer.parseInt(messageFile.getName()));
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//
	// Get resource object corresponding to model object
	// 

	private IProject getServerProject(Server server) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getProject(server.getHostname());
	}

	private IFolder getFolderFolder(Folder folder) {
		IProject serverProj = getServerProject(folder.getServer());
		return serverProj.getFolder(folder.getName());
	}

	private IFile getMessageFile(Message message) {
		IFolder folder = getFolderFolder(message.getParent());
		return folder.getFile(Integer.toString(message.getId()));
	}

}
