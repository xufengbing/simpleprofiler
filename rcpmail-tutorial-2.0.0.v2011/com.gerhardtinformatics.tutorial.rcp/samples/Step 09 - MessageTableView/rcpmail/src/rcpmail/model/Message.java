package rcpmail.model;

public class Message extends ModelObject {
	private Folder parent;
	private String subject;
	private String from;
	private String date;
	private boolean isSpam;
	private String body;
	public Message(String subject, String from, String date,String body) {
		this.subject=subject;
		this.from=from;
		this.date=date;
		this.body=body;
	}

	public void setParent(Folder parent) {
		firePropertyChange("parent", this.parent, this.parent = parent);
		this.parent = parent;
	}

	public Folder getParent() {
		return parent;
	}

	public String toString() {
		return subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		firePropertyChange("subject", this.subject, this.subject = subject);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		firePropertyChange("from", this.from, this.from = from);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		firePropertyChange("date", this.date, this.date = date);
	}

	public void setSpam(boolean isSpam) {
		firePropertyChange("spam", this.isSpam, this.isSpam = isSpam);
	}

	public boolean isSpam() {
		return isSpam;
	}

	public void setBody(String body) {
		firePropertyChange("body", this.body, this.body = body);
	}

	public String getBody() {
		return body;
	}
}
