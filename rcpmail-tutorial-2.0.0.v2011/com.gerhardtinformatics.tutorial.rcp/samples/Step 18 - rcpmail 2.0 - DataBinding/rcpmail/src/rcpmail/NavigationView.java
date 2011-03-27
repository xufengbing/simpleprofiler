package rcpmail;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.internal.databinding.observable.tree.IUnorderedTreeProvider;
import org.eclipse.jface.internal.databinding.provisional.viewers.UnorderedTreeContentProvider;
import org.eclipse.jface.internal.databinding.provisional.viewers.ViewerLabelProvider;
import org.eclipse.jface.viewers.IViewerLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import rcpmail.model.Folder;
import rcpmail.model.Model;
import rcpmail.model.Server;

public class NavigationView extends ViewPart {
	public static final String ID = "rcpmail.navigationView";
	private TreeViewer tree;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		tree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);

		IUnorderedTreeProvider unorderedTreeProvider = new IUnorderedTreeProvider() {
			public IObservableSet createChildSet(Object element) {
				if (element instanceof Model) {
					return ((Model)element).getServers();
				} else if (element instanceof Server) {
					return ((Server) element).getFolders();
				}
				return null;
			}
			public Realm getRealm() {
				return null;
			}
		};

		// Label provider for the tree
		IViewerLabelProvider labelProvider = new ViewerLabelProvider() {
			public void updateLabel(ViewerLabel label, Object element) {
				if (element instanceof Server) {
					label.setText(((Server) element).getHostname());
					setImage(label, ISharedImages.IMG_OBJ_ELEMENT);
				} else if (element instanceof Folder) {
					label.setText(((Folder)element).getName());
					setImage(label, ISharedImages.IMG_OBJ_FOLDER);
				}
			}
			private void setImage(ViewerLabel label, String imageKey) {
				label.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(imageKey));
			}
		};

		// UpdatableTreeContentProvider converts an ITreeProvider into a
		// standard JFace content provider
		UnorderedTreeContentProvider contentProvider = new UnorderedTreeContentProvider(
				unorderedTreeProvider, "pending...", false);
		
		tree.setContentProvider(contentProvider);
		tree.setLabelProvider(labelProvider);

		tree.setInput(Model.getInstance());
		// sort alphabetically
		tree.setSorter(new ViewerSorter());
		IWorkbenchPartSite site = getSite();
		site.setSelectionProvider(tree);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		tree.getControl().setFocus();
	}
}