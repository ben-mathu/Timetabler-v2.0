package com.bernard.timetabler.dbinit.model.faculty;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FacultyList {
	@SerializedName("faculties")
	private List<Faculty> facultyList;
	
	public void setFacultyList(List<Faculty> facultyList) {
		this.facultyList = facultyList;
	}
	
	public List<Faculty> getFacultyList() {
		return facultyList;
	}
}
