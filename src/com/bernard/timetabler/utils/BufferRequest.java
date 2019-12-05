package com.bernard.timetabler.utils;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

public class BufferRequest {
	public static String bufferRequest(HttpServletRequest req) {
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		} catch (Exception e) {
			Log.e(BufferRequest.class.getSimpleName(), "Error" + e.getMessage());
		}
		
		return strBuffer.toString();
	}
}
