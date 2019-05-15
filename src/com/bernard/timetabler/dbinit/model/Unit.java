package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class Unit {
	@SerializedName(Constants.UNIT_ID)
    private String id;
	@SerializedName(Constants.UNIT_NAME)
    private String unitName;
	@SerializedName(Constants.PROGRAMME_ID)
    private String programmeId;
	@SerializedName(Constants.FACULTY_ID)
    private String facultyId;
	@SerializedName(Constants.IS_PRACTICAL)
    private boolean isPractical = false;

    public Unit(String id, String unitName, String programmeId, String facultyId, boolean isPractical) {
        this.id = id;
        this.unitName = unitName;
        this.programmeId = programmeId;
        this.facultyId = facultyId;
        this.isPractical = isPractical;
    }

    public Unit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public boolean isPractical() {
        return isPractical;
    }

    public void setPractical(boolean practical) {
        isPractical = practical;
    }
}
