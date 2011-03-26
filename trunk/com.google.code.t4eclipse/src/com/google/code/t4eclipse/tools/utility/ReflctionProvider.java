package com.google.code.t4eclipse.tools.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Control;

import com.google.code.t4eclipse.core.utility.ExceptionUtility;

public class ReflctionProvider {
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

	public static String[] getMethods(Object o) {
		List<String> ms = new ArrayList<String>();

		ms.add("---Declared Methods---");

		Method[] methods = o.getClass().getDeclaredMethods();
		for (Method m : methods) {
			if (m.getParameterTypes().length == 0) {
				ms.add(m.getName());
			} else {
				// System.out.println("else condition");
			}
		}
		ms.add("---Methods---");

		Method[] methods1 = o.getClass().getMethods();
		for (Method m : methods1) {
			if (m.getParameterTypes().length == 0) {
				ms.add(m.getName());
			} else {
				// System.out.println("else condition 1");
			}
		}
		return ms.toArray(new String[0]);
	}

	public static String[] getFields(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		Field[] fields1 = o.getClass().getFields();
		List<String> fs = new ArrayList<String>();

		fs.add("---Declared Fields----");
		for (Field f : fields) {
			fs.add(f.getName());
		}

		fs.add("---Fields---");
		for (Field f : fields1) {
			fs.add(f.getName());
		}

		return fs.toArray(new String[0]);
	}

	// run method result
	public static String getMethodResultASStr(Object data,
			String methodComboText) {

		if (methodComboText.trim().length() == 0
				|| methodComboText.contains("---")) {
			return "ERROR: please select/set a method first!";
		}
		if (data == null) {
			return "ERROR: object to be analysised is null!";
		}

		if (data instanceof Control) {
			if (((Control) data).isDisposed()) {
				return "ERROR: the control to be analysis is disposed!";
			}
		}
		Method m;
		try {
			m = data.getClass().getDeclaredMethod(methodComboText);
		} catch (Exception e) {
			m = null;
		}
		if (m == null) {
			try {
				m = data.getClass().getMethod(methodComboText);
			} catch (Exception e) {
				m = null;
			}
		}

		if (m == null) {
			return "ERROR: can not find this method in this object!";
		} else {
			boolean returnnull = false;
			try {
				m.setAccessible(true);
				returnnull = checkReturnVoid(m);
				Object result = m.invoke(data, null);
				if (returnnull == true) {
					return "INFO:this method returns void and is sucessfully invoked !";
				} else

					return ObjectStringMapUtility.getStringDescription(result,
							"Method:" + m.getName());
			} catch (Exception e) {
				String start = "ERROR: exception happened when running this method !\n";
				String exceptionCause = ExceptionUtility.getErrorMessage(e);

				return start + exceptionCause;
			}
		}

	}

	private static boolean checkReturnVoid(Method m) {
		return (m.getReturnType() == Void.TYPE);
	}

	// get field str
	public static String getFieldContentAsStr(Object data, String fieldComboText) {
		if (fieldComboText.trim().length() == 0
				|| fieldComboText.contains("---")) {
			return "ERROR: please select/set a field first!";
		}
		if (data == null) {
			return "ERROR: object to be analysised is null!";
		}

		if (data instanceof Control) {
			if (((Control) data).isDisposed()) {
				return "ERROR: the control to be analysis is disposed!";
			}
		}
		Field f;
		try {
			f = data.getClass().getDeclaredField(fieldComboText);
		} catch (Exception e) {
			f = null;
		}
		if (f == null) {
			try {
				f = data.getClass().getField(fieldComboText);
			} catch (Exception e) {
				f = null;
			}
		}

		if (f == null) {
			return "ERROR: can not find this field in this object!";
		} else {
			try {
				f.setAccessible(true);
				Object result = f.get(data);
				return ObjectStringMapUtility.getStringDescription(result,
						"Field:" + f.getName());
			} catch (Exception e) {
				String start = "ERROR: exception happened to get the field!\n";
				String exceptionCause = ExceptionUtility.getErrorMessage(e);
				return start + exceptionCause;
			}
		}

		// return "get field";
	}

}
