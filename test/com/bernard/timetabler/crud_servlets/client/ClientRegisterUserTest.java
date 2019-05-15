package com.bernard.timetabler.crud_servlets.client;

import com.bernard.timetabler.dbinit.model.Lecturer;
import com.google.gson.annotations.SerializedName;

public class ClientRegisterUserTest {
	private Lecturer lecturer;
	public ClientRegisterUserTest(Lecturer lecturer) {
		this.lecturer = lecturer;
	}
	
	private class LecturerResponse {
		@SerializedName("lecturer")
		private Lecturer lecturer;
		
		public LecturerResponse() {
			// TODO Auto-generated constructor stub
		}
		
		public Lecturer getLecturer() {
			return lecturer;
		}
	}
	
	public static void main(String[] args) {
		Lecturer lecturer = new Lecturer();
		lecturer.setId("123456");
		lecturer.setFirstName("help");
		lecturer.setLastName("last");
		lecturer.setMiddleName("name");
		lecturer.setUsername("user");
		lecturer.setPassword("help");
		lecturer.setDepartmentId("od99e");
		lecturer.setFacultyId("oisjd884");
		lecturer.setProgrammeId("oosj8e9f");
		ClientRegisterUserTest clientRegTest = new ClientRegisterUserTest(lecturer);
		
		
	}
}
