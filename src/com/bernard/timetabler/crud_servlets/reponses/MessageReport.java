package com.bernard.timetabler.crud_servlets.reponses;

import com.google.gson.annotations.SerializedName;

public class MessageReport {
	@SerializedName("message")
	private String message;
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
