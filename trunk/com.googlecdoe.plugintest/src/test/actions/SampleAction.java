package test.actions;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.framework.internal.core.BundleHost;
import org.eclipse.osgi.internal.loader.BundleLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import test.Activator;

import com.ben.runjava.util.ClassLoaderUtil;
/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 *
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	IMethod method;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {
		if (this.method != null) {
			if (method.getParameterTypes().length != 0) {
				error("Java method must have zero arguments");
				return;
			}

			IType type = method.getDeclaringType();
			String typeName = type.getFullyQualifiedName();
			//
			BundleLoader bundleLoader = ((BundleHost) Activator.getDefault()
					.getBundle()).getLoaderProxy().getBundleLoader();

			IJavaProject javaProject = type.getJavaProject();

			ClassLoader loader=null;

				try {
					loader = ClassLoaderUtil.getClassLoaderForJavaProject(
							javaProject, this.getClass().getClassLoader());
				} catch (MalformedURLException e1) {
					return;
				} catch (CoreException e1) {
					return;
  				}

			// getClassLoaderForJavaProjecnew ProjectClassLoader(javaProject);
			Class<?> c;
			try {
				c = loader.loadClass(typeName);
			} catch (ClassNotFoundException e) {
				error("Failed to load: " + typeName);
				return;
			}

			// Instantiate the target object

			Object target;
			try {
				target = c.newInstance();
			} catch (Exception e) {
				error("Failed to instantiate: " + typeName);
				return;
			}

			// Use reflection to obtain and execute the method

			Method m;
			try {
				m = c.getMethod(method.getElementName(), new Class[] {});
			} catch (Exception e) {
				error("Failed to find method: " + method.getElementName());
				return;
			}
			Object result;
			try {
				result = m.invoke(target, new Object[] {});
			} catch (Exception e) {
				error("Failed to invoke method: " + method.getElementName());
				e.printStackTrace();
				return;
			}

		}
	}

	private void error(String message) {
		MessageDialog.openError(Display.getDefault().getActiveShell(), "",
				message);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if ((element instanceof IMethod)) {

				this.method = (IMethod) element;
				return;
			}
		}
		this.method = null;

	}

}
