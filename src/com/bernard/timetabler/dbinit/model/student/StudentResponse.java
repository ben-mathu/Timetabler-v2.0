package com.bernard.timetabler.dbinit.model.student;

import com.google.gson.annotations.SerializedName;

public class StudentResponse {
	@SerializedName("student")
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
