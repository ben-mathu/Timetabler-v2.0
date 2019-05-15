package com.bernard.timetabler.crud_servlets.reponses;

import com.google.gson.annotations.SerializedName;

public class ErrorReport {
	@SerializedName("message")
	private String errorMessage;
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
