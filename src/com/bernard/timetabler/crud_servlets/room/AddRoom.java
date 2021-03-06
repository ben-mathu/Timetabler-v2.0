package com.bernard.timetabler.crud_servlets.room;

import java.io.BufferedReader;
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
import com.bernard.timetabler.dbinit.model.room.Class;
import com.bernard.timetabler.dbinit.model.room.RoomReq;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class AddRoom
 */
@WebServlet("/add-room")
public class AddRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = AddRoom.class.getSimpleName();
	
	private Statement statement;
	
	public AddRoom() {
		statement = UtilCommonFunctions.initialize("ben", "");
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		Gson gson = new Gson();
		RoomReq roomReq = gson.fromJson(strBuffer.toString(), RoomReq.class);
		
		String passCode = roomReq.getPassCode();
		statement = UtilCommonFunctions.initialize("benard", passCode);
		
		try {
			if (addRoom(roomReq.getRoom())) {
				MessageReport report = new MessageReport();
				report.setMessage("Successfully added unit" + roomReq.getRoom().getId());
				
				writer.write(gson.toJson(report));
			} else {
				MessageReport report = new MessageReport();
				report.setMessage("Could not add unit" + roomReq.getRoom().getId());
				
				writer.write(gson.toJson(report));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean addRoom(Class room) throws SQLException {
		String strQuery = "INSERT INTO " + Constants.TABLE_CLASSES +
                " VALUES ('" + room.getId() +
                "', '" + room.getHall_id() +
                "', '" + room.getFacultyId() +
                "', '" + room.getVolume() +
                "', " + room.isAvailability() +
                ", " + room.isLab() +
                ", " + room.isRemoved() +
                ")";
		if (statement.executeUpdate(strQuery) != 0)
			return true;
		else
			return false;
	}
}
