package rcpmail.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import rcpmail.View;
import rcpmail.model.Folder;
import rcpmail.model.Message;

public class MarkAsSpamAndMoveHandler extends AbstractHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		if (activePart instanceof View) {
			Message m = ((View) activePart).getMessage();
			m.setSpam(true);
			Folder junk = m.getParent().getParent().getJunkFolder();
			m.getParent().removeMessage(m);
			junk.addMessage(m);
			((View) activePart).setMessage(null);
		}
		return null;
	}

}
