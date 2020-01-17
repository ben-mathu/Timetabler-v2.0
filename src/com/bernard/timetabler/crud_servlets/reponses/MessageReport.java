package com.bernard.timetabler.crud_servlets.reponses;

import com.google.gson.annotations.SerializedName;

public class MessageReport {
	@SerializedName("status")
	private int status;
	@SerializedName("message")
	private String message;
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
