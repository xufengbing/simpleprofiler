package com.googlecode.simpleprofiler.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

public class PropertyUtil {

	
	public static void main(String[] args) {
		//Read properties file.
		Properties properties = new Properties();
		try {
		     properties.load(new FileInputStream("build.property"));
		       Collection<Object> keys = properties.values();
		       
		       for(Object o:keys){
		    	   System.out.println(o);
		       }
		     
		  
		} catch (IOException e) {
		}

	 

	}
}
