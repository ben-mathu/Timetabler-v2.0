package com.bernard.timetabler.dbinit.model.room;

import com.google.gson.annotations.SerializedName;

public class RoomReq {
    @SerializedName("room")
    private Class room;
    @SerializedName("passcode")
    private String passCode;

    public RoomReq(Class room, String passCode) {
        this.room = room;
        this.passCode = passCode;
    }
    
    public RoomReq() {}
    
    public void setRoom(Class room) {
        this.room = room;
    }

    public Class getRoom() {
        return room;
    }
    
    public String getPassCode() {
		return passCode;
	}
    
    public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
}
