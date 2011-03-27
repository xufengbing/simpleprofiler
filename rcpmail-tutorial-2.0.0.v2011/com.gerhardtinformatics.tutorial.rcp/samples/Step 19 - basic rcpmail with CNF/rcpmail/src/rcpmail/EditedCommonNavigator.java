package rcpmail;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;

public class EditedCommonNavigator extends CommonNavigator {

	public EditedCommonNavigator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IAdaptable getInitialInput() {
		NavigationView nv = new NavigationView();
		return nv.createDummyModel();
	}
	
}
