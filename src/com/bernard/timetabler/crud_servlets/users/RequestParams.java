package com.bernard.timetabler.crud_servlets.users;

import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class RequestParams {
    @SerializedName("name")
    private final String name;
    @SerializedName("userId")
    private final String userId;
    @SerializedName("role")
    private final String role;

    public RequestParams(String name, String userId, String role) {
        this.name = name;
        this.userId = userId;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
