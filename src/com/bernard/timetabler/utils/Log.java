package com.bernard.timetabler.utils;

import com.bernard.timetabler.dbinit.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Log {
	private static SimpleDateFormat sf = new SimpleDateFormat(Constants.DATE_FORMAT);

	public static void d(String tag, String message) {
		Date date = new Date();
		System.out.println("T:" + sf.format(date.getTime()) + " D/" + tag + ": " + message);
	}
	
	public static void e(String tag, String error) {
		Date date = new Date();
		System.out.println("T:" + sf.format(date.getTime()) + " E/" + tag + ": " + error);
	}
}
