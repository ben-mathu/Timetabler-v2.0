package com.bernard.timetabler.dbinit.model.campus;

import com.google.gson.annotations.SerializedName;

public class CampusRequest {
    @SerializedName("campus")
    private Campus campus;

    public CampusRequest(Campus campus) {
        this.campus = campus;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
