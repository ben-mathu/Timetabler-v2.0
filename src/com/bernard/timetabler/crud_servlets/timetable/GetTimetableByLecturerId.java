package com.bernard.timetabler.crud_servlets.timetable;

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

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.bernard.timetabler.dbinit.model.room.Class;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetTimetable
 */
@WebServlet("/lecturers/timetables/*")
public class GetTimetableByLecturerId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetTimetableByLecturerId.class.getSimpleName();
	
	private Statement statement;
	private Statement st;
	private List<Table> list;
	
	public GetTimetableByLecturerId() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		
		statement = ct.getStatement();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter(Constants.LECTURER_ID);
		
		Log.d(TAG, userId);
		
		try {
			list = queryTimetableByLecturerId(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (list != null) {
			if (!list.isEmpty()) {
				Log.d(TAG, "Timetable list size: " + list.size());
				
				// Create a response
				TimetableResponse timetableResponse = new TimetableResponse();
				timetableResponse.setTimetable(list);
				
				response.setContentType(Constants.APPLICATION_JSON);
				PrintWriter printWriter = response.getWriter();
				
				Gson gson = new Gson();
				String jsonTimeable = gson.toJson(timetableResponse);
				
				Log.d(TAG, jsonTimeable);
				
				printWriter.write(jsonTimeable);
			}
		}
	}
	
	private List<Table> queryTimetableByLecturerId(String userId) throws SQLException {
		List<Table> timetableList = new ArrayList<>();
		
		String query = "SELECT tt." + Constants.PERIOD + ",tt." + Constants.TIME + ",tt." + Constants.DAY + ",tt." + Constants.UNIT_ID +
				" FROM " + Constants.TABLE_TIMTABLE + " tt " +
				"INNER JOIN " + Constants.TABLE_LECTURER_UNITS + " tu " +
				"ON tt." + Constants.UNIT_ID + "=tu." + Constants.UNIT_ID +
				" WHERE tu." + Constants.LECTURER_ID + "='" + userId + "'";
		
		Log.d(TAG, "Query: " + query);
		
		List<String> unitIdList = new ArrayList<>();
		
		// execute query
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Table timetables = new Table();
			Timeslot slot = new Timeslot();
			slot.setPeriod(resultSet.getString(Constants.PERIOD));
			slot.setDay(resultSet.getString(Constants.DAY));
			slot.setTime(resultSet.getString(Constants.TIME));
			timetables.setTimeslot(slot);
			unitIdList.add(resultSet.getString(Constants.UNIT_ID));
			
			timetableList.add(timetables);
		}
		
		for (String unitId : unitIdList) {
			timetableList.get(unitIdList.indexOf(unitId)).setUnit(queryUnit(unitId));
			timetableList.get(unitIdList.indexOf(unitId)).setRoom(queryRoom(unitId));
		}
		
		Log.d(TAG, "Size: " + timetableList.size());
		
		return timetableList;
	}
	
	private Class queryRoom(String unitId) throws SQLException {
		Class room = new Class();
		
		String query = "SELECT * FROM " + Constants.TABLE_CLASSES + " tc "
				+ " INNER JOIN " + Constants.TABLE_CLASS_UNITS + " cu "
				+ "ON tc." + Constants.CLASS_ID + "=cu." + Constants.CLASS_ID
				+ " WHERE cu." + Constants.UNIT_ID + "='" + unitId + "'";
		
		Log.d(TAG, "Query: " + query);
		
		ResultSet result = statement.executeQuery(query);
		
		if (result.next()) {
			room.setAvailability(result.getBoolean(Constants.AVAILABILITY));
			room.setFacultyId(result.getString(Constants.FACULTY_ID));
			room.setHall_id(result.getString(Constants.HALL_ID));
			room.setId(result.getString(Constants.CLASS_ID));
			room.setLab(result.getBoolean(Constants.IS_LAB));
			room.setVolume(result.getString(Constants.VOLUME));
		}
		
		return room;
	}

	private Unit queryUnit(String unitId) throws SQLException {
		Unit unit = new Unit();
		
		String unitQueryString = "SELECT * FROM " + Constants.TABLE_UNITS +
				" WHERE " + Constants.UNIT_ID + "='" + unitId + "'";
		
		ResultSet resultSet = statement.executeQuery(unitQueryString);
		
		if (resultSet.next()) {
			unit.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			unit.setId(resultSet.getString(Constants.UNIT_ID));
			unit.setPractical(resultSet.getBoolean(Constants.IS_PRACTICAL));
			unit.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
			unit.setUnitName(resultSet.getString(Constants.UNIT_NAME));
		}
		
		return unit;
	}

	private class TimetableResponse {
		@SerializedName("timetables")
		private List<Table> timetables;
		
		public List<Table> getTimetable() {
			return timetables;
		}
		
		public void setTimetable(List<Table> timetables) {
			this.timetables = timetables;
		}
	}
	
	public class Table {
	    @SerializedName(Constants.TIMESLOT)
	    private Timeslot timeslot;
	    @SerializedName(Constants.UNIT)
	    private Unit unit;
	    @SerializedName("class")
	    private Class room;

	    public Timeslot getTimeslot() {
	        return timeslot;
	    }

	    public void setTimeslot(Timeslot timeslot) {
	        this.timeslot = timeslot;
	    }

	    public Unit getUnit() {
	        return unit;
	    }

	    public void setUnit(Unit unit) {
	        this.unit = unit;
	    }
	    
	    public void setRoom(Class room) {
			this.room = room;
		}
	    
	    public Class getRoom() {
			return room;
		}
	}
	
	public class Timeslot {
		@SerializedName("period")
		private String period;
	    @SerializedName("day")
	    private String day;
	    @SerializedName("time")
	    private String time;
	    
	    public String getPeriod() {
			return period;
		}
	    
	    public void setPeriod(String period) {
			this.period = period;
		}

	    public String getDay() {
	        return day;
	    }

	    public void setDay(String day) {
	        this.day = day;
	    }

	    public String getTime() {
	        return time;
	    }

	    public void setTime(String time) {
	        this.time = time;
	    }
	}
}
