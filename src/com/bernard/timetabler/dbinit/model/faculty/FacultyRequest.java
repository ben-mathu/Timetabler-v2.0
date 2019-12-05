package com.bernard.timetabler.dbinit.model.faculty;

import com.google.gson.annotations.SerializedName;

public class FacultyRequest {
    @SerializedName("faculty")
    private Faculty faculty;

    public FacultyRequest(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Faculty getFaculty() {
        return faculty;
    }
}
