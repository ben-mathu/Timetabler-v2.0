package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class LecturerProgramme {
	@SerializedName(Constants.LECTURER_ID)
    private String lecturerId;
	@SerializedName(Constants.PROGRAMME_ID)
    private String programmeId;

    public LecturerProgramme(String lecturerId, String programmeId) {
        this.lecturerId = lecturerId;
        this.programmeId = programmeId;
    }

    public LecturerProgramme() {
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }
}
