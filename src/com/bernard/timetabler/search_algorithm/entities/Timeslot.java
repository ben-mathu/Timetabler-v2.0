package com.bernard.timetabler.search_algorithm.entities;

import java.util.HashMap;

import com.bernard.timetabler.dbinit.model.course.Unit;

public class Timeslot {
	HashMap<String, String> timeslot = new HashMap<>();
	Unit unit = new Unit();
	
	public void setTimeslot(HashMap<String, String> timeslot) {
		this.timeslot = timeslot;
	}
	
	public HashMap<String, String> getTimeslot() {
		return timeslot;
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public Unit getUnit() {
		return unit;
	}
}
