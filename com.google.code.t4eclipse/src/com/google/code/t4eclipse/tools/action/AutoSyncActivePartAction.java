package com.google.code.t4eclipse.tools.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.core.utility.MenuUtility;
import com.google.code.t4eclipse.tools.utility.EclipseCommonUtility;
import com.google.code.t4eclipse.tools.view.ActivePartControlView;
import com.google.code.t4eclipse.tools.view.MenuAnalyzerView;

public class AutoSyncActivePartAction implements IViewActionDelegate {

	private ActivePartControlView view;
	private static IPartListener partListener;
	static {

		partListener = new IPartListener() {

			public void partOpened(IWorkbenchPart part) {

			}

			public void partDeactivated(IWorkbenchPart part) {

			}

			public void partClosed(IWorkbenchPart part) {

			}

			public void partBroughtToTop(IWorkbenchPart part) {

			}

			public void partActivated(IWorkbenchPart part) {
				// if it is not the view itself
				if (!(part instanceof ActivePartControlView)) {
					IViewPart v = EclipseCommonUtility
							.getViewPart("com.google.code.t4eclipse.tools.view.ActivePartControlView");
					if (v != null && v instanceof ActivePartControlView) {
						ActivePartControlView ac = (ActivePartControlView) v;
						ac.showAcitivePart(part);
					}

				}

			}
		};

	}

	public void init(IViewPart view) {
		if (view != null && view instanceof ActivePartControlView) {
			ActivePartControlView partView = (ActivePartControlView) view;
			this.view = partView;

		}

	}

	public void run(IAction action) {
		boolean isChecked = action.isChecked();
		if (isChecked) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().addPartListener(partListener);
		} else {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().removePartListener(partListener);

		}

	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

	public static IPartListener getAutoSyncPartListener() {
		return partListener;
	}

}
