package com.googlecode.simpleprofiler.model;

public class ExecutionLog {
	private int startIndex;
	private int endIndex;
	private long threadID;
	private long executionTime;

	public ExecutionLog(long time, int startIndex, int endIndex, long threadID) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.executionTime = time;
		this.threadID = threadID;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public long getThreadID() {
		return threadID;
	}

	public long getExecutionTime() {
		return executionTime;
	}

}
