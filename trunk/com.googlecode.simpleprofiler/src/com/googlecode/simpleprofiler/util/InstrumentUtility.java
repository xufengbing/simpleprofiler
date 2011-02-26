package com.googlecode.simpleprofiler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
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
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.adaptor.LocationManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.service.datalocation.Location;
import com.googlecode.simpleprofiler.Activator;
import com.googlecode.simpleprofiler.model.LogUtility;

public class InstrumentUtility {

	private static void getProjectConfig(IJavaProject project)
			throws FileNotFoundException, IOException {

		IFile configFile = project.getProject().getFile("sp.config");
		File file = new File(configFile.getLocationURI());

		Properties properties = new Properties();
		properties.load(new FileReader(file));
		Collection<Object> keys = properties.values();

		for (Object o : keys) {
			System.out.println(o);
		}

	}

	public static List<String> getAllClassList(File outPutDirFile) {

		List<String> list = new ArrayList<String>();

		if (outPutDirFile.exists()) {

			for (File tmpFile : outPutDirFile.listFiles()) {
				findClasses(list, tmpFile, "");
			}
		}
		return list;
	}

	public static ClassPool getProjectClassPool(IJavaProject project)
			throws CoreException {
		ClassPool pool = new ClassPool(null);
		pool.appendSystemPath();
		String[] classPathEntries = JavaRuntime
				.computeDefaultRuntimeClassPath(project);
		for (String pathname : classPathEntries) {
			try {
				pool.appendClassPath(pathname);
			} catch (NotFoundException e) {
				Activator.getDefault().logError(
						"[" + project.getProject().getName() + "]"
								+ "Path Not Found:" + pathname, e);
			}
		}
		// changed into put model into boot classpath."classes" dir in jre
		// String bundleLocation = getBundleLocation();
		// if (bundleLocation != null) {
		// if (!bundleLocation.endsWith("/")) {
		// bundleLocation += "/";
		// }
		// String modelLocation = bundleLocation
		// + "com.googlecode.simpleprofiler.model.jar";
		// try {
		// if (new File(modelLocation).exists())
		// pool.appendClassPath(modelLocation);
		// } catch (NotFoundException e) {
		// Activator.getDefault().logError(
		// "[" + project.getProject().getName() + "]"
		// + "Path Not Found:" + modelLocation, e);
		// }
		// }
		return pool;

	}

