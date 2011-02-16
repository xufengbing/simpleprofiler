package com.googlecode.simpleprofiler.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;

import com.googlecode.simpleprofiler.util.Constant;

public class ProjectConfig {
	private boolean projectConfigUsed;
	private Pattern classNameInclude;
	private Pattern classNameExclude;
	private Pattern methodNameInclude;
	private Pattern methodNameExclude;

	public ProjectConfig(IJavaProject project) {

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

	public Pattern getClassNameInclude() {
		return classNameInclude;
	}

	public Pattern getClassNameExclude() {
		return classNameExclude;
	}

	public Pattern getMethodNameInclude() {
		return methodNameInclude;
	}

	public Pattern getMethodNameExclude() {
		return methodNameExclude;
	}

	private void process(Properties properties, String key) {

		String value = properties.getProperty(key);
		if (value != null) {
			String trim = value.trim();
			if (!trim.isEmpty()) {
				// catch the runtime exception to continue
				try {
					Pattern pattern = Pattern.compile(trim);
					if (Constant.CLASS_NAME_EXCLUDE.equals(key)) {
						classNameExclude = pattern;
					}
					if (Constant.CLASS_NAME_INCLUDE.equals(key)) {
						classNameInclude = pattern;
					}
					if (Constant.METHOD_NAME_EXCLUDE.equals(key)) {
						methodNameExclude = pattern;
					}

					if (Constant.METHOD_NAME_INCLUDE.equals(key)) {
						methodNameInclude = pattern;
					}
				} catch (PatternSyntaxException e) {
					//DO nothing
				}
			}

		}

	}

}
