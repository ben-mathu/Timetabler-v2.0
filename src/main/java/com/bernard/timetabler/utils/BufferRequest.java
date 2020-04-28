package com.bernard.timetabler.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

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
