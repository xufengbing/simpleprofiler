package com.google.code.t4eclipse.tools.utility;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class EclipseCommonUtility {

	public static IWorkbenchPart getActivePart() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActivePart();

	}

	public static IViewPart getViewPart(String id) {

		IViewReference[] views = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences();

		for (IViewReference vr : views) {

			if (vr.getId().equals(id)) {
				return vr.getView(false);
			}
		}
		return null;
	}

	public static void showView(String viewId) {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage()
					.showView(viewId, null, IWorkbenchPage.VIEW_ACTIVATE);
		} catch (PartInitException e) {

		}

	}
}