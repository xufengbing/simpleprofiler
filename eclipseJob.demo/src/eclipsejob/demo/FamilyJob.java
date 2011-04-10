package eclipsejob.demo;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class FamilyJob implements IWorkbenchWindowActionDelegate {

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
		final long start = System.currentTimeMillis();
		JobChangeAdapter jobChangeAdapter = new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				String name = event.getJob().getName();
				if (event.getResult().isOK())
					System.out.println(name + ":completed successfully after:"
							+ (System.currentTimeMillis() - start));
				else
					System.out.println(name
							+ ": not complete successfully after:"
							+ (System.currentTimeMillis() - start));
			}
		};

		// Create some family members and schedule them
		FamilyMember birg = new FamilyMember("Bridget", "Jones");
		birg.addJobChangeListener(jobChangeAdapter);
		FamilyMember tom = new FamilyMember("Tom", "Jones");
		tom.addJobChangeListener(jobChangeAdapter);

		FamilyMember tom2 = new FamilyMember("Tom2", "Jones");
		FamilyMember tom3 = new FamilyMember("Tom3", "Jones");
		FamilyMember tom4 = new FamilyMember("Tom4", "Jones");

		FamilyMember indiana = new FamilyMember("Indiana", "Jones");
		indiana.addJobChangeListener(jobChangeAdapter);

		tom.schedule();
		tom2.schedule();
		tom3.schedule();
		tom4.schedule();

		birg.schedule();
		indiana.schedule(1000);

		// Obtain the Platform job manager
		IJobManager manager = Job.getJobManager();
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {

		}

		// put the family to sleep
		manager.sleep("Jones");

		// put the family to sleep for good!
		// manager.cancel("Jones");

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
