package com.google.code.t4eclipse.tools.utility;


import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.console.ConsolePlugin;
//import org.eclipse.ui.console.IConsole;
//import org.eclipse.ui.console.IConsoleConstants;
//import org.eclipse.ui.console.IConsoleManager;
//import org.eclipse.ui.console.IConsoleView;
//import org.eclipse.ui.console.MessageConsole;
//import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Create an instance of this class in any of your plugin classes.
 *
 * Use it as follows ...
 *
 * ConsoleDisplayMgr.getDefault().println("Some error msg",
 * ConsoleDisplayMgr.MSG_ERROR); ... ... ConsoleDisplayMgr.getDefault().clear();
 * ...
 */
public class CodeGenerateMgr {
	private static CodeGenerateMgr fDefault = null;

	public static final String title = "Eclipse_TestLib";

	public CodeGenerateMgr() {

	}

	public static CodeGenerateMgr getDefault() {
		if (fDefault == null)
			fDefault = new CodeGenerateMgr();
		return fDefault;
	}

	public void printlnCode(String msg) {
		println(msg, SWT.COLOR_DARK_BLUE);

	}

	public void printlnComment(String msg) {
		println(msg, SWT.COLOR_DARK_GREEN);
	}

	public void println(String msg, int color) {
//		if (msg == null)
//			return;
//
//		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow();
//
//		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
//		IConsoleView view = null;
//		try {
//			view = (IConsoleView) activePage.showView(
//					IConsoleConstants.ID_CONSOLE_VIEW, null,
//					IWorkbenchPage.VIEW_VISIBLE);
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		MessageConsole fMessageConsole = findConsole(view, title);
//
//		MessageConsoleStream msgConsoleStream = fMessageConsole
//				.newMessageStream();
//		msgConsoleStream.setColor(Display.getCurrent().getSystemColor(color));
//		// SWT.COLOR_DARK_BLUE));
//		msgConsoleStream.println(msg);

	}

//	private MessageConsole findConsole(IConsoleView view, String name) {
//		ConsolePlugin plugin = ConsolePlugin.getDefault();
//		IConsoleManager conMan = plugin.getConsoleManager();
//		IConsole[] existing = conMan.getConsoles();
//		for (int i = 0; i < existing.length; i++)
//			if (name.equals(existing[i].getName()))
//				return (MessageConsole) existing[i];
//		// no console found, so create a new one
//		MessageConsole myConsole = new MessageConsole(name, null);
//		conMan.addConsoles(new IConsole[] { myConsole });
//		return myConsole;
//	}

}
