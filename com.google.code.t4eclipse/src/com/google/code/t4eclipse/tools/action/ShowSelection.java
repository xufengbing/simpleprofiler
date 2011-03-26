package com.google.code.t4eclipse.tools.action;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class ShowSelection implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public ShowSelection() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();

		ISelectionProvider selectionProvider = part.getSite()
				.getSelectionProvider();
		if (selectionProvider == null) {
			System.out.println("no selection provide or this part!");
			return;
		} else {
			System.out.println("the selection provide is:"
					+ selectionProvider.getClass().getName());
		}

		ISelection selection = selectionProvider.getSelection();

		if (selection instanceof IStructuredSelection) {
			System.out.println("structure selection:"
					+ selection.getClass().getName() + " :" + selection);
			IStructuredSelection sstruction = (IStructuredSelection) selection;
			System.out.println("first element: class"
					+ sstruction.getFirstElement().getClass().getName()
					+ ":  content:" + sstruction.getFirstElement());

			Object[] allObject = sstruction.toArray();
			System.out.println(" object size:" + allObject.length);
			Iterator iterator = sstruction.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next().getClass().getName());
			}
		}

		else {

			System.out.println("commmon  selection:"
					+ selection.getClass().getName());

			System.out.println("selection" + selection);
		}

		if (selection instanceof TreeSelection) {
			System.out.println("TreeSelection:");
			TreeSelection selection3 = (TreeSelection) selection;
			TreePath[] path = selection3.getPaths();
			for (TreePath p : path) {
				System.out.println(" tree path--------------- :" + p);
				int count = p.getSegmentCount();
				for (int i = 0; i < count; i++) {
					Object segment = p.getSegment(i);
					System.out.println(" segament " + i + " :"
							+ segment.getClass().getName() + " " + segment);
				}

			}
		}
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}