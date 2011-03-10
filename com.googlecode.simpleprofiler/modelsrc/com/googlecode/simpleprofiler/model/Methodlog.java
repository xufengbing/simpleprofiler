package com.googlecode.simpleprofiler.model;

import java.util.*;

public class Methodlog {

	private String fullName;

	// get from full name when analyze
	private String className;
	private String methodName;

	private List<ExecutionLog> list;
	private int allTime;
	private int execNum;

	public Methodlog(String fullName) {
		this.fullName = fullName;
		this.list = new ArrayList<ExecutionLog>();
	}

	public Methodlog(String fullName, int allTime, int execNum) {
		this.fullName = fullName;
		this.allTime = allTime;
		this.execNum = execNum;
	}

	public void addExecutionLog(long time, int startIndex, int endIndex,
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

	public String getFullMethodName() {
		return fullName;
	}

	public List<ExecutionLog> getList() {
		return list;
	}

	public int getAllTime() {
		return allTime;
	}

	public int getMeanTime() {
		if (execNum != 0)
			return allTime / execNum;
		return 0;
	}

	public int getExecNum() {
		return execNum;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Methodlog) {

				Methodlog m = (Methodlog) obj;
				if (m.fullName.equals(this.fullName)) {
					return true;

				}

			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.fullName.hashCode();
	}
}
