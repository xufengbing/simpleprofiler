package com.google.code.t4eclipse.tools.utility;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.google.code.t4eclipse.tools.view.MainView;

public class MainViewTabUtility {

	
	public static IViewPart openFocusT4EclipseView() {
		// make this view visiable
		try {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().showView(MainView.viewID, null,
							IWorkbenchPage.VIEW_ACTIVATE);
		} catch (PartInitException e) {
			return null;
		}
	}
	public static void openT4EclipseView() {
		// make this view visiable
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().showView(MainView.viewID, null,
							IWorkbenchPage.VIEW_VISIBLE);
		} catch (PartInitException e) {
		}
	}

	public static void showTabInT4EclipseView(String tab) {
//		//make it visiable first
//		openT4EclipseView();
		
		//open the tab
		TabFolder tabs=MainView.getMainSWT().getTabFolder();
		TabItem[] items = tabs.getItems();
		for(int i=0;i<items.length;i++){
			if(tab.equals(items[i].getText())){
				tabs.setSelection(items[i]);
			}
		}
	}
	
	
	
	
}
