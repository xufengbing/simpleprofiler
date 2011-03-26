package com.google.code.t4eclipse.tools.eventtrack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class DisplayListener implements Listener {

	public void handleEvent(Event event) {
		onEvent(event);

	}

	private void onEvent(Event event) {
		synchronized(Utility.lock){
		if (Utility.filter(event)) {
			Utility.addStrToText(event);
		}
		}
	}

	static DisplayListener dL=new DisplayListener();
	public static DisplayListener getDefault(){
		return dL ;
		
	}
}