package com.bernard.timetabler.dbinit.model.course;

import com.google.gson.annotations.SerializedName;

public class UnitReq {
    @SerializedName("unit")
    private Unit unit;
    @SerializedName("passcode")
    private String passCode;

    public UnitReq(Unit unit, String passCode) {
        this.unit = unit;
        this.passCode = passCode;
    }
    
    public UnitReq() {}
    
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }
    
    public String getPassCode() {
		return passCode;
	}
    
    public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
}
