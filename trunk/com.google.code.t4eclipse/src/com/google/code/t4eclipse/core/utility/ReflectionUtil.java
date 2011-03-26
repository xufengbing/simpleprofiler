package com.google.code.t4eclipse.core.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.widgets.Widget;

public class ReflectionUtil {
	public static ObjectResult invokeMethod(String name, Object o) {
		return invokeMethod(name, o, null);

	}

	public static ObjectResult invokeMethod(String name, Object o, Class c) {

		ObjectResult re = new ObjectResult();
		if (name.equals("wait") || name.equals("notify")
				|| name.equals("notifyAll")) {
			re.forbiddenMethod = true;
			return re;
		}
		Method[] ms;
		if (c == null)
			ms = o.getClass().getDeclaredMethods();
		else
			ms = c.getDeclaredMethods();
		String[] mstrs = new String[ms.length];
		for (int i = 0; i < mstrs.length; i++) {
			mstrs[i] = ms[i].getName();
		}
		Method m = null;
		for (Method tmp : ms) {
			if (tmp.getName().equals(name)) {
				if (tmp.getParameterTypes().length == 0) {
					m = tmp;
					break;
				}

			}
		}
		if (m != null) {
			re.methodOrFieldFound = true;
			try {
				m.setAccessible(true);
				re.result = m.invoke(o, null);
			} catch (Exception e) {
				re.ex = e;
			}

		} else {
			re.methodOrFieldFound = false;
		}
		return re;
	}

	public static ObjectResult getField(String text, Object w) {

		ObjectResult re = new ObjectResult();
		Field field = null;
		Field[] fields = w.getClass().getDeclaredFields();

		for (Field f : fields) {
			if (f.getName() != null && f.getName().equals(text)) {
				field = f;
				break;
			}

		}

		if (field == null) {
			re.methodOrFieldFound = false;

		} else {
			re.methodOrFieldFound = true;
			field.setAccessible(true);
			try {
				re.result = field.get(w);
			} catch (Exception e) {
				re.ex = e;
			}
		}
		return re;
	}

	public static ObjectResult invokeMethodInNewThread(String name,
			final Widget o) {
		final ObjectResult re = new ObjectResult();
		if (name.equals("wait") || name.equals("notify")
				|| name.equals("notifyAll")) {
			re.forbiddenMethod = true;
			return re;
		}

		Method[] ms = o.getClass().getMethods();
		Method m = null;
		for (Method tmp : ms) {
			if (tmp.getName().equals(name)) {
				if (tmp.getParameterTypes().length == 0) {
					m = tmp;
					break;
				}

			}
		}
		if (m != null) {

			final Method[] mm = new Method[] { m };

			re.methodOrFieldFound = true;
			// try {
			// // re.result = m.invoke(o, null);
			//
			// } catch (Exception e) {
			// re.ex = e;
			// }
			AtomicBoolean b = new AtomicBoolean(false);
			NewThread newT = new NewThread(new ExRunnable() {

				public void runEx() throws Exception {

					re.result = mm[0].invoke(o, null);

				}

				public void run() {
					// TODO Auto-generated method stub

				}
			}, b);

			newT.start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (!newT.finished()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			re.ex = newT.getE();

		} else {
			re.methodOrFieldFound = false;
		}
		return re;
	}

	public static class ObjectResult {
		public Object result;
		public boolean methodOrFieldFound;

		public boolean forbiddenMethod;

		public Exception ex;

	}

	public static interface ExRunnable {
		public void runEx() throws Exception;
	}

	public static class NewThread extends Thread {
		private Exception e;

		private ExRunnable r;

		private AtomicBoolean isfinished;

		public NewThread(ExRunnable r, AtomicBoolean b) {
			this.r = r;
			this.isfinished = b;
		}

		public void run() {
			try {
				r.runEx();
			} catch (Exception ex) {
				NewThread.this.e = ex;
			} finally {
				this.isfinished.set(true);
			}

		}

		public boolean finished() {
			return this.isfinished.get();
		}

		public Exception getE() {
			return e;
		}

	}

}
