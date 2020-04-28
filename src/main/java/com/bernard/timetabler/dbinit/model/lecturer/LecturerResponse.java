package com.bernard.timetabler.dbinit.model.lecturer;

import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

public class LecturerResponse {
    @SerializedName("lecturer")
    private Lecturer lecturer;
    @SerializedName(TABLE_FACULTIES)
    private Faculty faculty;
    @SerializedName(TABLE_DEPARTMENTS)
    private Department department;
    //	    @SerializedName(TABLE_PROGRAMMES)
//	    private Programme programme;
    @SerializedName("token")
    private String token;

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

//	    public Programme getProgramme() {
//			return programme;
//		}
//
//	    public void setProgramme(Programme programme) {
//			this.programme = programme;
//		}
}
