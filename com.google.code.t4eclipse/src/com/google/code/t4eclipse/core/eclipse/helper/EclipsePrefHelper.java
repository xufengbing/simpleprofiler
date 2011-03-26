package com.google.code.t4eclipse.core.eclipse.helper;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class EclipsePrefHelper {
    public static EclipsePrefHelper getDefault() {
        return new EclipsePrefHelper();
    }


    public void openPrefPage(String pageID) {

		Shell windowShell = null;

		Shell[] shells = Display.getDefault().getShells();
		for (int i = 0; i < shells.length; i++) {
			Object data = shells[i].getData();
			if (data != null && data instanceof IWorkbenchWindow) {
				windowShell = shells[i];
				break;
			}
		}
		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
				windowShell, pageID, null, null);
		dialog.setBlockOnOpen(false);
		dialog.open();

	}
}
