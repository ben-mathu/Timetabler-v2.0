package com.bernard.timetabler.dbinit.model.department;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * POJO - for Department objects
 */
public class Department {
	@SerializedName(Constants.DEPARTMENT_ID)
    private String departmentId;
	@SerializedName(Constants.DEPARTMENT_NAME)
    private String departmentName;
	@SerializedName(Constants.FACULTY_ID)
    private String facultyId;

    public Department(String departmentId, String departmentName, String facultyId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.facultyId = facultyId;
    }

    public Department() {
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
}
