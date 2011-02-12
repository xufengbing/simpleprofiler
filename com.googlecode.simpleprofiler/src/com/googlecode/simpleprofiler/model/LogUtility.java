package com.googlecode.simpleprofiler.model;

import java.util.HashMap;
import java.util.Map;

public class LogUtility {
	private Map<String, Methodlog> map;
	private static int index = 0;

	private LogUtility() {
		this.map = new HashMap<String, Methodlog>();
	}

	private static LogUtility logUtility = new LogUtility();

	public static LogUtility getDefault() {
		return logUtility;
	}

	public synchronized static int getIndex() {
		index = index + 1;
		return index;
	}

	public synchronized void addLog(String className, String methodName,
			int time, int startIndex, int endIndex, long threadID) {
		String id = className + ":" + methodName;
		Methodlog methodlog = map.get(id);
		if (methodlog == null) {
			methodlog = new Methodlog(className, methodName);
			map.put(id, methodlog);
		}
		methodlog.addExecutionLog(time, startIndex, endIndex, threadID);
	}
}
