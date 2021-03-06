package com.bernard.timetabler.dbinit.model.hall;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class Hall {
	@SerializedName(Constants.HALL_ID)
    private String hallId;
	@SerializedName(Constants.HALL_NAME)
    private String hallName;
	@SerializedName(Constants.FACULTY_ID)
    private String facultyId;
	@SerializedName(Constants.IS_REMOVED)
	private boolean isRemoved = false;

    public Hall(String hallId, String hallName, String facultyId) {
        this.hallId = hallId;
        this.hallName = hallName;
        this.facultyId = facultyId;
    }

    public Hall() {
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
    
    public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
    
    public boolean isRemoved() {
		return isRemoved;
	}
}
