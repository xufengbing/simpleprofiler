package com.google.code.t4eclipse.tools.utility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;

import com.google.code.t4eclipse.tools.model.WidgetModel;

public class WidgetModelUtility   {

 
 

	public static WidgetModel[] getWidetLevelModels(Control control) {

		List<WidgetModel> list = new ArrayList<WidgetModel>();

		WidgetModel root = new WidgetModel("Root",
				control.getClass().getName(), control);
		root.parse(list);
		return list.toArray(new WidgetModel[0]);
	}

 

}