package com.ben.runjava.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

public class ClassLoaderUtil {

	public static URLClassLoader getClassLoaderForJavaProject(
			IJavaProject project, final ClassLoader parentClassLoader)
			throws CoreException, MalformedURLException {
		ResourcesPlugin.getWorkspace().getRoot();
		String[] classPathEntries = JavaRuntime
				.computeDefaultRuntimeClassPath(project);
		List<URL> urlList = new ArrayList<URL>();
		for (int i = 0; i < classPathEntries.length; i++) {
			String entry = classPathEntries[i];
			IPath path = new Path(entry);
			File file = path.toFile();
			URL url = file.toURI().toURL();
			if (!url.toString().endsWith(".jar")) {
				urlList.add(url);
			}
		}

		final URL[] urls = urlList.toArray(new URL[urlList.size()]);
		URLClassLoader classLoader = AccessController
				.doPrivileged(new PrivilegedAction<URLClassLoader>() {
					public URLClassLoader run() {
						return new URLClassLoader(urls, parentClassLoader);
					}
				});
		return classLoader;

	}

}
