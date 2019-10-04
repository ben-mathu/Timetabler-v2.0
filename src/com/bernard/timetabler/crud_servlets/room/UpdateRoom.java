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
import com.bernard.timetabler.dbinit.model.room.RoomReq;
import com.bernard.timetabler.dbinit.model.room.RoomRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class UpdateCourse
 */
@WebServlet("/update-room")
public class UpdateRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Statement statement;
	
    public UpdateRoom() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		// deserialise json string
		Gson gson = new Gson();
		RoomRequest req = gson.fromJson(jsonRequest, RoomRequest.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (updateRoom(req)) {
				report.setMessage("Successfully updated " + req.getRoom().getId());
				jsonResponse = gson.toJson(report);
				
				writer = response.getWriter();
				writer.write(jsonResponse);
			} else {
				report.setMessage("Could not update " + req.getRoom().getId() + ". \nPlease try again, or contact admin.");
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				writer = response.getWriter();
				writer.write(jsonResponse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean updateRoom(RoomRequest req) throws SQLException {
		String updateQuery = "UPDATE " + Constants.TABLE_CLASSES
				+ " SET " + Constants.CLASS_ID + "='" + req.getRoom().getId() + "'"
				+ " SET " + Constants.FACULTY_ID + "='" + req.getRoom().getFacultyId() + "'"
				+ " SET " + Constants.HALL_ID + "='" + req.getRoom().getHall_id() + "'"
				+ " SET " + Constants.VOLUME + "='" + req.getRoom().getVolume() + "'"
				+ " WHERE " + Constants.CLASS_ID + "='" + req.getRoom().getId() + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
