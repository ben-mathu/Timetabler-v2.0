package com.bernard.timetabler.dbinit.model.hall;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.bernard.timetabler.dbinit.Constants.TABLE_HALLS;

/**
 * @author com.bernard
 */
public class HallsDao {
    @SerializedName(TABLE_HALLS)
    private List<Hall> hallList;

    public List<Hall> getHallList() {
        return hallList;
    }

    public void setHallList(List<Hall> hallList) {
        this.hallList = hallList;
    }
}
