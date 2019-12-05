package com.bernard.timetabler.dbinit.model.lecturer;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class LecRequest {
	@SerializedName("message")
	private String message;
    @SerializedName("email")
    private String email;
    @SerializedName(Constants.F_NAME)
    private String fname;
    @SerializedName(Constants.M_NAME)
    private String mname;
    @SerializedName(Constants.L_NAME)
    private String lname;
    @SerializedName("access_code")
    private String code;

    public void setMessage(String message) {
		this.message = message;
	}
    
    public String getMessage() {
		return message;
	}
    
    public void setEmail(String email) {
		this.email = email;
	}
    
    public String getEmail() {
        return email;
    }

    public void setFname(String fname) {
		this.fname = fname;
	}
    
    public String getFname() {
        return fname;
    }

    public void setLname(String lname) {
		this.lname = lname;
	}
    
    public String getLname() {
        return lname;
    }
    
    public void setMname(String mname) {
		this.mname = mname;
	}

    public String getMname() {
        return mname;
    }
    
    public void setCode(String code) {
		this.code = code;
	}
    
    public String getCode() {
		return code;
	}
}
