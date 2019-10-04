package com.bernard.timetabler.crud_servlets.users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.lecturer.LecturerRequest;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class UserSignUp
 */
@WebServlet("/lecturer-sign-up")
public class LecturerSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = LecturerSignUp.class.getSimpleName();
	
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
		
		// get db username from email
		Pattern pattern = Pattern.compile("(\\w*|\\w*.\\w*)@.*$");
		Matcher matcher = pattern.matcher(lecturer.getLecturer().getEmail());
		String username = "";
		if (matcher.find()) {
			username = matcher.group(1);
		}
		
		// initialize DB
		initDb(!username.isEmpty() ? username : "lecturer", lecturer.getPassword());
		PrintWriter printWriter = response.getWriter();
		try {
			// save the lecturer in database
			if (saveLecturer(lecturer)) {
				MessageReport report = new MessageReport();
				report.setMessage("Successfully added");
				String jsonReport = gson.toJson(report);
				
				printWriter.write(jsonReport);
			} else {
				MessageReport report = new MessageReport();
				report.setMessage("Error, invalide passcode");
				
				String jsonReport = gson.toJson(report);
				printWriter.write(jsonReport);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initDb(String username, String password) {
		statement = UtilCommonFunctions.initialize(username, password);
	}

	private boolean saveLecturer(LecturerRequest lecturer) throws SQLException {
		String insertStatement = "UPDATE " + Constants.TABLE_LECTURERS +
				" SET " + Constants.USERNAME + "='" + lecturer.getLecturer().getUsername() +
				"'," + Constants.PASSWORD + "='" + lecturer.getLecturer().getPassword() +
				"'," + Constants.FACULTY_ID + "='"+ lecturer.getLecturer().getFacultyId() +
				"'," + Constants.DEPARTMENT_ID + "='" + lecturer.getLecturer().getDepartmentId() +
				"'," + Constants.IN_SESSION + "=" + lecturer.getLecturer().isInSesson() +
				" WHERE " + Constants.EMAIL + "='" + lecturer.getLecturer().getEmail() + "'" +
				" AND " + Constants.IS_REMOVED + "=" + false;
		return statement.executeUpdate(insertStatement) > 0 ? true : false;
	}

}
