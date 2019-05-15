package com.bernard.timetabler.utils;

public class Log {
	public static void d(String tag, String message) {
		System.out.println("D/" + tag + ": " + message);
	}
	
	public static void e(String tag, String error) {
		System.out.println("E/" + tag + ": " + error);
	}
}
