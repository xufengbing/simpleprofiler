package rcpmail;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.model.WorkbenchAdapterBuilder;

import rcpmail.model.Model;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	private static final String PERSPECTIVE_ID = "rcpmail.perspective";

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	} 
	
	public IAdaptable getDefaultPageInput() {
		return Model.getInstance();
	}

	
	public void initialize(IWorkbenchConfigurer configurer) {
		// FG any better solution for the deprecated WorkbenchAdapterBuilder?
		WorkbenchAdapterBuilder.registerAdapters();
	}

	
}
