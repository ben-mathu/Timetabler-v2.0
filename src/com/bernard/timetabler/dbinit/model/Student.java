package com.bernard.timetabler.dbinit.model;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * POJO - for student objects
 */
public class Student {
	@SerializedName(Constants.STUDENT_ID)
    private String studentId;
	@SerializedName(Constants.F_NAME)
    private String fname;
	@SerializedName(Constants.L_NAME)
    private String lname;
	@SerializedName(Constants.M_NAME)
    private String mname;
	@SerializedName(Constants.USERNAME)
    private String username;
	@SerializedName(Constants.PASSWORD)
    private String password;
	@SerializedName(Constants.DEPARTMENT_ID)
    private String departmentId;
	@SerializedName(Constants.PROGRAMME_ID)
    private String programmeId;
	@SerializedName(Constants.CAMPUS_ID)
    private String campusId;
	@SerializedName(Constants.FACULTY_ID)
    private String facultyId;
	@SerializedName(Constants.YEAR_OF_STUDY)
    private String yearOfStudy;
	@SerializedName(Constants.ADMISSION_DATE)
    private String admissionDate;
	@SerializedName(Constants.IN_SESSION)
    private boolean inSession = false;
	@SerializedName(Constants.EMAIL)
	private String email;

    public Student(String studentId, String fname, String lname, String mname, String username, String password, String departmentId, String programmeId, String campusId, String facultyId, String yearOfStudy, String admissionDate, boolean inSession) {
        this.studentId = studentId;
        this.fname = fname;
        this.lname = lname;
        this.mname = mname;
        this.username = username;
        this.password = password;
        this.departmentId = departmentId;
        this.programmeId = programmeId;
        this.campusId = campusId;
        this.facultyId = facultyId;
        this.yearOfStudy = yearOfStudy;
        this.admissionDate = admissionDate;
        this.inSession = inSession;
    }

    public Student() {
        // Intentionally left blank
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }
    
    public void setUsername(String username) {
		this.username = username;
	}
    
    public String getUsername() {
		return username;
	}
    
    public void setPassword(String password) {
		this.password = password;
	}
    
    public String getPassword() {
		return password;
	}

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public boolean isInSession() {
        return inSession;
    }

    public void setInSession(boolean inSession) {
        this.inSession = inSession;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
    
    public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
