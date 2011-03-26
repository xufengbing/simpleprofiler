package com.google.code.t4eclipse.tools.prefenence;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;


import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.google.code.t4eclipse.Activator;
import com.google.code.t4eclipse.tools.eventtrack.Utility;

public class EclipseTestLibPref extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public EclipseTestLibPref() {

		super(GRID);
		IPreferenceStore x = Activator.getDefault().getPreferenceStore();

		setPreferenceStore(x);
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		Collection<Integer> vs = Utility.eventMap.keySet();
		TreeSet<Integer> set = new TreeSet<Integer>(vs);
		Iterator<Integer> i = set.iterator();
		while (i.hasNext()) {
			String tmp = Utility.eventMap.get(i.next());
			BooleanFieldEditor linkEditor = new BooleanFieldEditor(tmp, tmp,
					parent);
			addField(linkEditor);

		}
	}

	public static void initDefaults(IPreferenceStore prefs) {
		Collection<String> vs = Utility.eventMap.values();
		Iterator<String> i = vs.iterator();
		while (i.hasNext()) {
			String tmp = i.next();

			prefs.setDefault(tmp, checkInit(tmp));
		}
	}

	private static int[] defaultEvents = new int[] { SWT.KeyDown, SWT.KeyUp,
			SWT.MouseDown, SWT.MouseUp, SWT.MouseDoubleClick, SWT.Selection,
			SWT.DefaultSelection, SWT.FocusIn, SWT.Expand, SWT.Collapse };

	private static boolean checkInit(String tmp) {
		for (int event : defaultEvents) {
			if (Utility.eventMap.get(event).equals(tmp))
				return true;
		}
		return false;
	}

}
