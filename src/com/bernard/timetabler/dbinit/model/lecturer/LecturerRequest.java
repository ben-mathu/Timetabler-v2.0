package com.bernard.timetabler.dbinit.model.lecturer;

import com.google.gson.annotations.SerializedName;

public class LecturerRequest {
	@SerializedName("lecturer")
	private Lecturer lecturer;
	@SerializedName("password")
	private String password;
	
	public LecturerRequest() {
	}
	
	public void setLecturer(Lecturer lecturer) {
		this.lecturer = lecturer;
	}
	
	public Lecturer getLecturer() {
		return lecturer;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
}
