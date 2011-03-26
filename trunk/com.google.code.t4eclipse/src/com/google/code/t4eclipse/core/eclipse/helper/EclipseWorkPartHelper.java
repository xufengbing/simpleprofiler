/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/

package com.google.code.t4eclipse.core.eclipse.helper;

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PartPane;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.internal.WorkbenchPage;

/**
 *
 * this helper class is used to for eclipse view/editor
 *
 * @author xufengbing
 *
 */
public class EclipseWorkPartHelper {
	public static EclipseWorkPartHelper getDefault() {
		return new EclipseWorkPartHelper();
	}

	/**
	 * open the view at current perspective. don't mind if current perspective
	 * already has the view
	 *
	 * @param viewId
	 * @return the viewpart
	 */
	public IViewPart actionOpenView(final String viewId) {
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI
				.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return null;
		}

		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage == null) {
			return null;
		}
		try {
			return activePage.showView(viewId);
		} catch (PartInitException e) {
			return null;
		}

	}

	/**
	 * set the perspective according to the pid
	 *
	 * @param pid
	 */
	public void actionSetPerspective(String pid) {

		IPerspectiveRegistry perspectives = PlatformUI.getWorkbench()
				.getPerspectiveRegistry();
		IPerspectiveDescriptor[] s = perspectives.getPerspectives();
		for (IPerspectiveDescriptor dis : s) {
			if (dis.getId().equals(pid)) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().setPerspective(dis);

			}

		}
	}

	/**
	 * close all editors.
	 */
	public void actionCloseEditors() {
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI
				.getWorkbench().getActiveWorkbenchWindow();

		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();

		activePage.closeAllEditors(false);

	}

	/**
	 * use this method for fast view
	 *
	 * @param viewId
	 * @param makeFast
	 *            show in fast view
	 * @throws PartInitException
	 */
	public void actionOpenView(final String viewId, boolean makeFast)
			throws PartInitException {

		// org.eclipse.ui.internal.dialogs.ShowViewDialog a;

		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI
				.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return;
		}

		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage == null) {
			return;
		}

		if (makeFast) {
			WorkbenchPage wp = (WorkbenchPage) activePage;

			IViewReference ref = wp.findViewReference(viewId);

			if (ref == null) {
				IViewPart part = wp.showView(viewId, null,
						IWorkbenchPage.VIEW_CREATE);
				ref = (IViewReference) wp.getReference(part);
			}

			if (!wp.isFastView(ref)) {
				wp.addFastView(ref);
			}
			wp.activate(ref.getPart(true));
		} else {
			activePage.showView(viewId);
		}

	}

	public Control getPartControl(IWorkbenchPart part) {
		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			PartPane pane = ((PartSite) site).getPane();
			return pane.getControl();
		} else {
			return null;
		}
	}

	public Control getPartControlWithViewID(String viewID) {

		IViewPart vp = getViewInCurrentpage(viewID);
		if (vp != null) {
			return getPartControl(vp);
		}
		return null;

	}

	/**
	 *
	 * @param viewid
	 * @return
	 */
	public IViewPart getViewInCurrentpage(String viewid) {
		IViewReference[] views = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences();

		for (IViewReference view : views) {
			IViewPart v = view.getView(false);

			if (v != null && v.getSite().getId().equals(viewid)) {
				return view.getView(false);
			}

		}
		return null;

	}

	public Control getPartControlFromActiveEditor() {
		IEditorPart editorpart = getActiveEditorPart();
		if (editorpart == null)
			return null;
		return getPartControl(editorpart);
	}

	public Control getPartControlFromActiveView() {
		IViewPart viewpart = getActiveView();
		if (viewpart == null)
			return null;
		return getPartControl(viewpart);
	}

	public IEditorPart getActiveEditorPart() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();
	}

	public IViewPart getActiveView() {
		IWorkbenchPart activepart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (activepart instanceof IViewPart)
			return (IViewPart) activepart;
		return null;
	}

	public void actionCloseView(String view) {
		IViewPart v = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(view);
		if (v != null)
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().hideView(v);
	}

}
