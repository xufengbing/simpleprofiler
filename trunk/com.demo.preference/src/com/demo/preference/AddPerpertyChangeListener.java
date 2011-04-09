package com.demo.preference;

import org.eclipse.core.internal.preferences.PreferencesService;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class AddPerpertyChangeListener implements
		IWorkbenchWindowActionDelegate {
	IPreferenceChangeListener listener;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
 	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.listener = new IPreferenceChangeListener() {

			@Override
			public void preferenceChange(PreferenceChangeEvent event) {
				AddPerpertyChangeListener.this.printEvent(event);
			}
		};

	}

	@Override
	public void run(IAction action) {
		IEclipsePreferences p;
		// p=
		// PreferencesService.getDefault().getRootNode().node("instance").node("");

		p = (IEclipsePreferences) PreferencesService.getDefault().getRootNode()
				.node("instance");
		try {
			String[] names = p.childrenNames();
			for (String tmp : names) {
				IEclipsePreferences ptmp = (IEclipsePreferences) p.node(tmp);
				ptmp
						.addPreferenceChangeListener(AddPerpertyChangeListener.this.listener);
			}

		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

	}

	protected void printEvent(PreferenceChangeEvent event) {
		System.out.println("key:" + event.getSource());
		System.out.println("key class:" + event.getKey());
		System.out.println("key:" + event.getKey());

		Object oldValue = event.getOldValue();

		System.out.println("old:" + oldValue);
		if (oldValue != null) {
			System.out.println("old class:" + oldValue.getClass().getName());
		}
		Object newValue = event.getNewValue();
		System.out.println("new:" + newValue);
		if (newValue != null) {
			System.out.println("new class:" + newValue.getClass().getName());
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
