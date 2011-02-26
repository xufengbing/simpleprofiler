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

	public synchronized int getIndex() {
		System.out.println("getIndex:" + index);
		index = index + 1;
		return index;
	}

	public synchronized int getMapSize() {
		return map.values().size();
	}

	public synchronized void addLog(String fullName, long time, int startIndex,
			int endIndex, long threadID) {

		Methodlog methodlog = map.get(fullName);
		if (methodlog == null) {
			methodlog = new Methodlog(fullName);
			map.put(fullName, methodlog);
		}
		methodlog.addExecutionLog(time, startIndex, endIndex, threadID);
	}

	public synchronized void clearAll() {
		boolean willLog = LogConfig.getDefault().getLogInfo();
		if (willLog) {
			LogConfig.getDefault().setLogInfo(false);
			this.map.clear();
			LogConfig.getDefault().setLogInfo(true);
		} else {
			this.map.clear();
		}
	}

}
