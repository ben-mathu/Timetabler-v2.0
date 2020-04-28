package com.bernard.timetabler.dbinit.model.student;

import com.google.gson.annotations.SerializedName;

/**
 * @author com.bernard
 */
public class StudentRequest {
    @SerializedName("student")
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
