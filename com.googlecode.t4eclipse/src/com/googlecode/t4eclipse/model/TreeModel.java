package com.googlecode.t4eclipse.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;

public class TreeModel {

	private String id;
	private String pid;
	private String label;
	private String imagePath;
	private String plugInID;
	private String classID;

	private TreeModel parent;

	private List<TreeModel> children;

	public void print() {
		System.out.println("ID:" + id + " Pid:" + pid + " label:" + label
				+ " imagepath:" + imagePath + " :pluginID:" + plugInID
				+ " classID" + classID);
		System.out.println("parent:" + this.parent);
		System.out.println("child:" + this.children.size());

	}

	public TreeModel(String id, String pid, String label, String imagePath,
			String plugInID, String classID) {
		this.id = id;
		this.pid = pid;
		this.label = label;
		this.imagePath = imagePath;
		this.plugInID = plugInID;
		this.classID = classID;
		this.children = new ArrayList<TreeModel>();

	}

	public void setParent(TreeModel p) {
		this.parent = p;

	}

	public void addChildren(TreeModel p) {
		this.children.add(p);

	}

	// bundle = Platform.getBundle(symbolicName);

	// desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
	// null));

	public TreeModel getParent() {
		return parent;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getPid() {
		return pid;
	}

	public boolean hasImage() {
		return imagePath == null;
	}

	public ImageDescriptor getImage() {
		ImageDescriptor image = null;
		if (this.imagePath != null) {
			Bundle bundle = Platform.getBundle(plugInID);
			Path path = new Path(this.imagePath);
			image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
					path, null));
		}
		return image;
	}

	public String getPlugInID() {
		return plugInID;
	}

	public String getClassID() {
		return classID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof TreeModel) {
				TreeModel t = (TreeModel) obj;
				return t.id.equals(this.id);
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public String toString() {
		String ts = "ID:" + id + " \nPid:" + pid + " \nLabel:" + label
				+ " \nImagepath:" + imagePath + " \nPluginID:" + plugInID
				+ " \nClassID" + classID;
		ts += "\nHasParent:" + this.parent != null;
		ts += "\nChild:" + this.children.size();
		return ts;

	}

}
