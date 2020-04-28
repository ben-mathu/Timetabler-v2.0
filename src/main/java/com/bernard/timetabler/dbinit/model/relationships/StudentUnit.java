package com.bernard.timetabler.dbinit.model.relationships;

import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

public class StudentUnit {
	@SerializedName(STUDENT_ID)
    private String studentId;
	@SerializedName(UNIT_ID)
    private String unitId;
	@SerializedName(IS_REMOVED)
	private boolean isRemoved = false;

    public StudentUnit(String unitId, String studentId) {
        this.unitId = unitId;
        this.studentId = studentId;
    }

    public StudentUnit() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public void setIsRemoved(boolean isRemoved) {
    	this.isRemoved = isRemoved;
    }
	public boolean isRemoved() {
		return isRemoved;
	}
}
