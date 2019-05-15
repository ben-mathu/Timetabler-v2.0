package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class StudentUnit {
	@SerializedName(Constants.STUDENT_ID)
    private String studentId;
	@SerializedName(Constants.UNIT_ID)
    private String unitId;

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
}
