package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class Admin {
	@SerializedName(Constants.ADMIN_ID)
	private String adminId;
	@SerializedName(Constants.F_NAME)
	private String fName;
	@SerializedName(Constants.M_NAME)
	private String mName;
	@SerializedName(Constants.L_NAME)
	private String lName;
	@SerializedName(Constants.USERNAME)
	private String username;
	@SerializedName(Constants.PASSWORD)
	private String password;
	@SerializedName(Constants.EMAIL)
	private String email;
	
	public Admin(String adminId, String fName, String mName, String lName, String username, String password) {
		this.adminId = adminId;
		this.fName = fName;
		this.mName = mName;
		this.lName = lName;
		this.username = username;
		this.password = password;
	}

	public Admin() {
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
}
