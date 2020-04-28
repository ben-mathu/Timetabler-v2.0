package com.bernard.timetabler.dbinit.model.programme;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProgrammesResponse {
	@SerializedName("programmes")
	private List<Programme> programmes;
	
	@SuppressWarnings("unused")
	public List<Programme> getProgrammes() {
		return programmes;
	}
	
	public void setProgrammes(List<Programme> programmes) {
		this.programmes = programmes;
	}
}
