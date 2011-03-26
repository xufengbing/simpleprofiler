package com.google.code.t4eclipse.tools.utility;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

public class SelectionUtility {

	public static String getSelectionInfo(IWorkbenchPart part) {
		String s = "\n\n";

		ISelectionProvider provider = part.getSite().getSelectionProvider();
		if (provider == null) {
			s += "NO Selection Provider in this active part:\n";

			return s;
		}

		s += "Selection  Provider:\n";
		s += provider.getClass().getName() + "\n\n";
		ISelection sel = provider.getSelection();
		if (sel == null) {
			s += "Selection is null\n";
			return s;
		}
		s += "Selection:\n";
		s += "class:" + sel.getClass().getName() + "\n";
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;
			s += "\n";
			s += "instanceof IStructuredSelection\n";
			s += "--first element--:\n";
			Object firstElement = ssel.getFirstElement();
			if (firstElement == null) {
				s += "  null\n";
			} else {
				s += "  class:\n    " + firstElement.getClass().getName()
						+ "\n";
				s += "  tostr:\n";
				s += "    " + firstElement.toString() + "\n";
			}

			s += "  size:" + ssel.size() + "\n";
			if (ssel.size() > 1)
				s += "  all elements class:\n";
			Object[] eles = ssel.toArray();
			for (int i = 0; i < eles.length; i++) {
				s += "  [" + i + "]:" + eles[i].getClass().getName() + "\n";
			}

		}

		s += "\n\nselection toString():\n";
		s += sel.toString() + "\n";
		return s;
	}
}
