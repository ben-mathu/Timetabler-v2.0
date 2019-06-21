package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class Timetable {
	@SerializedName(Constants.PERIOD)
	private String period;
	@SerializedName(Constants.TIME)
	private String time;
	@SerializedName(Constants.DAY)
	private String day;
	@SerializedName(Constants.UNIT_ID)
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
