package com.google.code.t4eclipse.tools.prefenence;


import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.google.code.t4eclipse.Activator;
 
 
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public void initializeDefaultPreferences() {
    	EclipseTestLibPref.initDefaults(Activator.getDefault().getPreferenceStore());
        
        
    }

}
