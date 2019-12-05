package com.bernard.timetabler.dbinit.model.department;

import com.google.gson.annotations.SerializedName;

public class DepartmentRequest {
	@SerializedName("department")
	private Department department;
	
	public Department getDepartment() {
		return department;
	}
	
	public void setDepartment(Department department) {
		this.department = department;
	}
}
