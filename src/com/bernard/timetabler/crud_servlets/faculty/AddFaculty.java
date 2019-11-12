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
import com.bernard.timetabler.dbinit.utils.GenerateRandomString;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

@WebServlet("/add-faculty")
public class AddFaculty extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Statement statement;
	
	public AddFaculty() {
		statement = UtilCommonFunctions.initialize("ben", "");
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		Gson gson = new Gson();
		FacultyRequest req = gson.fromJson(jsonRequest, FacultyRequest.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			PrintWriter writer;
			if (addFaculty(req)) {
				report.setMessage("Successfully added " + req.getFaculty().getFacultyName());
				
				jsonResponse = gson.toJson(report);
				response.setStatus(HttpServletResponse.SC_CREATED);
				
				writer = response.getWriter();
				
				writer.write(jsonResponse);
			} else {
				report.setMessage("Did not add " + req.getFaculty().getFacultyName());
				
				jsonResponse = gson.toJson(report);
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				
				writer = response.getWriter();
				
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean addFaculty(FacultyRequest req) throws SQLException {
		GenerateRandomString rand = new GenerateRandomString(7);
		String id = rand.nextString();
		
		String insertQuery = "INSERT INTO " + Constants.TABLE_FACULTIES
				+ " VALUES('" + id + "','"
				+ req.getFaculty().getFacultyName() + "','"
				+ req.getFaculty().getCampusId() + "')";
		
		if (statement.executeUpdate(insertQuery) > 0) {
			return true;
		}
		return false;
	}
}
