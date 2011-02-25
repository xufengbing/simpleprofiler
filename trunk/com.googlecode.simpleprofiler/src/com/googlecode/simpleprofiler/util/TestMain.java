package com.googlecode.simpleprofiler.util;

import java.io.File;

import com.googlecode.simpleprofiler.Activator;

public class TestMain {
	public static void main(String[] args) {

		File a = new File(
				"/D:/svn/simpleprofiler/com.googlecode.simpleprofiler/com.googlecode.simpleprofiler.model.jar");
		System.out.println(a.exists());
		
		
		String bundleLocation = "reference:file:dropins/com.googlecode.simpleprofiler_1.0.0//com.googlecode.simpleprofiler.model.jar";
		String prefix = "reference:file:";
		if(bundleLocation.startsWith(prefix)){
			bundleLocation=bundleLocation.substring(prefix.length());
			System.out.println(bundleLocation);
		}
	}
}
