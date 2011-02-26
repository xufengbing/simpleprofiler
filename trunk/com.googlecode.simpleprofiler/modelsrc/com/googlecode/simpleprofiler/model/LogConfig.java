package com.googlecode.simpleprofiler.model;

/**
 *
 * This class will be invoked from UI to configure how the LogUtility logs info
 *
 * @author BEN.XU
 *
 */
public class LogConfig {

	private static LogConfig cf = new LogConfig();

	private LogConfig() {

	}

	public static LogConfig getDefault() {
		return cf;
	}

	/**
	 * whether to log, when set to false,nothing will be logged.
	 */
	private boolean logInfo;

	private boolean logMethodStart;


	public void setLogInfo(boolean loginfo){
		this.logInfo=loginfo;
	}

	public boolean getLogInfo(){
		return this.logInfo;
	}


//	private boolean log


}
