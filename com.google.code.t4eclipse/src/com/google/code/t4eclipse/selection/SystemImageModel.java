package com.google.code.t4eclipse.selection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class SystemImageModel {

	private String name;

	public SystemImageModel(String n) {
		this.name = n;
	}

	public Image getImage() {

		return PlatformUI.getWorkbench().getSharedImages().getImage(name);
	}

	public String getCode() {
		return "PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages."
				+ name + ");";
	}

	public static SystemImageModel[] getAllModels() {
		List<SystemImageModel> list = new ArrayList<SystemImageModel>();

		Field[] fields = ISharedImages.class.getFields();
		for (Field f : fields) {
			list.add(new SystemImageModel(f.getName()));
		}

		return list.toArray(new SystemImageModel[0]);
	}

	
	public String getName(){
		return this.name;
	}
}
