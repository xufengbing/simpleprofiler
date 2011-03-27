package rcpmail.model;

import java.util.ArrayList;
import java.util.List;

public class Folder extends ModelObject {
	private List<Message> messages;
	private String name;
	private Server server;

	public Folder(String name) {
		this.name = name;
		messages = new ArrayList<Message>();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+messages.toString();
	}
	public void addMessage(Message child) {
		messages.add(child);
		child.setParent(this);
	}

	public void removeMessage(Message child) {
		messages.remove(child);
		child.setParent(null);
	}

	public List<Message> getMessages() {
		return messages;
	}

	public String getName() {
		return name;
	}

	public boolean hasChildren() {
		return messages.size() > 0;
	}

	public void setParent(Server parent) {
		this.server = parent;
	}

	public Server getParent() {
		return server;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
}
