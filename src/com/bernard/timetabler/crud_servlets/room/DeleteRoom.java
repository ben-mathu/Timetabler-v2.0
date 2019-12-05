package com.bernard.timetabler.crud_servlets.room;

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
import com.bernard.timetabler.dbinit.model.room.RoomRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class DeleteCourse
 */
@WebServlet("/delete-room")
public class DeleteRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private Statement statement;
    
    public DeleteRoom() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String  jsonrequest = BufferRequest.bufferRequest(request);
		
		// deserialize json string: jsonRequest
		Gson gson = new Gson();
		RoomRequest req = gson.fromJson(jsonrequest, RoomRequest.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (deleteRoom(req)) {
				report.setMessage("Successfully deleted " + req.getRoom().getId());
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_OK);
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not delete " + req.getRoom().getId());
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean deleteRoom(RoomRequest req) throws SQLException{
		String updateQuery = "DELETE FROM " + Constants.TABLE_CLASSES
				+ " WHERE " + Constants.CLASS_ID + "='" + req.getRoom().getId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
