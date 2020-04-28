package com.bernard.timetabler.dbinit.model.student;

import com.bernard.timetabler.dbinit.model.campus.Campus;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.bernard.timetabler.dbinit.model.programme.Programme;
import com.google.gson.annotations.SerializedName;

/**
 * @author com.bernard
 */
public class StudentPackageResponse {
    @SerializedName("student")
    private Student student;
    @SerializedName("faculty")
    private Faculty faculty;
    @SerializedName("department")
    private Department department;
    @SerializedName("campus")
    private Campus campus;
    @SerializedName("programme")
    private Programme programme;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }
}
