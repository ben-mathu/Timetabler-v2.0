package com.bernard.timetabler.dbinit.model.faculty;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * POJO - for faculty objects
 */
public class Faculty {
	@SerializedName(Constants.FACULTY_ID)
    private String facultyId;
	@SerializedName(Constants.FACULTY_NAME)
    private String facultyName;
	@SerializedName(Constants.CAMPUS_ID)
    private String campusId;
	@SerializedName(Constants.IS_REMOVED)
    private boolean isRemoved = false;

    public Faculty(String facultyId, String facultyName, String campusId, boolean isRemoved) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.campusId = campusId;
        this.isRemoved = isRemoved;
    }

    public Faculty() {
        // intentionally left blank
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }
    
    public boolean isRemoved() {
		return isRemoved;
	}
    
    public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
}
