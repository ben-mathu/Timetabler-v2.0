package com.bernard.timetabler.dbinit.model.hall;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.hall.Hall;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author bernard
 */
public class HallsDao {
    @SerializedName(Constants.TABLE_HALLS)
    private List<Hall> hallList;

    public List<Hall> getHallList() {
        return hallList;
    }

    public void setHallList(List<Hall> hallList) {
        this.hallList = hallList;
    }
}
