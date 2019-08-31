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

import com.bernard.timetabler.crud_servlets.reponses.SuccessfulReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.Lecturer;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class UserSignUp
 */
@WebServlet("/lecturer-sign-up")
public class LecturerSignUp extends HttpServlet {
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
			Log.e(TAG, e.getMessage());
		}
		
		Gson gson = new Gson();
		LecturerRequest lecturer = gson.fromJson(strBuffer.toString(), LecturerRequest.class);
		
		Log.d(TAG, "populating lecturer details");
		
		initDb("lecturer", lecturer.getPassword());
		PrintWriter printWriter = response.getWriter();
		try {
			if (saveLecturer(lecturer)) {
				SuccessfulReport report = new SuccessfulReport();
				report.setMessage("Successfully added");
				String jsonReport = gson.toJson(report);
				
				printWriter.write(jsonReport);
			} else {
				SuccessfulReport report = new SuccessfulReport();
				report.setMessage("Error, invalide passcode");
				
				String jsonReport = gson.toJson(report);
				printWriter.write(jsonReport);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class LecturerRequest {
		@SerializedName("lecturer")
		private Lecturer lecturer;
		@SerializedName("password")
		private String password;
		
		public LecturerRequest() {
			// TODO Auto-generated constructor stub
		}
		
		public void setLecturer(Lecturer lecturer) {
			this.lecturer = lecturer;
		}
		
		public Lecturer getLecturer() {
			return lecturer;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public String getPassword() {
			return password;
		}
	}

	private void initDb(String username, String password) {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		ct = new CreateSchemaTimeTabler(username, password);
		statement = ct.getStatement();
	}

	private boolean saveLecturer(LecturerRequest lecturer) throws SQLException {
		String insertStatement = "INSERT INTO " + Constants.TABLE_LECTURERS +
				" VALUES ('" + lecturer.getLecturer().getId() +
				"','" + lecturer.getLecturer().getFirstName() +
				"','" + lecturer.getLecturer().getLastName() +
				"','" + lecturer.getLecturer().getMiddleName() +
				"','" + lecturer.getLecturer().getUsername() +
				"','" + lecturer.getLecturer().getPassword() +
				"','" + lecturer.getLecturer().getFacultyId() +
				"','" + lecturer.getLecturer().getDepartmentId() +
				"'," + lecturer.getLecturer().isInSesson() +
				")";
		return statement.executeUpdate(insertStatement) > 0 ? true : false;
	}

}
