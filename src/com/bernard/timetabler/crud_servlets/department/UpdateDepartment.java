package com.bernard.timetabler.crud_servlets.department;

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
import com.bernard.timetabler.dbinit.model.department.DepartmentRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class UpdateCourse
 */
@WebServlet("/update-department")
public class UpdateDepartment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Statement statement;
	
    public UpdateDepartment() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		// deserialise json string
		Gson gson = new Gson();
		DepartmentRequest req = gson.fromJson(jsonRequest, DepartmentRequest.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (updateDepart(req)) {
				report.setMessage("Successfully updated " + req.getDepartment().getDepartmentName());
				jsonResponse = gson.toJson(report);
				
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not update " + req.getDepartment().getDepartmentName() + ". \nPlease try again, or contact admin.");
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean updateDepart(DepartmentRequest req) throws SQLException {
		String updateQuery = "UPDATE " + Constants.TABLE_DEPARTMENTS
				+ " SET " + Constants.DEPARTMENT_NAME + "='" + req.getDepartment().getDepartmentName()+ "'"
				+ " SET " + Constants.FACULTY_ID + "='" + req.getDepartment().getFacultyId() + "'"
				+ " WHERE " + Constants.UNIT_ID + "='" + req.getDepartment().getDepartmentId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
