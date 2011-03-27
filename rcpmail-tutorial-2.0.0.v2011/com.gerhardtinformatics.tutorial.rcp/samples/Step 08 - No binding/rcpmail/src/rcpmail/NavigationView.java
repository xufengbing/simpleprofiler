package rcpmail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import rcpmail.model.Folder;
import rcpmail.model.Model;
import rcpmail.model.Server;

public class NavigationView extends ViewPart {
	public static final String ID = "rcpmail.navigationView";
	private TreeViewer viewer;
	private PropertyChangeListener modelListener;
	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}
        
		public Object getParent(Object child) {
			if (child instanceof Server)
				return ((Server)child).getParent();
			if (child instanceof Folder)
				return ((Folder)child).getParent();
			return null;
		}
        
		public Object[] getChildren(Object parent) {
			if (parent instanceof Model)
				return ((Model)parent).getServers().toArray();
			if (parent instanceof Server)
				return ((Server)parent).getFolders().toArray();
			return new Object[0];
		}

        public boolean hasChildren(Object parent) {
			return getChildren(parent).length>0;
		}
	}
	
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			if (obj instanceof Server) 
				return ((Server) obj).getHostname();
			if (obj instanceof Folder)
				return ((Folder) obj).getName();
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof Folder)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	/**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(Model.getInstance());
		
		Model.getInstance().addPropertyChangeListener(modelListener=new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				viewer.setInput(Model.getInstance());
				
			}});
	}
	@Override
	public void dispose() {
		Model.getInstance().removePropertyChangeListener(modelListener);
		super.dispose();
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}