package com.bernard.timetabler.dbinit.model.hall;

import com.bernard.timetabler.dbinit.model.hall.Hall;
import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
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
