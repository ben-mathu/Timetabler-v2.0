package com.bernard.timetabler.crud_servlets.users.student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.student.StudentRequest;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class UserSignUp
 */
@WebServlet("/student-sign-up")
public class StudentSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = StudentSignUp.class.getSimpleName();

	private Statement statement;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();

		response.setContentType("application/json");
		
		try {
			BufferedReader reader = request.getReader();
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		StudentRequest student = gson.fromJson(sb.toString(), StudentRequest.class);
		
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
			e.printStackTrace();
		}
	}

	private void initDb() {
		statement = UtilCommonFunctions.initialize("ben", "");
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
				"'," + student.getStudent().isRemoved() +
				")";
		return statement.executeUpdate(insertStatement) > 0;
	}

}
