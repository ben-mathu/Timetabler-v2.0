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
import com.bernard.timetabler.dbinit.model.programme.ProgrammeRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

import static com.bernard.timetabler.dbinit.Constants.*;

/**
 * Servlet implementation class DeleteCourse
 */
@WebServlet("/delete-programme")
public class DeleteProgramme extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private Statement statement;
    
    public DeleteProgramme() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String  jsonrequest = BufferRequest.bufferRequest(request);
		
		// deserialize json string: jsonRequest
		Gson gson = new Gson();
		ProgrammeRequest req = gson.fromJson(jsonrequest, ProgrammeRequest.class);
		
		try {
			response.setContentType(APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (deleteprog(req)) {
				report.setMessage("Successfully deleted " + req.getProgramme().getProgrammeName());
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_OK);
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not delete " + req.getProgramme().getProgrammeName());
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean deleteprog(ProgrammeRequest req) throws SQLException{
		String updateQuery = "DELETE FROM " + TABLE_PROGRAMMES
				+ " WHERE " + PROGRAMME_ID + "='" + req.getProgramme().getProgrammeId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
