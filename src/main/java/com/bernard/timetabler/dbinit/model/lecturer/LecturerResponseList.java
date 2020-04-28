package com.bernard.timetabler.dbinit.model.lecturer;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LecturerResponseList {
	@SerializedName("lecturers")
	private List<Lecturer> list;
	
	public List<Lecturer> getList() {
		return list;
	}
	
	public void setList(List<Lecturer> list) {
		this.list = list;
	}
}
