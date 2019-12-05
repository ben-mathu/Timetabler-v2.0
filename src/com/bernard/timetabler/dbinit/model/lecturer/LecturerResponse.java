package com.bernard.timetabler.dbinit.model.lecturer;

import com.google.gson.annotations.SerializedName;

public class LecturerResponse {
	@SerializedName("lecturer")
    private Lecturer lecturer;

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}
