package com.bernard.timetabler.dbinit.model;

import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

public class SaltWithRole {
	@SerializedName(ROLE)
	private String role;
	@SerializedName(SALT)
	private String salt;
	
	public SaltWithRole(String role, byte[] salt) {
		this.setRole(role);
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
