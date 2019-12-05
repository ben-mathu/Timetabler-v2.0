package com.bernard.timetabler.crud_servlets.faculty;

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
import com.bernard.timetabler.dbinit.model.faculty.FacultyRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class UpdateCourse
 */
@WebServlet("/update-faculty")
public class UpdateFaculty extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Statement statement;
	
    public UpdateFaculty() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		// deserialise json string
		Gson gson = new Gson();
		FacultyRequest req = gson.fromJson(jsonRequest, FacultyRequest.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (updateFaculty(req)) {
				report.setMessage("Successfully updated " + req.getFaculty().getFacultyName());
				jsonResponse = gson.toJson(report);
				
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not update " + req.getFaculty().getFacultyName() + ". \nPlease try again, or contact admin.");
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean updateFaculty(FacultyRequest req) throws SQLException {
		String updateQuery = "UPDATE " + Constants.TABLE_FACULTIES
				+ " SET " + Constants.FACULTY_NAME + "='" + req.getFaculty().getFacultyName() + "'"
				+ " SET " + Constants.CAMPUS_ID + "='" + req.getFaculty().getCampusId() + "'"
				+ " WHERE " + Constants.FACULTY_ID + "='" + req.getFaculty().getFacultyId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
