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
 * Servlet implementation class DeleteCourse
 */
@WebServlet("/delete-course")
public class DeleteCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private Statement statement;
    
    public DeleteCourse() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String  jsonrequest = BufferRequest.bufferRequest(request);
		
		// deserialize json string: jsonRequest
		Gson gson = new Gson();
		UnitReq req = gson.fromJson(jsonrequest, UnitReq.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (deleteCourse(req)) {
				report.setMessage("Successfully deleted course" + req.getUnit().getUnitName());
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_OK);
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not delete " + req.getUnit().getUnitName());
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean deleteCourse(UnitReq req) throws SQLException{
		String updateQuery = "UPDATE " + Constants.TABLE_UNITS
				+ " SET " + Constants.IS_REMOVED + "=" + true
				+ " WHERE " + Constants.UNIT_ID + "='" + req.getUnit().getId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
