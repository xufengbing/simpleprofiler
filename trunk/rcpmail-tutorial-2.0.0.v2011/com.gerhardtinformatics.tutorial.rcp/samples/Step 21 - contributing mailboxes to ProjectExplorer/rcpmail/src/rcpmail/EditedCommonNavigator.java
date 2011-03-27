package rcpmail;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;

import rcpmail.model.Model;

public class EditedCommonNavigator extends CommonNavigator {

	public EditedCommonNavigator() {
	}
	
	@Override
	public IAdaptable getInitialInput(){
		return Model.getInstance();
	}
	
}
