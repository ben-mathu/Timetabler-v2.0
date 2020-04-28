package com.bernard.timetabler.dbinit.model.timetable;

import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

public class Timetable {
	@SerializedName(PERIOD)
	private String period;
	@SerializedName(TIME)
	private String time;
	@SerializedName(DAY)
	private String day;
	@SerializedName(UNIT_ID)
	private String unitId;
	
	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getUnitId() {
		return unitId;
	}
	
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
