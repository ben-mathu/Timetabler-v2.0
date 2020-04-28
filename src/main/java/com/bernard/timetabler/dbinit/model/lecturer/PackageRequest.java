package com.bernard.timetabler.dbinit.model.lecturer;

import com.google.gson.annotations.SerializedName;

public class PackageRequest {
	@SerializedName("package")
	private LecRequest lecRequest;
	
	public void setLecRequest(LecRequest lecRequest) {
		this.lecRequest = lecRequest;
	}
	
	public LecRequest getLecRequest() {
		return lecRequest;
	}
}
