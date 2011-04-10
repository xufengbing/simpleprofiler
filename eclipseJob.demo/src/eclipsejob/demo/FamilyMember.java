package eclipsejob.demo;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class FamilyMember extends Job {
	private String lastName;
	private String firstname;
	public FamilyMember(String firstName, String lastName) {
		super(firstName + " " + lastName);
		this.lastName = lastName;
		this.firstname=firstName;
	}

	protected IStatus run(IProgressMonitor monitor) {
	 System.out.println(">>JOB started:"+this.firstname+ ":"+Thread.currentThread().getName());

//     ResourcesPlugin.getWorkspace().getRuleFactory();

	 try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("<<<<JOB finished:"+this.firstname);
		return Status.OK_STATUS;
	}

	public boolean belongsTo(Object family) {
		return lastName.equals(family);
	}
}