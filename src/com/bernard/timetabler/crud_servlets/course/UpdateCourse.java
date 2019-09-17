package com.bernard.timetabler.crud_servlets.course;

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
import com.bernard.timetabler.dbinit.model.course.UnitReq;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class UpdateCourse
 */
@WebServlet("/update-course")
public class UpdateCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Statement statement;
	
    public UpdateCourse() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		// deserialise json string
		Gson gson = new Gson();
		UnitReq req = gson.fromJson(jsonRequest, UnitReq.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (updateCourse(req)) {
				report.setMessage("Successfully updated " + req.getUnit().getUnitName());
				jsonResponse = gson.toJson(report);
				
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not update " + req.getUnit().getUnitName() + ". \nPlease try again, or contact admin.");
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean updateCourse(UnitReq req) throws SQLException {
		String updateQuery = "UPDATE " + Constants.TABLE_UNITS
				+ " SET " + Constants.UNIT_NAME + "='" + req.getUnit().getUnitName() + "'"
				+ " SET " + Constants.PROGRAMME_ID + "='" + req.getUnit().getProgrammeId() + "'"
				+ " SET " + Constants.FACULTY_ID + "='" + req.getUnit().getFacultyId() + "'"
				+ " SET " + Constants.DEPARTMENT_ID + "='" + req.getUnit().getDepartmentId() + "'"
				+ " SET " + Constants.IS_PRACTICAL + "=" + req.getUnit().isPractical()
				+ " SET " + Constants.IS_COMMON + "=" + req.getUnit().isCommon()
				+ " WHERE " + Constants.UNIT_ID + "='" + req.getUnit().getId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
