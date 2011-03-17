package com.googlecode.t4eclipse.model;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;

public class TreeModel {

	private String id;
	private String pid;
	private String label;
	private String imagePath;
	private String plugInID;
	private String classID;
 

	public TreeModel(String id, String pid, String label, String imagePath,
			String plugInID, String classID ) {
		this.id = id;
		this.pid = id;
		this.label = label;
		this.imagePath = imagePath;
		this.plugInID = plugInID;
		this.classID = classID;
 
	}
	// bundle = Platform.getBundle(symbolicName);

	// desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
	// null));
	
	
	public String getId() {
		return id;
	}

	public String getPid() {
		return pid;
	}

	public String getLabel() {
		return label;
	}

	public String getImagePath() {
		return imagePath;
	}
	
	public ImageDescriptor getImage(){
		
		return null;
	}

	public String getPlugInID() {
		return plugInID;
	}

	public String getClassID() {
		return classID;
	}

	public String getBundleID() {
		return this.bundleID;
	}
}
