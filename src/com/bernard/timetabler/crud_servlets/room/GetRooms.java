package com.bernard.timetabler.crud_servlets.room;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.room.Class;
import com.bernard.timetabler.dbinit.model.room.ClassResponse;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;

/**
 * Servlet implementation class GetRooms
 */
@WebServlet("/rooms")
public class GetRooms extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetRooms.class.getSimpleName();
	
	private Statement statement;
	
	private List<Class> list;
    public GetRooms() {
        super();
        
         statement = UtilCommonFunctions.initialize("ben", "");
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			list = queryTimetable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (list != null) {
			if (!list.isEmpty()) {
				Log.d(TAG, "Timetable list size: " + list.size());
				
				// Create a response
				ClassResponse classResponse = new ClassResponse();
				classResponse.setRooms(list);
				
				response.setContentType(Constants.APPLICATION_JSON);
				PrintWriter printWriter = response.getWriter();
				
				Gson gson = new Gson();
				if (list == null) {
					
					String jsonStr = gson.toJson(classResponse);
					printWriter.write(jsonStr);
				} else {
					String jsonTimeable = gson.toJson(classResponse);
					
					Log.d(TAG, jsonTimeable);
					
					printWriter.write(jsonTimeable);
				}
			}
		}
	}

	private List<Class> queryTimetable() throws SQLException {
		List<Class> classList = new ArrayList<>();
		
		String query = "SELECT * FROM " + Constants.TABLE_CLASSES
				+ " WHERE " + Constants.IS_REMOVED + "=" + false;
		
		Log.d(TAG, "Query: " + query);
		
		List<String> unitIdList = new ArrayList<>();
		
		// execute query
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Class room = new Class();
			room.setAvailability(resultSet.getBoolean(Constants.AVAILABILITY));
			room.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			room.setHall_id(resultSet.getString(Constants.HALL_ID));
			room.setId(resultSet.getString(Constants.CLASS_ID));
			room.setLab(resultSet.getBoolean(Constants.IS_LAB));
			room.setVolume(resultSet.getString(Constants.VOLUME));
			classList.add(room);
		}
		
		Log.d(TAG, "Size: " + classList.size());
		
		return classList;
	}
}
