package com.bernard.timetabler.crud_servlets.programme;

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
import com.bernard.timetabler.dbinit.model.programme.ProgrammeRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class UpdateCourse
 */
@WebServlet("/update-department")
public class UpdateProgramme extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Statement statement;
	
    public UpdateProgramme() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		// deserialise json string
		Gson gson = new Gson();
		ProgrammeRequest req = gson.fromJson(jsonRequest, ProgrammeRequest.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (updateProg(req)) {
				report.setMessage("Successfully updated " + req.getProgramme().getProgrammeName());
				jsonResponse = gson.toJson(report);
				
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not update " + req.getProgramme().getProgrammeName() + ". \nPlease try again, or contact admin.");
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean updateProg(ProgrammeRequest req) throws SQLException {
		String updateQuery = "UPDATE " + Constants.TABLE_PROGRAMMES
				+ " SET " + Constants.PROGRAMME_NAME + "='" + req.getProgramme().getProgrammeName() + "'"
				+ " SET " + Constants.DEPARTMENT_ID + "='" + req.getProgramme().getDepartmentId() + "'"
				+ " SET " + Constants.FACULTY_ID + "='" + req.getProgramme().getFacultyId() + "'"
				+ " WHERE " + Constants.PROGRAMME_ID + "='" + req.getProgramme().getProgrammeId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
