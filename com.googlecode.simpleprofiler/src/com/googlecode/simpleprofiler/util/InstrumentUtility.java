package com.googlecode.simpleprofiler.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.security.PrivilegedAction;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.JavaRuntime;

public class InstrumentUtility {

	public static final String INSTRUMENTED_INDICATOR_CLASSNAME = "com.googlecode.simpleprofiler.model.InstrumentedIndicator";

	public static void instrumentJavaProject(IJavaProject project)
			throws CoreException, NotFoundException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// TODO: using job mechanism to do clean&build.
		// use monitor to check if it is ok to continue
		// dont' do build now
		// clean than rebuild project
		// root.getWorkspace().build(0, monitor)
		// System.out.println("build project.............");
		// project.getProject().build(IncrementalProjectBuilder.FULL_BUILD,
		// null);

		IFile outPutDir = root.getFile(project.getOutputLocation());
		File outPutDirFile = new File(outPutDir.getLocationURI());
		if (outPutDirFile.exists()) {
			List<String> list = new ArrayList<String>();
			for (File tmpFile : outPutDirFile.listFiles()) {
				findClasses(list, tmpFile, "");
			}

			// final URLClassLoader classLoader =
			// getClassLoaderForJavaProject(project);
			ClassPool pool = new ClassPool(null);
			pool.appendSystemPath();
			String[] classPathEntries = JavaRuntime
					.computeDefaultRuntimeClassPath(project);

			for (String pathname : classPathEntries) {
				pool.appendClassPath(pathname);
			}

			// for each class, do instrument and write back using javasist
			for (String oneClass : list) {
				CtClass cc = pool.get(oneClass);
				// only the normal class need to be checked. not for interface
				// and others
				if (cc.isInterface() || cc.isAnnotation() || cc.isEnum()) {
					continue;
				}
				// check sub class
				// cc.subclassOf(cc)
				// //check if
				// if(cc.getInterfaces())

				// CtClass instrumentedInterface = pool
				// .get(INSTRUMENTED_INDICATOR_CLASSNAME);
				// CtClass[] interfaces = cc.getInterfaces();
				// if (interfaces != null && interfaces.length > 0) {
				// boolean isInstrumented = false;
				// for (CtClass tmp : interfaces) {
				// if (tmp.getName().equals(
				// INSTRUMENTED_INDICATOR_CLASSNAME)) {
				// isInstrumented = true;
				// break;
				// }
				// }
				// if (isInstrumented == true) {
				// // this class has being instrumented before.
				// continue;
				// }
				// }
				// add a special interface to indicate that it is instrumented
				// if it is not being instrumented before.
				// cc.addInterface(instrumentedInterface);
				CtMethod[] methods = cc.getDeclaredMethods();
				for (CtMethod method : methods) {
					// check if it is an abstract method
					// here checks if it is abstract or empty
					if (method.isEmpty()) {
						continue;
					}
					// check if it is native
					int modifier = method.getModifiers();
					if (Modifier.isNative(modifier)) {
						continue;
					}

					// filter instrument the unpublic method call if specified
					if (!Modifier.isPublic(modifier)) {
						continue;
					}

					// instrument method
					try {
						instrumentMethod(method);
					} catch (CannotCompileException e) {
						RuntimeException ex = new RuntimeException(
								"Instrument method failed:"
										+ method.getLongName(), e);
						System.err.println(ex.getMessage());
					}
				}
				// write back instrumented class files

				try {
					cc.writeFile(outPutDirFile.getAbsolutePath());
				} catch (Exception e) {
					RuntimeException ex = new RuntimeException(
							"Write back class failed:" + cc.getName(), e);
					System.err.println(ex.getMessage());

				}

			}

		}

	}

	private static void instrumentMethod(CtMethod method)
			throws CannotCompileException {

		insertBefore(method);
		insertAfter(method);
	}

	private static void insertAfter(CtMethod method) {
		// Do nothing
	}

	private static void insertBefore(CtMethod method)
			throws CannotCompileException {

		method.insertBefore("System.out.println(\"" + method.getLongName()
				+ "\");");
	}

	public static URLClassLoader getClassLoaderForJavaProject(
			IJavaProject project) throws CoreException, MalformedURLException {
		ResourcesPlugin.getWorkspace().getRoot();
		String[] classPathEntries = JavaRuntime
				.computeDefaultRuntimeClassPath(project);
		List<URL> urlList = new ArrayList<URL>();
		for (int i = 0; i < classPathEntries.length; i++) {
			String entry = classPathEntries[i];
			IPath path = new Path(entry);
			File file = path.toFile();
			URL url = file.toURI().toURL();
			urlList.add(url);
		}
		final ClassLoader parentClassLoader = project.getClass()
				.getClassLoader();
		final URL[] urls = urlList.toArray(new URL[urlList.size()]);
		URLClassLoader classLoader = AccessController
				.doPrivileged(new PrivilegedAction<URLClassLoader>() {
					@Override
					public URLClassLoader run() {
						return new URLClassLoader(urls, parentClassLoader);
					}
				});
		return classLoader;

	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 */
	private static void findClasses(List<String> list, File file, String prefix) {
		String fileName = file.getName();
		if (file.isDirectory()) {
			File[] files = file.listFiles();

			for (File tmpFile : files) {
				String prefex2;
				if (prefix.equals("")) {
					prefex2 = fileName;
				} else {
					prefex2 = prefix + "." + fileName;
				}
				findClasses(list, tmpFile, prefex2);
			}
		}

		if (file.isFile()) {
			// internal class don't count
			// if (fileName.endsWith(".class") && !fileName.contains("$")) {

			if (fileName.endsWith(".class") && !fileName.contains("$")) {
				String className = fileName.substring(0, fileName.length() - 6);
				String fullClassName;
				if (prefix.equals("")) {
					fullClassName = className;
				} else {
					fullClassName = prefix + "." + className;
				}
				list.add(fullClassName);

			}
		}
	}

}
