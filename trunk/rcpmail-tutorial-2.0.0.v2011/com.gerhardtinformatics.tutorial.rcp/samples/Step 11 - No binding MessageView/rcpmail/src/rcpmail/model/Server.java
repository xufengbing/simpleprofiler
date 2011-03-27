package rcpmail.model;

import java.util.ArrayList;
import java.util.List;

public class Server extends ModelObject {
	private List<Folder> children;
	private String hostname;
	private String username;
	private String password;
	private int port;
	private Model parent;
	private Folder junkFolder;

	public Server() {
		children = new ArrayList<Folder>();
		
		Folder inbox = new Folder("Inbox");
		inbox.addMessage(new Message("Viagra", "xx@x.com", "2008-02-01","Make us happy\nand buy our fake v*agra"));
		inbox.addMessage(new Message("Nigeria", "nnnn@x.sa", "2008-02-02","I have 1 million...."));
		inbox.addMessage(new Message("I Love You", "xx@x.com", "2008-02-01","Make us happy\nand buy our fake v*agra"));
		inbox.addMessage(new Message("Make Money Fast", "nnnn@x.sa", "2008-02-02","I have 1 million...."));
		
		addFolder(inbox);
		addFolder(new Folder("Drafts"));
		addFolder(new Folder("Sent"));
		addFolder(junkFolder=new Folder("Junk"));
		addFolder(new Folder("Trash"));
	}

	public Folder getJunkFolder() {
		return junkFolder;
	}
	public void addFolder(Folder child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeFolder(Folder child) {
		children.remove(child);
		child.setParent(null);
	}

	public  List<Folder> getFolders() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public void setParent(Model parent) {
		this.parent = parent;
	}

	public Model getParent() {
		return parent;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Server( hostname=\""+hostname + "\""
		+", username=\"" +username + "\""
		+", password=\""+password + "\""
		+", port="+port
		+")";
	}

}
