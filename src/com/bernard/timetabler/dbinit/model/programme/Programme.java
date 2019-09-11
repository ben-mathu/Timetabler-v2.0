package com.bernard.timetabler.dbinit.model.programme;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * POJO - for Programme objects
 */
public class Programme {
	@SerializedName(Constants.PROGRAMME_ID)
    private String programmeId;
	@SerializedName(Constants.PROGRAMME_NAME)
    private String programmeName;
	@SerializedName(Constants.DEPARTMENT_ID)
    private String departmentId;
	@SerializedName(Constants.FACULTY_ID)
    private String facultyId;

    public Programme(String programmeId, String programmeName, String departmentId, String facultyId) {
        this.programmeId = programmeId;
        this.programmeName = programmeName;
        this.departmentId = departmentId;
        this.facultyId = facultyId;
    }

    public Programme() {
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
}
