package com.bernard.timetabler.crud_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.ErrorReport;
import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.LecturerUnit;
import com.bernard.timetabler.dbinit.model.StudentUnit;
import com.bernard.timetabler.dbinit.model.Unit;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class RegisterUnits
 */
@WebServlet("/submit-units/*")
public class RegisterUnits extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = RegisterUnits.class.getSimpleName();
	
	private PrintWriter out;
	
	private Statement statement;
	
	int countRowsAffected = 0;
	
	private String jsonResponse = "";
	private String studentId = "";
	private String lecturerId = "";
	
    public RegisterUnits() {
    	CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
    	
    	CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
    	statement = ct.getStatement();
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameterMap().containsKey(Constants.LECTURER_ID)) {
			saveByLec(request, response);
		} else if (request.getParameterMap().containsKey(Constants.STUDENT_ID)) {
			save(request, response);
		}
	}
	
	private void saveByLec(HttpServletRequest request, HttpServletResponse response) throws IOException {
		lecturerId = request.getParameter(Constants.LECTURER_ID);
		response.setContentType(Constants.APPLICATION_JSON);
		
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		Gson gson = new Gson();
		LecturerUnitRequest req = gson.fromJson(strBuffer.toString(), LecturerUnitRequest.class);
		
		out = response.getWriter();
		MessageReport success = new MessageReport();
		ErrorReport err = new ErrorReport();
		
		try {
			if (saveRegisteredUnits(req, response)) {
				success.setMessage("Registered Successfully, Units registered " + countRowsAffected);
				
				jsonResponse = gson.toJson(success);
				
				out.write(jsonResponse);
			} else {
				err.setErrorMessage("Error occurred, Please try again.");
				
				jsonResponse = gson.toJson(err);
				
				out.write(jsonResponse);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
		studentId = request.getParameter(Constants.STUDENT_ID);
		response.setContentType(Constants.APPLICATION_JSON);
		
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		Gson gson = new Gson();
		StudentUnitRequest req = gson.fromJson(strBuffer.toString(), StudentUnitRequest.class);
		
		out = response.getWriter();
		MessageReport success = new MessageReport();
		ErrorReport err = new ErrorReport();
		
		try {
			if (saveRegisteredUnits(req, response)) {
				success.setMessage("Registered Successfully, Units registered " + countRowsAffected);
				
				jsonResponse = gson.toJson(success);
				
				out.write(jsonResponse);
			} else {
				err.setErrorMessage("Error occurred, Please try again.");
				
				jsonResponse = gson.toJson(err);
				
				out.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean saveRegisteredUnits(StudentUnitRequest req, HttpServletResponse response) throws SQLException, IOException {
		for (StudentUnit studentUnit : req.getStudentUnitList()) {
			String insertUnit = "INSERT INTO " + Constants.TABLE_STUDENT_UNITS +
					"(" + Constants.STUDENT_ID + "," + Constants.UNIT_ID + "," + Constants.IS_REMOVED + ")" +
					" VALUES ('" + studentId + "','" +
					studentUnit.getUnitId() + "'," + studentUnit.isRemoved() + ")";
			countRowsAffected = statement.executeUpdate(insertUnit);
			
			Log.d(TAG, "Insert statement: " + insertUnit + "\n");
			Log.d(TAG, "Rows affected " + countRowsAffected);
		}
		
		if (countRowsAffected <= req.getStudentUnitList().size() && countRowsAffected != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean saveRegisteredUnits(LecturerUnitRequest req, HttpServletResponse response) throws SQLException, IOException {
		for (LecturerUnit lecturerUnit : req.getLecturerUnitList()) {
			String insertUnit = "INSERT INTO " + Constants.TABLE_LECTURER_UNITS +
					"(" + Constants.LECTURER_ID + "," + Constants.UNIT_ID + "," + Constants.IS_REMOVED + ")" +
					" VALUES ('" + lecturerId + "','" +
					lecturerUnit.getUnitId() + "'," + lecturerUnit.isRemoved() + ")";
			countRowsAffected = statement.executeUpdate(insertUnit);
			
			Log.d(TAG, "Insert statement: " + insertUnit + "\n");
			Log.d(TAG, "Rows affected " + countRowsAffected);
		}
		
		if (countRowsAffected <= req.getLecturerUnitList().size() && countRowsAffected != 0) {
			return true;
		} else {
			return false;
		}
	}

	private class StudentUnitRequest {
		@SerializedName("units")
		List<StudentUnit> studentUnitList;
		
		public List<StudentUnit> getStudentUnitList() {
			return studentUnitList;
		}
		
		public void setStudentUnitList(List<StudentUnit> studentUnitList) {
			this.studentUnitList = studentUnitList;
		}
	}
	
	private class LecturerUnitRequest {
		@SerializedName("units")
		List<LecturerUnit> lecturerUnitList;
		
		public List<LecturerUnit> getLecturerUnitList() {
			return lecturerUnitList;
		}
		
		public void setStudentUnitList(List<LecturerUnit> lecturerUnitList) {
			this.lecturerUnitList = lecturerUnitList;
		}
	}
}
