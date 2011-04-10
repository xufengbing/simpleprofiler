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

public class LongJob implements IWorkbenchWindowActionDelegate {

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

		// LongJOb
		final Job job = new Job("Long Running Job") {

			private int index = 0;

			protected IStatus run(IProgressMonitor monitor) {
				try {
					long start = System.currentTimeMillis();
					while (hasMoreWorkToDo()) {
						System.out.println("time used:"
								+ (System.currentTimeMillis() - start));
						if (monitor.isCanceled())
							return Status.CANCEL_STATUS;
						doWork();
					}
					return Status.OK_STATUS;
				} finally {
					/*
					 * Jobs of equal priority and <code>delay</code> with
					 * conflicting scheduling rules are guaranteed to run in the
					 * order they are scheduled. No guarantees are made about
					 * the relative execution order of jobs with unrelated or
					 * <code>null</code> scheduling rules, or different
					 * priorities.
					 */
					// schedule(1000); // start again in a seconds
				}
			}

			private void doWork() {
				this.index++;
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private boolean hasMoreWorkToDo() {
				if (this.index < 60) {
					return true;
				}
				return false;
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK())
					System.out.println("Job completed successfully");
				else
					System.out.println("Job did not complete successfully");
			}
		});
		// job.setSystem(true);
		job.schedule(); // start as soon as possible

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