	private static String getBundleLocation() {
		String bundleLocation = Activator.getDefault().getBundle()
				.getLocation();
		Activator.getDefault().logInfo(bundleLocation);
		String prefix = "reference:file:";
		Location location = LocationManager.getEclipseHomeLocation();
		File homeLocation;
		try {
			homeLocation = new File(location.getURL().toURI());
			if (bundleLocation.startsWith(prefix)) {
				bundleLocation = bundleLocation.substring(prefix.length());
				if (!bundleLocation.startsWith("/")) {
					bundleLocation = homeLocation.getAbsolutePath()
							+ bundleLocation;
				}
				return bundleLocation;
			} else {
				Activator.getDefault().logError(
						"bundle location:" + bundleLocation, null);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
		// runtime:
		// reference:file:/D:/svn/simpleprofiler/com.googlecode.simpleprofiler/
		// in dropin:
		// reference:file:dropins/com.googlecode.simpleprofiler_1.0.0/

	}

	public static File getOutputDir(IJavaProject project)
			throws JavaModelException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile outPutDir = root.getFile(project.getOutputLocation());
		File outPutDirFile = new File(outPutDir.getLocationURI());
		return outPutDirFile;

	}

	//
	// CtClass iaction = null;
	// try {
	// iaction = pool.get("org.eclipse.jface.action.IAction");
	// } catch (NotFoundException e1) {
	//
	// // do nothing
	// // TODO: must report error if use paras
	// // indicate that
	// // throw new RuntimeException(e1);
	// }

	// check sub type

	// try {
	// if (!cc.subtypeOf(iaction)) {
	// continue;
	// }
	// } catch (NotFoundException e1) {
	// }
	public static void instrumentJavaProject(IJavaProject project)
			throws NotFoundException, CoreException {

		File outPutDirFile = getOutputDir(project);
		ClassPool pool = getProjectClassPool(project);
		List<String> list = getAllClassList(outPutDirFile);

		// for each class, do instrument and write back using javasist
		for (String oneClass : list) {
			CtClass cc;
			try {
				cc = pool.get(oneClass);
			} catch (NotFoundException e2) {
				Activator.getDefault().logError(
						"Can Not get Class:" + oneClass, e2);
				continue;
			}
			// only the normal class need to be checked. not for interface
			// and others
			if (cc.isInterface() || cc.isAnnotation() || cc.isEnum()) {
				continue;
			}
			// TODO: check class if need to be instrumented.
			// check from configuration object
			CtClass[] interfaces = cc.getInterfaces();
			if (interfaces != null && interfaces.length > 0) {
				boolean isInstrumented = false;
				for (CtClass tmp : interfaces) {
					if (tmp.getName().equals(
							Constant.INSTRUMENTED_INDICATOR_CLASSNAME)) {
						isInstrumented = true;
						break;
					}
				}
				if (isInstrumented == true) {
					Activator.getDefault().logInfo(
							"Instumented Alreday:" + oneClass);
					continue;
				}
			}

			CtMethod[] methods = cc.getDeclaredMethods();
			for (CtMethod method : methods) {
				// checks if it is abstract or empty
				if (method.isEmpty()) {
					continue;
				}
				// check if it is native
				int modifier = method.getModifiers();
				if (Modifier.isNative(modifier)) {
					continue;
				}

				// TODO: check if method need to be instrumented
				// using configuration object

				// filter instrument the unpublic method call if specified
				// if (!Modifier.isPublic(modifier)) {
				// continue;
				// }

				// instrument method
				try {
					instrumentMethod(method);
				} catch (CannotCompileException e) {
					Activator.getDefault().logError(
							"Can not instrumented method:"
									+ method.getLongName(), e);
				}
			}

			// add a special interface to indicate that it is instrumented
			// if it is not being instrumented before.

			try {
				CtClass instrumentedInterface = pool
						.get(Constant.INSTRUMENTED_INDICATOR_CLASSNAME);
				cc.addInterface(instrumentedInterface);

				// This code adds an int field named "i". The initial value of
				// this field is 1.

				cc.writeFile(outPutDirFile.getAbsolutePath());
			} catch (Exception e) {
				Activator.getDefault().logError(
						"Can not write to class:" + cc.getName(), e);
			}

		}

	}

	public static void buildProject(IJavaProject project) throws CoreException {
		// build project:
		String location = Activator.getDefault().getBundle().getLocation();
		System.out.println("location:" + location);
		// TODO: using job mechanism to do clean&build.
		// use monitor to check if it is ok to continue
		// dont' do build now
		// clean than rebuild project
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// root.getWorkspace().build(0, null);

		final boolean[] isDone = new boolean[1];
		NullProgressMonitor monitor = new NullProgressMonitor() {
			@Override
			public void done() {
				isDone[0] = true;
			}

		};

		project.getProject().build(IncrementalProjectBuilder.CLEAN_BUILD,
				monitor);

		for (int i = 0; i < 100; i++) {
			if (!isDone[0]) {

				System.out.println("is done:" + isDone[0]);
			}
		}
		System.out.println(isDone[0]);
	}

	private static void instrumentMethod(CtMethod method)
			throws CannotCompileException {

		insertBefore(method);
		insertAfter(method);
	}

	private static void insertBefore(CtMethod method) {
		// method.insertAfter("final long endMs = System.nanoTime();" +
		try {
			method.addLocalVariable("startMs", CtClass.longType);
			method.addLocalVariable("startIndex", CtClass.intType);
			String startMsStat = "startMs = System.nanoTime();";
			String startIndexStat = "startIndex=com.googlecode.simpleprofiler.model.LogUtility.getDefault().getIndex();";

			method.insertBefore(startMsStat + startIndexStat);

		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void insertAfter(CtMethod method) {
		try {// Do nothing
			method.addLocalVariable("used", CtClass.longType);
			method.addLocalVariable("threadID", CtClass.longType);
			method.addLocalVariable("endIndex", CtClass.intType);

			String usedTimeStat = "used = System.nanoTime()-startMs;";
			String threadIDStat = "threadID = Thread.currentThread().getId();";
			String endIndexStat = "endIndex=com.googlecode.simpleprofiler.model.LogUtility.getDefault().getIndex();";

			// String methodName = method.getName();
			String methodLongName = method.getLongName();

			String logStat = "com.googlecode.simpleprofiler.model.LogUtility.getDefault().addLog(\""
					+ methodLongName
					+ "\", used, startIndex, endIndex, threadID);";
			System.out.println(logStat);
			// String after3 = "if(used>10000) {System.out.println(\""
			// + method.getLongName() + "\"+used" + ");}";
			method.insertAfter(usedTimeStat + threadIDStat + endIndexStat
					+ logStat);

		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
					public URLClassLoader run() {
						return new URLClassLoader(urls, parentClassLoader);
					}
				});
		return classLoader;

	}

	/**
	 * Recursive method used to find all classes in a given directory and<br>
	 * sub directories
	 *
	 * @param list
	 *            found full class name string will be added into this list
	 * @param file
	 *            internal used to track current file
	 * @param prefix
	 *            internal used to track current package name
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
			if (fileName.endsWith(".class")) {
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
