package com.bernard.timetabler.dbinit.model.admin;

import com.google.gson.annotations.SerializedName;

public class AdminResponse {
	@SerializedName("admin")
    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
