package com.bernard.timetabler.crud_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.Lecturer;
import com.bernard.timetabler.dbinit.model.Student;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class UserSignUp
 */
@WebServlet("/student-sign-up")
public class StudentSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = LecturerSignUp.class.getSimpleName();
	
	private CreateSchemaTimeTabler ct;
	private Statement statement;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		response.setContentType("application/json");
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		Gson gson = new Gson();
		StudentRequest student = gson.fromJson(strBuffer.toString(), StudentRequest.class);
		
		Log.d(TAG, "populating Student details");
		
		initDb();
		PrintWriter printWriter = response.getWriter();
		try {
			if (saveStudent(student)) {
				MessageReport report = new MessageReport();
				report.setMessage("Successfully added");
				String jsonReport = gson.toJson(report);
				
				printWriter.write(jsonReport);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class StudentRequest {
		@SerializedName("student")
		private Student student;
		
		public void setStudent(Student student) {
			this.student = student;
		}
		
		public Student getStudent() {
			return student;
		}
	}

	private void initDb() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
	}

	private boolean saveStudent(StudentRequest student) throws SQLException {
		String insertStatement = "INSERT INTO " + Constants.TABLE_STUDENTS +
				" VALUES ('" + student.getStudent().getStudentId() +
				"','" + student.getStudent().getFname() +
				"','" + student.getStudent().getLname() +
				"','" + student.getStudent().getMname() +
				"','" + student.getStudent().getUsername() +
				"','" + student.getStudent().getPassword() +
				"','" + student.getStudent().getEmail() +
				"'," + student.getStudent().isInSession() +
				",'" + student.getStudent().getDepartmentId() +
				"','" + student.getStudent().getCampusId() +
				"','" + student.getStudent().getFacultyId() +
				"','" + student.getStudent().getProgrammeId() +
				"','" + student.getStudent().getYearOfStudy() +
				"','" + student.getStudent().getAdmissionDate() +
				"')";
		return statement.executeUpdate(insertStatement) > 0 ? true : false;
	}

}
