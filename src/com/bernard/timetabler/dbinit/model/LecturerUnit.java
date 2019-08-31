package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class LecturerUnit {
	@SerializedName(Constants.LECTURER_ID)
    private String lecturerId;
	@SerializedName(Constants.UNIT_ID)
    private String unitId;
	@SerializedName(Constants.IS_REMOVED)
	private boolean isRemoved = false;

    public LecturerUnit(String lecturerId, String unitId) {
        this.lecturerId = lecturerId;
        this.unitId = unitId;
    }

    public LecturerUnit() {
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    
    public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
    
    public boolean isRemoved() {
		return isRemoved;
	}
}
