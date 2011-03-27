package rcpmail.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenNewWindowHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow != null) {
			IAdaptable pageInput = activeWorkbenchWindow.getActivePage().getInput();
			try {
				activeWorkbenchWindow.getWorkbench().openWorkbenchWindow(pageInput);
			} catch (WorkbenchException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
