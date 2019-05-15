package com.bernard.timetabler.crud_servlets;

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
	
	private CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler();
	private Statement statement;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Lecturer> lecturers = new ArrayList<>();
		response.setContentType(Constants.APPLICATION_JSON);
		PrintWriter printWriter = response.getWriter();
		
		
		Gson gson = new Gson();
		Lecturer lecturer = gson.fromJson(request.getParameter("lecturer"), Lecturer.class);
		
		Log.d(TAG, "populating lecturer details");
		
		initDb();
		
		try {
			if (saveLecturer(lecturer)) {
				SuccessfulReport report = new SuccessfulReport();
				report.setMessage("Successfully added");
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
		
		public LecturerRequest() {
			// TODO Auto-generated constructor stub
		}
		
		public void setLecturer(Lecturer lecturer) {
			this.lecturer = lecturer;
		}
		
		public Lecturer getLecturer() {
			return lecturer;
		}
	}

	private void initDb() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		statement = ct.getStatement();
	}

	private boolean saveLecturer(Lecturer lecturer) throws SQLException {
		String insertStatement = "INSERT INTO " + Constants.TABLE_LECTURERS +
				" VALUES ('" + lecturer.getId() +
				"','" + lecturer.getFirstName() +
				"','" + lecturer.getLastName() +
				"','" + lecturer.getMiddleName() +
				"','" + lecturer.getUsername() +
				"','" + lecturer.getPassword() +
				"','" + lecturer.getDepartmentId() +
				"'," + lecturer.isInSesson() +
				"')";
		return statement.executeUpdate(insertStatement) > 0 ? true : false;
	}

}
