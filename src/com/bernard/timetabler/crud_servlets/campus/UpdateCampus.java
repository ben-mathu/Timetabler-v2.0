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
import com.bernard.timetabler.dbinit.model.Campus;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@WebServlet("/update-campus")
public class UpdateCampus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private Statement statement;
    public UpdateCampus() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		// deserialization of json strings
		Gson gson = new Gson();
		CampusRequest req = gson.fromJson(jsonRequest, CampusRequest.class);
		
		try {
			// prepare response
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (updateCampus(req)) {
				report.setMessage("Successfully added" + req.getCampus().getCampusId());
				jsonResponse = gson.toJson(report);
				
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
		}
	}
	
	private boolean updateCampus(CampusRequest req) throws SQLException {		
		String insertQuery = "UPDATE " + Constants.TABLE_CAMPUS
				+ " SET " + Constants.CAMPUS_NAME + "='" + req.getCampus().getCampusName() + "'"
				+ " WHERE " + Constants.CAMPUS_ID + "='" + req.getCampus().getCampusId() + "'";
		
		if (statement.executeUpdate(insertQuery) != 0) {
			return true;
		}
		return false;
	}

	public class CampusRequest {
	    @SerializedName("campus")
	    private Campus campus;

	    public CampusRequest(Campus campus) {
	        this.campus = campus;
	    }

	    public Campus getCampus() {
	        return campus;
	    }

	    public void setCampus(Campus campus) {
	        this.campus = campus;
	    }
	}
}
