package com.googlecode.simpleprofiler.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;

import com.googlecode.simpleprofiler.util.Constant;

public class GlobalConfig {

    //TODO: refactor this class to a config factory

	public static boolean useTimeLimit() {
		return true;
	}

	/**
	 * limit time in milli seconds
	 * 
	 * @return
	 */
	public static long getTimeLimit() {
		return 1;
	}

	public static boolean printTime() {
		return true;
	}

	public static boolean analyazeModel() {
		return true;
	}

	public static boolean analyazeDetailLog() {
		return true;
	}

	public static boolean checkType() {
		return false;
	}

	public static String[] getType() {
		return new String[] { "org.eclipse.jface.action.IAction" };
	}

	public static boolean checkPrivate() {

		return false;
	}

}
