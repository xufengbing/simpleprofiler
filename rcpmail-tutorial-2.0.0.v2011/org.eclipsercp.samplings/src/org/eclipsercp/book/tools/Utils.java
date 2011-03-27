/*******************************************************************************
 * Copyright (c) 2004, 2005 Jean-Michel Lemieux, Jeff McAffer and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Hyperbola is an RCP application developed for the book 
 *     Eclipse Rich Client Platform - 
 *         Designing, Coding, and Packaging Java Applications 
 *
 * Contributors:
 *     Jean-Michel Lemieux and Jeff McAffer - initial implementation
 *******************************************************************************/
package org.eclipsercp.book.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

/**
 * Useful utility methods with no home
 */
public class Utils {
	
	/*
	 * Buffer size used when copying files
	 */
	private static final int BUF_SIZE = 4000;

	/**
	 * Deep file system copy with progress.
	 * 
	 * @param sourceFile the originating file or folder.
	 * @param destFile the destination file or folder that may exist if <code>overwrite</code> is true.
	 * @param overwrite determine if copy should fail is destFile exists.
	 * @param monitor to report progress and cancel the operation.
	 * @throws IOException if anything goes wrong.
	 */
	public static void copy(File sourceFile, File destFile, boolean overwrite, IProgressMonitor monitor)
			throws IOException {
		monitor.subTask("Copying " + sourceFile.getName());
		if (overwrite || !destFile.exists()) {
			if(monitor.isCanceled())
				return;
			File parent = destFile.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			if (sourceFile.isDirectory()) {
				if (!destFile.exists())
					destFile.mkdir();
				File[] children = sourceFile.listFiles();
				for (int i = 0; i < children.length; i++) {
					File file = children[i];
					copy(file, new File(destFile, file.getName()), true, monitor);
				}
			} else {
				FileInputStream in = null;
				FileOutputStream out = null;
				try {
					in = new FileInputStream(sourceFile);
					out = new FileOutputStream(destFile);

					byte[] buffer = new byte[BUF_SIZE];
					int count = 0;
					do {
						out.write(buffer, 0, count);
						count = in.read(buffer, 0, buffer.length);
					} while (count != -1 && ! monitor.isCanceled());
				} finally {
					close(out);
					close(in);
				}
			}
		}
	}

	/**
	 * Close the given output stream and handle all close errors.
	 * @param device the output stream to close.
	 */
	public static void close(OutputStream device) {
		if (device != null) {
			try {
				device.close();
			} catch (IOException ioex) {
				// ignore
			}
		}
	}

	/**
	 * Close the given input stream and handle all close errors.
	 * @param device the input stream to close.
	 */
	public static void close(InputStream device) {
		if (device != null) {
			try {
				device.close();
			} catch (IOException ioex) {
				// ignore
			}
		}
	}
	
	/**
	 * Runs the given runnable in a progress dialog.
	 * @param shell the parent for the progress dialog
	 * @param runnable the operation to run
	 */
	public static void run(final Shell shell, IRunnableWithProgress runnable) {
		ProgressMonitorDialog d = new ProgressMonitorDialog(shell);
		try {
			d.run(true, true, runnable);
		} catch (InvocationTargetException e) {
			ErrorDialog.openError(shell, "Import Error", "Error importing chapters", statusFrom(e.getTargetException()));
		} catch (InterruptedException e) {
			ErrorDialog.openError(shell, "Import Error", "Error importing chapters", statusFrom(e));
		}
	}
	
	/**
	 * Wraps the provided exception in a status object.
	 * @param e the exception to wrap
	 * @return a status object
	 */
	public static Status statusFrom(Throwable e) {
		return new Status(IStatus.ERROR, IConstants.PLUGIN_ID, 0, e.toString(), e);
	}
	
	/**
	 * Utility method to handle errors and extract status objects from exceptions. An error dialog
	 * is shown with the title and message provided and possibly with more information from the
	 * status objects.
	 * 
	 * @param shell the parent shell of the error dialog
	 * @param exception the exception to handle
	 * @param title the title of the error dialog
	 * @param message the message to show in the error dialog
	 */
	public static void handleError(Shell shell, Exception exception, String title, String message) {
		IStatus status = null;
		boolean log = false;
		boolean dialog = false;
		Throwable t = exception;
		if (exception instanceof InvocationTargetException) {
			t = ((InvocationTargetException) exception).getTargetException();
			if (t instanceof CoreException) {
				status = ((CoreException) t).getStatus();
				log = true;
				dialog = true;
			} else if (t instanceof InterruptedException) {
				return;
			} else {
				status = new Status(IStatus.ERROR, IConstants.PLUGIN_ID, 1, "Error", t);
				log = true;
				dialog = true;
			}
		}
		if (status == null)
			return;
		if (!status.isOK()) {
			IStatus toShow = status;
			if (status.isMultiStatus()) {
				IStatus[] children = status.getChildren();
				if (children.length == 1) {
					toShow = children[0];
				}
			}
			if (title == null) {
				title = status.getMessage();
			}
			if (message == null) {
				message = status.getMessage();
			}
			if (dialog && shell != null) {
				ErrorDialog.openError(shell, title, message, toShow);
			}
			if (log || shell == null) {
				Platform.getLog(Platform.getBundle(IConstants.PLUGIN_ID)).log(toShow);
			}
		}
	}
}
