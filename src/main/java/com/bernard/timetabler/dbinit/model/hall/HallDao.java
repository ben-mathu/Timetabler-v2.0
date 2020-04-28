package com.bernard.timetabler.dbinit.model.hall;

import com.google.gson.annotations.SerializedName;

/**
 * @author com.bernard
 */
public class HallDao {
    @SerializedName("hall")
    private Hall hall;

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
