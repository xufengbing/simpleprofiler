package com.googlecode.simpleprofiler.test;

public class TestTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			checkTimeInNano();
			checkTimeInMilli();
		}

		
		
	}

	private static void checkTimeInNano() {
		long start = System.nanoTime();
		runTime();
		long end = System.nanoTime();
		System.out.println("used time in nano seconds:" + (end - start));

	}

	private static void checkTimeInMilli() {
		long start = System.currentTimeMillis();
		runTime();
		long end = System.currentTimeMillis();
		System.out.println("used time in milli seconds:" + (end - start));

	}
		
	

	private static void runTime() {
		long j = 0;
		for (int i = 0; i < 1; i++) {
 	j += (1.123412412+i) / 12.32412413513523;
 		
		}

	}

}
