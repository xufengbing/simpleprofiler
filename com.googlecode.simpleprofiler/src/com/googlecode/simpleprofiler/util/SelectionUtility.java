package com.googlecode.simpleprofiler.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class SelectionUtility {

	public static IJavaProject getJavaProjectFromSelection(ISelection sel) {
		if (sel != null && sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;
			Object first = ssel.getFirstElement();
			if (first != null && first instanceof IJavaProject) {
				return (IJavaProject) first;
			}
		}
		return null;
	}

	public static IJavaProject[] getJavaProjectsFromSelection(ISelection sel) {

		List<IJavaProject> list = new ArrayList<IJavaProject>();
		if (sel != null && sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;

			Object[] objects = ssel.toArray();
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] != null
							&& objects[i] instanceof IJavaProject) {
						IJavaProject tmp = (IJavaProject) objects[i];
						list.add(tmp);
					}
				}
			}

		}
		return list.toArray(new IJavaProject[0]);
	}
}
