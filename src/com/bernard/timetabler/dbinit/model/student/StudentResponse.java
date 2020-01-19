package com.bernard.timetabler.dbinit.model.student;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.campus.Campus;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.bernard.timetabler.dbinit.model.programme.Programme;
import com.google.gson.annotations.SerializedName;

public class StudentResponse {
    @SerializedName("student")
    private Student student;
    @SerializedName(Constants.TABLE_DEPARTMENTS)
    private Department department;
    @SerializedName(Constants.TABLE_CAMPUS)
    private Campus campus;
    @SerializedName(Constants.TABLE_FACULTIES)
    private Faculty faculty;
    @SerializedName(Constants.TABLE_PROGRAMMES)
    private Programme programme;
    @SerializedName("token")
    private String token;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
