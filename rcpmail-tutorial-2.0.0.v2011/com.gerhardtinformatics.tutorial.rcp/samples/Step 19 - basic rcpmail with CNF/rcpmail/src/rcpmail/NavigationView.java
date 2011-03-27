package rcpmail;

import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.ViewPart;

public class NavigationView extends ViewPart {
	public static final String ID = "test.navigationView";
	private TreeViewer viewer;
	 
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		
		@Override
		public Object getAdapter(Class adapter) {
			if (adapter == TreeObject.class) return this;
			if (adapter == IWorkbenchAdapter.class)
				return ResourcesPlugin.getWorkspace().getRoot().getAdapter(adapter);
			return null;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	/**
     * We will set up a dummy model to initialize tree heararchy. In real
     * code, you will connect to a real model and expose its hierarchy.
     */
    public TreeObject createDummyModel() {
        TreeObject to1 = new TreeObject("Inbox");
        TreeObject to2 = new TreeObject("Drafts");
        TreeObject to3 = new TreeObject("Sent");
        TreeParent p1 = new TreeParent("me@this.com");
        p1.addChild(to1);
        p1.addChild(to2);
        p1.addChild(to3);

        TreeObject to4 = new TreeObject("Inbox");
        TreeParent p2 = new TreeParent("other@aol.com");
        p2.addChild(to4);

        TreeParent root = new TreeParent("");
        root.addChild(p1);
        root.addChild(p2);
        return root;
    }

	/**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(createDummyModel());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}