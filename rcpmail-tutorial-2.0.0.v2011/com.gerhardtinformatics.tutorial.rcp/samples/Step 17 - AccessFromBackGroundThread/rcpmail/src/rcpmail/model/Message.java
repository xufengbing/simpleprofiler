package rcpmail.model;

import java.util.Date;

import org.eclipse.swt.widgets.Display;

public class Message extends ModelObject {
	volatile private Folder parent;
	volatile private String subject;
	volatile private String from;
	volatile private String date;
	volatile private boolean isSpam;
	volatile private String body;
	public Message(String subject, String from, String date,String body) {
		this.subject=subject;
		this.from=from;
		this.date=date;
		this.body=body;
		Thread t=new Thread(new Runnable(){
			public void run() {
				while(true) {
							setDate(new Date().toString());							
					try {
						Thread.sleep((long)(Math.random()*10000));
					} catch (InterruptedException e) {
						return;
					}
				}
			}});
		t.setDaemon(true);
		t.start();
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
