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

public class ProjectConfig {
	private boolean projectConfigUsed;
	private List<String> classNameInclude;
	private List<String> classNameExclude;
	private List<String> methodNameInclude;
	private List<String> methodNameExclude;

	public ProjectConfig(IJavaProject project) {
		this.classNameInclude = new ArrayList<String>();
		this.classNameExclude = new ArrayList<String>();
		this.methodNameInclude = new ArrayList<String>();
		this.methodNameExclude = new ArrayList<String>();

		Properties properties = new Properties();
		try {
			IFile configFile = project.getProject().getFile(
					Constant.DEFAULT_FILE_NAME);
			File file = new File(configFile.getLocationURI());
			properties.load(new FileReader(file));
			Collection<Object> keys = properties.values();

			for (Object o : keys) {

				String key = ((String) o).trim();
				if (Constant.USE_LOCAL.equals(key)) {
					String value = properties.getProperty(key);
					if (value != null
							&& value.trim().toLowerCase().equals("true")) {
						projectConfigUsed = true;
						continue;

					} else {
						projectConfigUsed = false;
						break;
					}
				}

				process(properties, key);

			}
		} catch (Exception e) {
			projectConfigUsed = false;
		}

	}

	public boolean isUsed() {
		return projectConfigUsed;
	}

	public List<String> getClassNameInclude() {
		return classNameInclude;
	}

	public List<String> getClassNameExclude() {
		return classNameExclude;
	}

	public List<String> getMethodNameInclude() {
		return methodNameInclude;
	}

	public List<String> getMethodNameExclude() {
		return methodNameExclude;
	}

	private void process(Properties properties, String key) {

		String value = properties.getProperty(key);
		if (value != null) {
			value = value.trim();
			if (!value.isEmpty()) {
				// catch the runtime exception to continue
				try {
					if (Constant.CLASS_NAME_EXCLUDE.equals(key)) {
						addContents(this.classNameExclude, value);
					}
					if (Constant.CLASS_NAME_INCLUDE.equals(key)) {
						addContents(this.classNameInclude, value);
					}
					if (Constant.METHOD_NAME_EXCLUDE.equals(key)) {
						addContents(this.methodNameExclude, value);
					}

					if (Constant.METHOD_NAME_INCLUDE.equals(key)) {
						addContents(this.methodNameInclude, value);
					}
				} catch (PatternSyntaxException e) {
					// DO nothing
				}
			}

		}

	}

	private void addContents(List<String> list, String value) {
		String[] values = value.split(":");
		// each string should only contain
		// startsWith, endsWith, contains
		for (int i = 0; i < values.length; i++) {
			String tmp = values[i].trim();
			if (tmp.length() > 0) {
				list.add(tmp);
			}
		}

	}

}
