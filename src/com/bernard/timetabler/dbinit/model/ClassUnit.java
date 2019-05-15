package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class ClassUnit {
	@SerializedName(Constants.CLASS_ID)
    private String classId;
	@SerializedName(Constants.UNIT_ID)
    private String unitId;
	@SerializedName(Constants.HALL_ID)
    private String hallId;

    public ClassUnit(String classId, String unitId, String hallId) {
        this.classId = classId;
        this.unitId = unitId;
        this.hallId = hallId;
    }

    public ClassUnit() {
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

	public String getHallId() {
		return hallId;
	}

	public void setHallId(String hallId) {
		this.hallId = hallId;
	}
}
