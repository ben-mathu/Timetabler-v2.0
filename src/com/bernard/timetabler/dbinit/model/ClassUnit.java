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
	@SerializedName(Constants.TIME)
	private String time;
	@SerializedName(Constants.DAY)
	private String day;
	@SerializedName(Constants.PERIOD)
	private String period;

    public ClassUnit(String classId, String unitId, String hallId, String time, String day, String period) {
        this.classId = classId;
        this.unitId = unitId;
        this.hallId = hallId;
        this.time = time;
        this.day = day;
        this.period = period;
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
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
}
