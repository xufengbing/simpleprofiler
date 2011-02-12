package com.googlecode.simpleprofiler.model;

import java.util.*;

public class Methodlog {
	private String className;
	// with para if needed
	private String methodName;
	private List<ExecutionLog> list;
	private int allTime;
	private int execNum;

	public Methodlog(String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
		this.list = new ArrayList<ExecutionLog>();
	}

	public void addExecutionLog(int time, int startIndex, int endIndex,
			long threadID) {
		this.allTime += time;
		this.execNum += 1;
		String detailOn = System
				.getProperty("com.googlecode.simplejavaprofiler.logdetail");
		// if the property is not set, by default log detail info
		// if the property is set to "true" or "on", also log
		// else, not log detail
		if (detailOn == null
				|| (detailOn.toLowerCase().equals("true") || (detailOn
						.toLowerCase().equals("on")))) {
			ExecutionLog log = new ExecutionLog(time, startIndex, endIndex,
					threadID);
			this.list.add(log);

		}

	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<ExecutionLog> getList() {
		return list;
	}

	public int getAllTime() {
		return allTime;
	}

	public int getExecNum() {
		return execNum;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Methodlog) {

				Methodlog m = (Methodlog) obj;
				if ((m.className.equals(this.className))
						&& (m.methodName.equals(this.methodName))) {
					return true;

				}

			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		String all = className + ":" + methodName;
		return all.hashCode();
	}
}
