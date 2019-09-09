package com.bernard.timetabler.dbinit.model;

import com.google.gson.annotations.SerializedName;

public class ProgrammeRequest {
    @SerializedName("programme")
    private Programme programme;

    public ProgrammeRequest(Programme programme) {
        this.programme = programme;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }
}
