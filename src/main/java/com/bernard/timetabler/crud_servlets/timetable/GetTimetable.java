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

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.bernard.timetabler.dbinit.model.room.Class;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

/**
 * Servlet implementation class GetTimetable
 */
@WebServlet("/admin/timetables/*")
public class GetTimetable extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetTimetable.class.getSimpleName();
	
	private Statement statement;
	private Statement st;
	private List<Table> list;
	
	public GetTimetable() {
		CreateSchemaTimeTabler.setDatabase(DATABASE_NAME);
		
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		
		statement = ct.getStatement();
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
				TimetableResponse timetableResponse = new TimetableResponse();
				timetableResponse.setTimetable(list);
				
				response.setContentType(APPLICATION_JSON);
				PrintWriter printWriter = response.getWriter();
				
				Gson gson = new Gson();
				if (list == null) {
					MessageReport report = new MessageReport();
					report.setMessage("Timetable is empty");
					
					String jsonStr = gson.toJson(report);
					printWriter.write(jsonStr);
				} else {
					String jsonTimeable = gson.toJson(timetableResponse);
					
					Log.d(TAG, jsonTimeable);
					
					printWriter.write(jsonTimeable);
				}
			}
		}
	}
	
	private List<Table> queryTimetable() throws SQLException {
		List<Table> timetableList = new ArrayList();
		
		String query = "SELECT DISTINCT" + UNIT_ID + "," + PERIOD + "," + TIME + "," + DAY + "," +
				" FROM " + TABLE_TIMTABLE;
		
		Log.d(TAG, "Query: " + query);
		
		List<String> unitIdList = new ArrayList();
		
		// execute query
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Table timetables = new Table();
			Timeslot slot = new Timeslot();
			slot.setPeriod(resultSet.getString(PERIOD));
			slot.setDay(resultSet.getString(DAY));
			slot.setTime(resultSet.getString(TIME));
			timetables.setTimeslot(slot);
			unitIdList.add(resultSet.getString(UNIT_ID));
			
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
		
		String query = "SELECT * FROM " + TABLE_CLASSES + " tc "
				+ " INNER JOIN " + TABLE_CLASS_UNITS + " cu "
				+ "ON tc." + CLASS_ID + "=cu." + CLASS_ID
				+ " WHERE cu." + UNIT_ID + "='" + unitId + "'";
		
		Log.d(TAG, "Query: " + query);
		
		ResultSet result = statement.executeQuery(query);
		
		if (result.next()) {
			room.setAvailability(result.getBoolean(AVAILABILITY));
			room.setFacultyId(result.getString(FACULTY_ID));
			room.setHall_id(result.getString(HALL_ID));
			room.setId(result.getString(CLASS_ID));
			room.setLab(result.getBoolean(IS_LAB));
			room.setVolume(result.getString(VOLUME));
		}
		
		return room;
	}

	private Unit queryUnit(String unitId) throws SQLException {
		Unit unit = new Unit();
		
		String unitQueryString = "SELECT * FROM " + TABLE_UNITS +
				" WHERE " + UNIT_ID + "='" + unitId + "'";
		
		ResultSet resultSet = statement.executeQuery(unitQueryString);
		
		if (resultSet.next()) {
			unit.setFacultyId(resultSet.getString(FACULTY_ID));
			unit.setId(resultSet.getString(UNIT_ID));
			unit.setPractical(resultSet.getBoolean(IS_PRACTICAL));
			unit.setProgrammeId(resultSet.getString(PROGRAMME_ID));
			unit.setUnitName(resultSet.getString(UNIT_NAME));
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
	    @SerializedName(TIMESLOT)
	    private Timeslot timeslot;
	    @SerializedName(UNIT)
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
