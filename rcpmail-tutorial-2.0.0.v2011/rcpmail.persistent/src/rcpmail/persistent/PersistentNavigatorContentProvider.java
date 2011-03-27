package rcpmail.persistent;

import rcpmail.Controller;
import rcpmail.NavigatorContentProvider;

public class PersistentNavigatorContentProvider extends NavigatorContentProvider { 

	public PersistentNavigatorContentProvider() {
		super();
		Controller.setInstance(new PersistentModelManager());
		Controller.getInstance().init();
	}
	

}
