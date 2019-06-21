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
import com.bernard.timetabler.crud_servlets.reponses.SuccessfulReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
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
	
    public RegisterUnits() {
    	CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
    	
    	CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler();
    	statement = ct.getStatement();
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String studentId = request.getParameter(Constants.STUDENT_ID);
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
		SuccessfulReport success = new SuccessfulReport();
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
	
	private boolean saveRegisteredUnits(StudentUnitRequest req, HttpServletResponse response) throws SQLException, IOException {
		for (StudentUnit studentUnit : req.getStudentUnitList()) {
			String insertUnits = "INSERT INTO TABLE " + Constants.TABLE_STUDENT_UNITS +
					" VALUES ('" + studentUnit.getStudentId() + "','" +
					studentUnit.getUnitId() + "')";
			countRowsAffected = statement.executeUpdate(insertUnits);
			
			Log.d(TAG, "Rows affected " + countRowsAffected);
		}
		
		if (countRowsAffected <= req.getStudentUnitList().size() && countRowsAffected != 0) {
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
}
