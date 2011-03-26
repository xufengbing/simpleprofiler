package com.google.code.t4eclipse.tools.action;

import java.text.DecimalFormat;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class MemoryCheckAction implements IWorkbenchWindowActionDelegate {

	DecimalFormat decFmt = new DecimalFormat("0.0");
	private long free;
	private long total;
	private long max;

	// private Label pre;
	// private Label cur;
	// private StatusLineManager manager;
	public void dispose() {

	}

	public void init(IWorkbenchWindow window) {
		// WorkbenchWindow windows =
		// ((WorkbenchWindow)PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		// manager = windows.getStatusLineManager();
		// Composite status=(Composite) manager.getControl();
		// this.pre=new Label(status,SWT.NULL);
		// this.cur=new Label(status,SWT.NULL);

	}

	public void run(IAction action) {
		// this.pre.setText("xxxxxxxxxxx");
		// this.cur.setText("qqqqqqqqqqqqqqqqqqqqqqqqqqq");
		// manager.markDirty();
		// manager.update(true);
		Runtime.getRuntime().gc();
		long freeM = Runtime.getRuntime().freeMemory();
		long totalM = Runtime.getRuntime().totalMemory();
		long maxM = Runtime.getRuntime().maxMemory();

		if (action.isChecked()) {
			this.free = freeM;
			this.total = totalM;
			this.max = maxM;
		} else {
			long used = (totalM - freeM) - (this.total - this.free);

			String begin = "******start******\n"
					+ getAllStatus(this.total, this.free, this.max) + "\n\n";
			String end = "****** end ******\n"
					+ getAllStatus(totalM, freeM, maxM) + "\n\n";
			String changed = "***use changed***\n" + getMemoryStr(used);
			MessageDialog.openInformation(
					Display.getDefault().getActiveShell(),
					"Memory Status Report", begin + end + changed);
		}

	}

	private String getAllStatus(long totalM, long freeM, long maxM) {
		String statusIndex = "total   /   free   /   max   /   use" + "\n";
		String statusData = getMemoryStr(totalM) + " / " + getMemoryStr(freeM)
				+ " / " + getMemoryStr(maxM) + " / "
				+ getMemoryStr(totalM - freeM) + "\n";
		return statusIndex + statusData;
	}

	private String getMemoryStr(long mem) {
		long copy = Math.abs(mem);
		String tmp = mem < 0 ? "-" : "";
		return tmp + decFmt.format(copy / 1024.0 / 1024.0) + "M";
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
