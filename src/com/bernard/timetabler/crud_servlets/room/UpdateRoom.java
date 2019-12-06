package com.bernard.timetabler.crud_servlets.room;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * Servlet implementation class UpdateCourse
 */
@WebServlet("/update-room/*")
public class UpdateRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Statement statement;
	
    public UpdateRoom() {
        super();
        
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String classId = request.getParameter(Constants.CLASS_ID);

		String jsonRequest = BufferRequest.bufferRequest(request);
		
		// deserialise json string
		Gson gson = new Gson();
		RoomRequest req = gson.fromJson(jsonRequest, RoomRequest.class);
		
		try {
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter writer;
			
			MessageReport report = new MessageReport();
			String jsonResponse = "";
			
			if (updateRoom(req, classId)) {
				report.setMessage("Successfully updated " + req.getRoom().getId());
				jsonResponse = gson.toJson(report);

			} else {
				report.setMessage("Could not update " + req.getRoom().getId() + ". \nPlease try again, or contact admin.");
				jsonResponse = gson.toJson(report);
				
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			}
			writer = response.getWriter();
			writer.write(jsonResponse);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean updateRoom(RoomRequest req, String classId) throws SQLException {
		String oldClassId = "";
		String newClassId = "";

		Matcher matcher = Pattern.compile("^(\\w+)\\s.*$").matcher(req.getRoom().getId());
		if (matcher.find())
			oldClassId = matcher.group(1);

		matcher = Pattern.compile("\\s(\\w+)$").matcher(req.getRoom().getId());
		if (matcher.find())
			newClassId = matcher.group(1);

		String updateQuery = "UPDATE " + Constants.TABLE_CLASSES
				+ " SET " + Constants.CLASS_ID + "='" + newClassId + "'"
				+ " , " + Constants.FACULTY_ID + "='" + req.getRoom().getFacultyId() + "'"
				+ " , " + Constants.HALL_ID + "='" + req.getRoom().getHall_id() + "'"
				+ " , " + Constants.VOLUME + "='" + req.getRoom().getVolume() + "'"
				+ " , " + Constants.IS_LAB + "=" + req.getRoom().isLab()
				+ " , " + Constants.AVAILABILITY + "=" + req.getRoom().isLab()
				+ " WHERE " + Constants.CLASS_ID + "='" + oldClassId + "'";
		
		if (statement.executeUpdate(updateQuery) != 0) {
			return true;
		}
		return false;
	}

}
