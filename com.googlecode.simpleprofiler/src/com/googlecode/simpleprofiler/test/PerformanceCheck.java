package com.googlecode.simpleprofiler.test;

import java.util.Vector;

public class PerformanceCheck {

	private static final int THREAD_NUM = 10;
	private static final int ADDED_RECORD_NUM_PER_THREAD = 100000;
	private static final String ADDED_STRING_CONSTANT = "abcxxxxxxx ";

	final static Vector<String> v = new Vector<String>();

	@SuppressWarnings("boxing")
	public static void test() {
		// check memory

		System.gc();
		long start = Runtime.getRuntime().freeMemory();
		System.out.println("\n\nStart With Free Memory:" + start);
		long t1 = System.currentTimeMillis();

		final Vector<Integer> lock = new Vector<Integer>(1);

		for (int i = 0; i < THREAD_NUM; i++) {
			lock.add(0);
		}

		Runnable a = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ADDED_RECORD_NUM_PER_THREAD; i++) {
					PerformanceCheck.v.add(ADDED_STRING_CONSTANT + i);
				}
				synchronized (PerformanceCheck.class) {
					Integer integer = lock.get(0);
					lock.set(0, integer + 1);
				}
			}
		};

		for (int i = 0; i < THREAD_NUM; i++) {
			new Thread(a).start();

		}
		// waiting for all thread to finish
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (PerformanceCheck.class) {
				if (lock.get(0) < THREAD_NUM) {
					continue;
				}

			}

			break;
		}

		System.out.println("used memory: "
				+ (start - Runtime.getRuntime().freeMemory()));
		System.out.println("used time: " + (System.currentTimeMillis() - t1));
		System.out.println("Vector  Size    :" + v.size());
		System.gc();
	}

	public static void main(String[] args) {

		for (int i = 0; i < 100; i++) {
			test();
			// v.clear();
			System.gc();
		}
	}

}