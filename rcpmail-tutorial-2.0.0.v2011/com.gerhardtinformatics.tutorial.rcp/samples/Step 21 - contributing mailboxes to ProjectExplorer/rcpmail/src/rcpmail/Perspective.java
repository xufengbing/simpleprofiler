package rcpmail;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	private final String NAVIGATOR_ID = "rcpmail.navigatorView";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(NAVIGATOR_ID,  true, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(View.ID);
		layout.addView(MessageTableView.ID, IPageLayout.TOP, 0.32f, "messages");
		
		layout.getViewLayout(MessageTableView.ID).setCloseable(false);
		layout.getViewLayout(MessageTableView.ID).setMoveable(false);
		
		//layout.getViewLayout(NavigationView.ID).setCloseable(false);
		
		layout.getViewLayout(View.ID).setMoveable(false);
	}
}
