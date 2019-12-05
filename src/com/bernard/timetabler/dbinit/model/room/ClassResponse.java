package com.bernard.timetabler.dbinit.model.room;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ClassResponse {
	@SerializedName("rooms")
	private List<Class> rooms;
	
	public List<Class> getRooms() {
		return rooms;
	}
	
	public void setRooms(List<Class> rooms) {
		this.rooms = rooms;
	}
}
