package com.bernard.timetabler.dbinit.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UnitList {
	@SerializedName("units")
	private List<Unit> unitList;
	
	public void setUnitList(List<Unit> unitList) {
		this.unitList = unitList;
	}
	
	public List<Unit> getUnitList() {
		return unitList;
	}
}
