package com.bernard.timetabler.dbinit.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class UserValidationRequest {
    @SerializedName("role")
    private String role;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("userId")
    private String userId;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
