package com.bernard.timetabler.dbinit.model.lecturer;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class LecturerPackageRequest {
    @SerializedName("lecturer")
    private Lecturer lecturer;
    @SerializedName(Constants.TABLE_FACULTIES)
    private Faculty faculty;
    @SerializedName(Constants.TABLE_DEPARTMENTS)
    private Department department;

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}
