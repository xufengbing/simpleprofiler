package demo.annotationtype.use;

import java.lang.reflect.*;

import demo.annotationtype.*;

public class Test2 {
	@demo.annotationtype.TestAnno
	public static void m1() {
	}

	public static void m2() {
	}

	@demo.annotationtype.TestAnno
	public static void m3() {
		throw new RuntimeException("Boom");
	}

	public static void m4() {
	}

	@demo.annotationtype.TestAnno
	public static void m5() {
	}

	public static void m6() {
	}

	// WHY the following code also works????
	// TODO: check it
	@TestAnno
	public static void m7() {
		throw new RuntimeException("Crash");
	}

	public static void m8() {
	}

	public static void main(String[] args) throws Exception {
		int passed = 0, failed = 0;
		for (Method m : Test2.class.getMethods()) {
			if (m.isAnnotationPresent(TestAnno.class)) {
				try {
					m.invoke(null);
					passed++;
				} catch (Throwable ex) {
					System.out
							.printf("Test %s failed: %s %n", m, ex.getCause());
					failed++;
				}
			}
		}
		System.out.printf("Passed: %d, Failed %d%n", passed, failed);
	}
}
