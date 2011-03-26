package com.google.code.t4eclipse.tools.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public class WidgetModel {

	private Control rootControl;

	public Control getRootControl() {
		return this.rootControl;
	}

	public void setRootControl(Control control) {
		this.rootControl = control;
	}

	private String nodeStr = "";

	private String typeName = "N/A";

	private String level = "";

	public void setLevel(String l) {
		this.level = l;
	}

	public void addLevel() {
		level += "+";

	}

	public String getLevel() {
		return this.level;
	}

	private Widget widget;

	private List<WidgetModel> childs = new ArrayList<WidgetModel>();

	public void addChild(WidgetModel child) {
		childs.add(child);
	}

	public List<WidgetModel> getChilds() {
		return childs;
	}

	public int getChildCount() {
		return childs.size();
	}

	public WidgetModel() {
		super();
	}

	public WidgetModel(String nodeStr, String typeName, Widget control) {
		super();
		this.nodeStr = nodeStr;
		this.typeName = typeName;
		this.widget = control;
	}

	public WidgetModel(String nodeStr, String typeName) {
		super();
		this.nodeStr = nodeStr;
		this.typeName = typeName;
	}

	public void setNodeStr(String nodeStr) {
		this.nodeStr = nodeStr;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void parse(List<WidgetModel> list) {
		list.add(this);
		if (this.widget instanceof Composite) {
			Composite c = (Composite) this.widget;
			Control[] controls = c.getChildren();
			for (int i = 0; i < controls.length; i++) {
				WidgetModel m = new WidgetModel("", controls[i].getClass()
						.getName(), controls[i]);
				m.setLevel(this.getLevel());
				m.setRootControl(this.getRootControl());
				m.addLevel();
				this.childs.add(m);
				m.parse(list);
			}

		}
	}

	/**
	 * { "Node", "ClassName", "Type", "isVisiable", "isDisposed", "isEnabled",
	 * "isFocusControl" };
	 *
	 */
	public String getClassName() {
		return typeName;
	}

	public String getNodeStr() {
		return nodeStr;
	}

	public String getType() {
		if (this.widget == null) {
			return "N/A";
		}

		if (this.widget instanceof Composite)
			return Composite.class.getSimpleName();
		if (this.widget instanceof Control)
			return Control.class.getSimpleName();
		/**
		 * TODO: other special type?
		 */
		return "N/A";
	}

	public String getVisiableStr() {

		if (this.widget == null) {
			return "N/A";
		}

		if (this.widget instanceof Control) {
			Control control = (Control) this.widget;
			return   Boolean.valueOf(control.isVisible()).toString();
		}
		return "N/A";
	}

	public String getDisposedStr() {

		if (this.widget == null) {
			return "N/A";
		}

		return   Boolean.valueOf(this.widget.isDisposed()).toString();
	}

	public String getFocusControlStr() {
		if (this.widget == null) {
			return "N/A";
		}

		if (this.widget instanceof Control) {
			Control control = (Control) this.widget;
			if (control.isFocusControl())
				return   Boolean.valueOf(control.isFocusControl()).toString();
			else
				return "";
		}
		return "N/A";
	}

	public Widget getWidget() {
		return this.widget;
	}

	public String getText() {
		Object o = invokeMethod("getText", this.widget);
		if (o == null)
			return "N/A";
		else if (o instanceof String)
			return o.toString();
		else
			return "N/A(str)";
	}

	// the flollowing are getter methods
	// public String

	public String toString() {
		return "";
	}

	public static Object invokeMethod(String name, Object o) {
		Object returnObject = null;
		try {
			Method m = o.getClass().getDeclaredMethod(name);
			m.setAccessible(true);
			returnObject = m.invoke(o);

		} catch (Exception e) {
			return null;
		}
		return returnObject;

	}

}
