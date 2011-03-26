package com.google.code.t4eclipse.tools.eventtrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import com.google.code.t4eclipse.Activator;
import com.google.code.t4eclipse.tools.view.*;


public class Utility {

	public static HashMap<Integer, String> eventMap = new HashMap<Integer, String>();

	static {
		eventMap.put(SWT.Paint, "Paint");
		eventMap.put(SWT.Selection, "Selection");
		eventMap.put(SWT.DefaultSelection, "DefaultSelection");
		eventMap.put(SWT.Dispose, "Dispose");
		eventMap.put(SWT.FocusIn, "FocusIn");
		eventMap.put(SWT.FocusOut, "FocusOut");
		eventMap.put(SWT.Hide, "Hide");
		eventMap.put(SWT.Show, "Show");
		eventMap.put(SWT.KeyDown, "KeyDown");
		eventMap.put(SWT.KeyUp, "KeyUp");
		eventMap.put(SWT.MouseDown, "MouseDown");
		eventMap.put(SWT.MouseUp, "MouseUp");
		eventMap.put(SWT.MouseDoubleClick, "MouseDoubleClick");
		eventMap.put(SWT.MouseMove, "MouseMove");
		eventMap.put(SWT.Resize, "Resize");
		eventMap.put(SWT.Move, "Move");
		eventMap.put(SWT.Close, "Close");
		eventMap.put(SWT.Activate, "Activate");
		eventMap.put(SWT.Deactivate, "Deactivate");
		eventMap.put(SWT.Iconify, "Iconify");
		eventMap.put(SWT.Deiconify, "Deiconify");
		eventMap.put(SWT.Expand, "Expand");
		eventMap.put(SWT.Collapse, "Collapse");
		eventMap.put(SWT.Modify, "Modify");
		eventMap.put(SWT.Verify, "Verify");
		eventMap.put(SWT.Help, "Help");
		eventMap.put(SWT.Arm, "Arm");
		eventMap.put(SWT.MouseExit, "MouseExit");
		eventMap.put(SWT.MouseEnter, "MouseEnter");
		eventMap.put(SWT.MouseHover, "MouseHover");
		eventMap.put(SWT.Traverse, "Traverse");
	}

	public static String getEventType(int type) {
		return eventMap.get(type);
	}

	public static void addDisplayFilter(DisplayListener listener) {
		Display display = Display.getDefault();
		Set<Integer> set = eventMap.keySet();
		Iterator<Integer> iter = set.iterator();
		events.clear();
		while (iter.hasNext()) {
			Integer eventcode = iter.next();

			if (Activator.getDefault().getPreferenceStore().getBoolean(
					eventMap.get(eventcode))) {
				display.addFilter(eventcode, listener);
				events.add(eventcode);

			}
		}
	}

	private static List<Integer> events = new ArrayList<Integer>();

	public static void removeDisplayFilter(DisplayListener listener) {
		Display display = Display.getDefault();

		Iterator<Integer> iter = events.iterator();
		while (iter.hasNext()) {
			Integer eventcode = iter.next();
			display.removeFilter(eventcode, listener);
		}
	}

	public static String getEventString(Event event) {
		if (event != null) {
			if (event.type == SWT.MouseDoubleClick
					|| event.type == SWT.MouseDown || event.type == SWT.MouseUp) {
				return "Event::" + getEventType(event.type) + "button:"
						+ event.button + " mask:" + event.stateMask + " ::: "
						+ event.widget.getClass().getName() + "::::"
						+ event.toString();

			}

			return "Event::" + getEventType(event.type) + " ::: "
					+ event.widget.getClass().getName() + "::::"
					+ event.toString();

		}

		return "\ne event==null";
	}

	public static boolean[] lock = new boolean[1];

	public static void addStrToText(Event event) {
		String str = getEventString(event);
		Text text = MainView.getMainSWT().texts[1];
		String textStr = "\n" + text.getText() + "\n" + str + "\n";
		String removeHeadtestStr = processHead(textStr);
		if (text != null && !text.isDisposed()) {
			text.setText("");
			text.setText(removeHeadtestStr);
		}

	}

	public static String processHead(String textStr) {
		int i = 0;
		byte[] bytes = textStr.getBytes();
		while (i < bytes.length && (bytes[i] == 10 || bytes[i] == 13)) {
			i++;
		}

		if (i == 0)
			return textStr;
		else
			return textStr.substring(i - 1);

	}

	public static boolean filter(Event event) {
		Widget w = event.widget;

		if (w.getData(WidgetData_Exclude) != null)
			return false;
		else if (w instanceof Control) {
			Control c = (Control) w;
			Composite com = c.getParent();
			if (com != null) {
				if (com.getData(WidgetData_Exclude) != null)
					return false;
			}
		}
		return true;
//		if (event.widget instanceof ScrollBar)
//			return false;
//		// /let only ktable
//
//		if (event.widget instanceof KTable)
//			return true;
//
//		if (event.widget instanceof Tree)
//			return true;
//		if (event.widget instanceof TreeItem)
//			return true;
//
//		return false;
	}

	public static final String WidgetData_Exclude = "net.sourceforge.eclipse_testlib_EXCLUDE";

	public static final String WidgetData_Include = "net.sourceforge.eclipse_testlib_INCLUDE";
}
