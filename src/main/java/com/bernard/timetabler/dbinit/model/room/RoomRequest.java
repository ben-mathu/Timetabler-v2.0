package com.bernard.timetabler.dbinit.model.room;

import com.google.gson.annotations.SerializedName;

public class RoomRequest {
    @SerializedName("room")
    private Class room;

    public RoomRequest(Class room) {
        this.room = room;
    }
    
    public RoomRequest() {}
    
    public void setRoom(Class room) {
        this.room = room;
    }

    public Class getRoom() {
        return room;
    }
}
