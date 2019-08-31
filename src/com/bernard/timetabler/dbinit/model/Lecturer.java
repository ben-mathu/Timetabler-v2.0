package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class Lecturer {
	@SerializedName(Constants.LECTURER_ID)
    private String id;
	@SerializedName(Constants.F_NAME)
    private String firstName;
	@SerializedName(Constants.L_NAME)
    private String lastName;
	@SerializedName(Constants.M_NAME)
    private String middleName;
	@SerializedName(Constants.USERNAME)
    private String username;
	@SerializedName(Constants.PASSWORD)
    private String password;
	@SerializedName(Constants.FACULTY_ID)
    private String facultyId;
//	@SerializedName(Constants.PROGRAMME_ID)
//    private String programmeId;
	@SerializedName(Constants.DEPARTMENT_ID)
    private String departmentId;
	@SerializedName(Constants.IN_SESSION)
    private boolean inSesson;

    public Lecturer(String id, String firstName,
    		String lastName, String middleName,
    		String username, String password,
    		String facultyId, String programmeId,
    		String departmentId, boolean inSession) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.username = username;
        this.password = password;
        this.facultyId = facultyId;
        this.departmentId = departmentId;
        this.inSesson = inSession;
    }

    public Lecturer() {
        inSesson = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public void setUsername(String username) {
		this.username = username;
	}
    
    public String getUsername() {
		return username;
	}
    
    public void setPassword(String password) {
		this.password = password;
	}
    
    public String getPassword() {
		return password;
	}

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isInSesson() {
        return inSesson;
    }

    public void setInSession(boolean inSesson) {
        this.inSesson = inSesson;
    }

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

//	public String getProgrammeId() {
//		return programmeId;
//	}
//
//	public void setProgrammeId(String programmeId) {
//		this.programmeId = programmeId;
//	}
}
