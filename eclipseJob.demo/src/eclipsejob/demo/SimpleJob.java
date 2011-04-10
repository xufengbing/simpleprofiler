package eclipsejob.demo;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class SimpleJob implements IWorkbenchWindowActionDelegate {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {
		// Simplest JOb

		// job states: sleeping ,waiting, running ,None
		System.out.println("Thread Main:" + Thread.currentThread().getName());
		Job simpleJob = new Job("HelloJob") {
			protected IStatus run(IProgressMonitor monitor) {
				System.out.println("Hello World (from a background job)");
				System.out.println(" HelloJOB:"
						+ Thread.currentThread().getName());
				try {
					Thread.currentThread().sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Status.OK_STATUS;
			}
		};
		// Job.INTERACTIVE,Job.SHORT,Job.LONG,Job.BUILD,Job.DECORATE;
		simpleJob.setPriority(Job.SHORT);

		/**
		 * There are three categories of jobs: systems, user and default. The
		 * distinction is that system jobs, by default, do not appear in the
		 * Progress view (unless the view is in verbose mode) and do not animate
		 * the status line. The job in the above example has been marked as a
		 * system job (line ). User jobs and default jobs will show UI
		 * affordances when running. In addition, a user job will show a
		 * progress dialog to the user with the option to be run in the
		 * background.
		 */
		simpleJob.setUser(true);
		// simpleJob.setSystem(true);
		simpleJob.schedule(); // start as soon as possible

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
