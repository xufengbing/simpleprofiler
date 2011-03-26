package com.google.code.t4eclipse.core.utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtility {
	public static String getErrorMessage(Exception ex) {
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		String returnStr=new String( stringWriter.getBuffer());
		stringWriter.flush();
		try {
			stringWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnStr;
	}
}
