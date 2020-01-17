package com.bernard.timetabler.crud_servlets.campus;

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
import com.bernard.timetabler.dbinit.model.campus.Campus;
import com.bernard.timetabler.dbinit.model.campus.CampusRequest;
import com.bernard.timetabler.dbinit.utils.GenerateRandomString;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.JwtTokenUtil;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@WebServlet("/add-campus")
public class AddCampus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private Statement statement;
    public AddCampus() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		String token = request.getHeader("Authorization");
		
		// deserialization of json strings
		Gson gson = new Gson();
		CampusRequest req = gson.fromJson(jsonRequest, CampusRequest.class);

		// prepare response
		response.setContentType(Constants.APPLICATION_JSON);
		PrintWriter writer;

		try {

			if (token == null) {
				throw new NullPointerException("Requires Validation.");
			} else {
				JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
				if (jwtTokenUtil.verifyToken(Constants.ISSUER, "login", token)) {

				}
			}
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			if (saveCampus(req)) {
				report.setMessage("Successfully added" + req.getCampus().getCampusName());
				jsonResponse = gson.toJson(req);
				
				response.setStatus(HttpServletResponse.SC_CREATED);
				writer = response.getWriter();
				writer.write(jsonRequest);
			} else {
				report.setMessage("Could not save " + req.getCampus().getCampusName());
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);

			writer = response.getWriter();

			MessageReport report = new MessageReport();
			report.setMessage("Requires authorization.");

			String message = gson.toJson(report);
			writer.write(message);
		}
	}
	
	private boolean saveCampus(CampusRequest req) throws SQLException {
		// generate id for campus
		GenerateRandomString rand = new GenerateRandomString(7);
		String id = rand.nextString();
		
		req.getCampus().setCampusId(id);
		
		String insertQuery = "INSERT INTO " + Constants.TABLE_CAMPUS
				+ " VALUES('" + id + "','"
				+ req.getCampus().getCampusName()  + "'," + req.getCampus().isRemoved() + ")";
		
		if (statement.executeUpdate(insertQuery) != 0) {
			return true;
		}
		return false;
	}
}
