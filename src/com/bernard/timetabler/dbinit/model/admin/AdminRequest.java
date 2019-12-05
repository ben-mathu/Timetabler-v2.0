package com.bernard.timetabler.dbinit.model.admin;

import com.google.gson.annotations.SerializedName;

public class AdminRequest {
	@SerializedName("admin")
	private Admin admin;
	@SerializedName("password")
	private String dbPassword;
	
	public Admin getAdmin() {
		return admin;
	}
	
	@SuppressWarnings("unused")
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}
	
	@SuppressWarnings("unused")
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
}
