package com.bernard.timetabler.dbinit.model.admin;

import com.google.gson.annotations.SerializedName;

public class AdminResponse {
	@SerializedName("admin")
    private Admin admin;
	@SerializedName("token")
    private String token;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
