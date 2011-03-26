package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.core.utility.MenuUtility;
import com.google.code.t4eclipse.tools.view.MenuAnalyzerView;

public class AnalyzeNextMenuAction implements IViewActionDelegate {
	public static boolean checkMenu;

	private static IPartListener partListener;
	static {

		partListener = new IPartListener() {

			public void partOpened(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			public void partDeactivated(IWorkbenchPart part) {
				if (!(part instanceof MenuAnalyzerView)) {
					checkMenu = false;

				}

			}

			public void partClosed(IWorkbenchPart part) {
				if (part instanceof MenuAnalyzerView) {
					AnalyzeNextMenuAction.checkMenu = false;
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().removePartListener(this);
				}
			}

			public void partBroughtToTop(IWorkbenchPart part) {

			}

			public void partActivated(IWorkbenchPart part) {
				if (!(part instanceof MenuAnalyzerView)) {
					MenuUtility.removeMenuHideListener(part);
					MenuUtility.addMenuHideListener(part);
					checkMenu = true;

				}

			}
		};

	}

	private MenuAnalyzerView view;

	public void init(IViewPart view) {
		this.view = (MenuAnalyzerView) view;

	}

	public void run(IAction action) {
		boolean isChecked = action.isChecked();

		addRemovePageListener(isChecked);

	}

	public static void addRemovePageListener(boolean isChecked) {
		if (isChecked) {
			checkMenu = true;
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().addPartListener(partListener);

		} else {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().removePartListener(partListener);
			checkMenu = false;
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
 
	}

}
