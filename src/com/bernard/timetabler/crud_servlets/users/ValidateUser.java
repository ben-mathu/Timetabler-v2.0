package com.bernard.timetabler.crud_servlets.users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.admin.Admin;
import com.bernard.timetabler.dbinit.model.admin.AdminResponse;
import com.bernard.timetabler.dbinit.model.campus.Campus;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.bernard.timetabler.dbinit.model.lecturer.Lecturer;
import com.bernard.timetabler.dbinit.model.programme.Programme;
import com.bernard.timetabler.dbinit.model.student.Student;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class ValidateUser
 */
@WebServlet("/validate-user")
public class ValidateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String TAG = ValidateUser.class.getSimpleName();
    
    private Gson gson;
    
    private CreateSchemaTimeTabler ct;
    private Statement st;
    
    public ValidateUser() {
    	CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		ct = new CreateSchemaTimeTabler("ben", "");
		
		st = ct.getStatement();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(Constants.APPLICATION_JSON);
		PrintWriter writer = response.getWriter();
		
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
			
			Log.d(TAG, "Buffered Line: " + strBuffer.toString());
		} catch (Exception e) {
			Log.e(TAG, "Error" + e.getMessage());
		}
		
		gson = new Gson();
		UserValidationRequest req = gson.fromJson(strBuffer.toString(), UserValidationRequest.class);
		
		try {
			String userJson = validateUser(req);
			
			writer.write(userJson);
		} catch (SQLException e) {
			Log.e(TAG, "Error: " + e.getMessage());
		}
	}
	
	private String validateUser(UserValidationRequest req) throws SQLException {		
		if (req.getRole().equalsIgnoreCase("admin")) {
			Admin admin = new Admin();
			AdminResponse res = new AdminResponse();
			
			String query = "SELECT * FROM " + Constants.TABLE_ADMIN +
					" WHERE " + Constants.USERNAME + "='" + req.getUsername() + "'";
			
			Log.d(TAG, "Query: " + query);
			
			ResultSet result = st.executeQuery(query);
			
			while (result.next()) {
				admin.setAdminId(result.getString(Constants.ADMIN_ID));
				admin.setfName(result.getString(Constants.F_NAME));
				admin.setlName(result.getString(Constants.L_NAME));
				admin.setmName(result.getString(Constants.M_NAME));
				admin.setPassword(result.getString(Constants.PASSWORD));
				admin.setUsername(result.getString(Constants.USERNAME));
			}
			res.setAdmin(admin);
			
			Log.d(TAG, "Found: " + gson.toJson(res));
			
			return gson.toJson(res);
		} else if (req.getRole().equalsIgnoreCase("student")) {
			Student student = new Student();
			StudentResponse res = new StudentResponse();
			
			String query = "SELECT * FROM " + Constants.TABLE_STUDENTS +
					" WHERE " + Constants.USERNAME + "='" + req.getUsername() + "'";
			
			ResultSet result = st.executeQuery(query);
			
			while (result.next()) {
				student.setStudentId(result.getString(Constants.STUDENT_ID));
				student.setFname(result.getString(Constants.F_NAME));
				student.setLname(result.getString(Constants.L_NAME));
				student.setMname(result.getString(Constants.M_NAME));
				student.setPassword(result.getString(Constants.PASSWORD));
				student.setUsername(result.getString(Constants.USERNAME));
				student.setAdmissionDate(result.getString(Constants.ADMISSION_DATE));
				student.setCampusId(result.getString(Constants.CAMPUS_ID));
				student.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
				student.setInSession(result.getBoolean(Constants.IN_SESSION));
				student.setProgrammeId(result.getString(Constants.PROGRAMME_ID));
				student.setYearOfStudy(result.getString(Constants.YEAR_OF_STUDY));
				student.setFacultyId(result.getString(Constants.FACULTY_ID));
			}
			res.setStudent(student);
			
			Log.d(TAG, "Found: " + gson.toJson(res));
			
			// declare classes to hold data tied to student
			Campus campus = getCampusById(student.getCampusId());
			Department dep = getDepartmentById(student.getDepartmentId());
			Faculty faculty = getFacultyById(student.getFacultyId());
			Programme prog = getProgrammeById(student.getProgrammeId());
			
			StudentResponse response = new StudentResponse();
			response.setStudent(student);
			response.setDepartment(dep);
			response.setCampus(campus);
			response.setFaculty(faculty);
			response.setProgramme(prog);
			
			Log.d(TAG, "Found " + gson.toJson(response));
			
			return gson.toJson(response);
		} else if (req.getRole().equalsIgnoreCase("lecturer")) {
			Lecturer lecturer = new Lecturer();
			LecturerResponse res = new LecturerResponse();
			
			String query = "SELECT * FROM " + Constants.TABLE_LECTURERS 
					+ " WHERE " + Constants.USERNAME + "='" + req.getUsername() + "'";
			
			ResultSet result = st.executeQuery(query);
			
			while (result.next()) {
				lecturer.setId(result.getString(Constants.LECTURER_ID));
				lecturer.setFirstName(result.getString(Constants.F_NAME));
				lecturer.setLastName(result.getString(Constants.L_NAME));
				lecturer.setMiddleName(result.getString(Constants.M_NAME));
				lecturer.setPassword(result.getString(Constants.PASSWORD));
				lecturer.setUsername(result.getString(Constants.USERNAME));
				lecturer.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
				lecturer.setInSession(result.getBoolean(Constants.IN_SESSION));
//				lecturer.setProgrammeId(result.getString(Constants.PROGRAMME_ID));
				lecturer.setFacultyId(result.getString(Constants.FACULTY_ID));
			}
			res.setLecturer(lecturer);
			
			Department dep = getDepartmentById(lecturer.getDepartmentId());
//			Programme prog = getProgrammeById(lecturer.getProgrammeId());
			Faculty faculty = getFacultyById(lecturer.getFacultyId());
			
			LecturerResponse response = new LecturerResponse();
			response.setDepartment(dep);
			response.setFaculty(faculty);
			response.setLecturer(lecturer);
//			response.setProgramme(prog);
			
			Log.d(TAG, "Found: " + gson.toJson(response));
			
			return gson.toJson(response);
		}
		
		return "";
	}

	private Programme getProgrammeById(String programmeId) throws SQLException {
		Programme prog = new Programme();
		
		String query = "SELECT * FROM " + Constants.TABLE_PROGRAMMES
				+ " WHERE " + Constants.PROGRAMME_ID + "='" + programmeId + "'";
		
		ResultSet result = st.executeQuery(query);
		
		while (result.next()) {
			prog.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
			prog.setFacultyId(result.getString(Constants.FACULTY_ID));
			prog.setProgrammeId(result.getString(Constants.PROGRAMME_ID));
			prog.setProgrammeName(result.getString(Constants.PROGRAMME_NAME));
		}
		return prog;
	}

	private Faculty getFacultyById(String facultyId) throws SQLException {
		Faculty faculty = new Faculty();
		
		String query = "SELECT * FROM " + Constants.TABLE_FACULTIES
				+ " WHERE " + Constants.FACULTY_ID + "='" + facultyId + "'";
		
		ResultSet result = st.executeQuery(query);
		
		while (result.next()) {
			faculty.setFacultyId(result.getString(Constants.FACULTY_ID));
			faculty.setFacultyName(result.getString(Constants.FACULTY_NAME));
			faculty.setCampusId(result.getString(Constants.CAMPUS_ID));
		}
		return faculty;
	}

	private Department getDepartmentById(String departmentId) throws SQLException {
		Department dep = new Department();
		
		String query = "SELECT * FROM " + Constants.TABLE_DEPARTMENTS
				+ " WHERE " + Constants.DEPARTMENT_ID + "='" + departmentId + "'";
		
		ResultSet result = st.executeQuery(query);
		
		while (result.next()) {
			dep.setFacultyId(result.getString(Constants.FACULTY_ID));
			dep.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
			dep.setDepartmentName(result.getString(Constants.DEPARTMENT_NAME));
		}
		return dep;
	}

	private Campus getCampusById(String campusId) throws SQLException {
		Campus campus = new Campus();
		
		String query = "SELECT * FROM " + Constants.TABLE_CAMPUS
				+ " WHERE " + Constants.CAMPUS_ID + "='" + campusId + "'";
		
		ResultSet result = st.executeQuery(query);
		
		while (result.next()) {
			campus.setCampusId(result.getString(Constants.CAMPUS_ID));
			campus.setCampusName(result.getString(Constants.CAMPUS_NAME));
		}
		return campus;
	}
	
	public class StudentResponse {
		@SerializedName("student")
	    private Student student;
		@SerializedName(Constants.TABLE_DEPARTMENTS)
	    private Department department;
	    @SerializedName(Constants.TABLE_CAMPUS)
	    private Campus campus;
	    @SerializedName(Constants.TABLE_FACULTIES)
	    private Faculty faculty;
	    @SerializedName(Constants.TABLE_PROGRAMMES)
	    private Programme programme;

	    public Student getStudent() {
	        return student;
	    }

	    public void setStudent(Student student) {
	        this.student = student;
	    }
	    
	    public Department getDepartment() {
			return department;
		}
	    
	    public void setDepartment(Department department) {
			this.department = department;
		}
	    
	    public Campus getCampus() {
			return campus;
		}
	    
	    public void setCampus(Campus campus) {
			this.campus = campus;
		}
	    
	    public Faculty getFaculty() {
			return faculty;
		}
	    
	    public void setFaculty(Faculty faculty) {
			this.faculty = faculty;
		}
	    
	    public Programme getProgramme() {
			return programme;
		}
	    
	    public void setProgramme(Programme programme) {
			this.programme = programme;
		}
	}
	
	public class LecturerResponse {
	    @SerializedName("lecturer")
	    private Lecturer lecturer;
	    @SerializedName(Constants.TABLE_FACULTIES)
	    private Faculty faculty;
	    @SerializedName(Constants.TABLE_DEPARTMENTS)
	    private Department department;
//	    @SerializedName(Constants.TABLE_PROGRAMMES)
//	    private Programme programme;

	    public Lecturer getLecturer() {
	        return lecturer;
	    }

	    public void setLecturer(Lecturer lecturer) {
	        this.lecturer = lecturer;
	    }

	    public Faculty getFaculty() {
	        return faculty;
	    }

	    public void setFaculty(Faculty faculty) {
	        this.faculty = faculty;
	    }

	    public Department getDepartment() {
	        return department;
	    }

	    public void setDepartment(Department department) {
	        this.department = department;
	    }
	    
//	    public Programme getProgramme() {
//			return programme;
//		}
//	    
//	    public void setProgramme(Programme programme) {
//			this.programme = programme;
//		}
	}

	public class UserValidationRequest {
	    @SerializedName("role")
	    private String role;
	    @SerializedName("username")
	    private String username;
	    @SerializedName("password")
	    private String password;
	    @SerializedName("userId")
	    private String userId;

	    public String getRole() {
	        return role;
	    }

	    public void setRole(String role) {
	        this.role = role;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }
	}
}
