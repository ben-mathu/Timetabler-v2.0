package com.bernard.timetabler.crud_servlets;

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
import com.bernard.timetabler.dbinit.model.ClassUnit;
import com.bernard.timetabler.dbinit.model.Timetable;
import com.bernard.timetabler.dbinit.model.Unit;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetTimetable
 */
@WebServlet("/students/timetables/*")
public class GetTimetableByStudentId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetTimetableByStudentId.class.getSimpleName();
	
	private Statement statement;
	private List<Table> list;
	
	public GetTimetableByStudentId() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler();
		
		statement = ct.getStatement();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String studentId = request.getParameter(Constants.STUDENT_ID);
		
		Log.d(TAG, studentId);
		
		try {
			list = queryTimetableByStudentId(studentId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d(TAG, "Timetable list size: " + list.size());
		
		// Create a response
		TimetableResponse timetableResponse = new TimetableResponse();
		timetableResponse.setClassUnits(list);
		
		response.setContentType(Constants.APPLICATION_JSON);
		PrintWriter printWriter = response.getWriter();
		
		Gson gson = new Gson();
		String jsonTimeable = gson.toJson(timetableResponse);
		
		Log.d(TAG, jsonTimeable);
		
		printWriter.write(jsonTimeable);
	}
	
	private List<Table> queryTimetableByStudentId(String studentId) throws SQLException {
		List<Table> timetableList = new ArrayList<>();
		
		String query = "SELECT " + Constants.PERIOD + "," + Constants.TIME + "," + Constants.DAY + ",tt." + Constants.UNIT_ID +
				" FROM " + Constants.TABLE_TIMTABLE + " tt " +
				"INNER JOIN " + Constants.TABLE_STUDENT_UNITS + " su " +
				"ON tt." + Constants.UNIT_ID + "=su." + Constants.UNIT_ID +
				" WHERE " + Constants.STUDENT_ID + "='" + studentId + "'";
		
		Log.d(TAG, "Query: " + query);
		
		// execute query
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Table timetables = new Table();
			Timeslot slot = new Timeslot();
			slot.setPeriod(resultSet.getString(Constants.PERIOD));
			slot.setDay(resultSet.getString(Constants.DAY));
			slot.setTime(resultSet.getString(Constants.TIME));
			timetables.setTimeslot(slot);
			timetables.setUnit(queryUnits(resultSet.getString(Constants.UNIT_ID)));
			
			timetableList.add(timetables);
		}
		
		Log.d(TAG, "" + timetableList.size());
		
		return timetableList;
	}

	private Unit queryUnits(String unitId) throws SQLException {
		Unit unit = new Unit();
		
		String unitQueryString = "SELECT * FROM " + Constants.TABLE_UNITS +
				" WHERE " + Constants.UNIT_ID + "='" + unitId + "'";
		
		ResultSet resultSet = statement.executeQuery(unitQueryString);
		
		while (resultSet.next()) {
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
		
		public List<Table> getClassUnits() {
			return timetables;
		}
		
		public void setClassUnits(List<Table> timetables) {
			this.timetables = timetables;
		}
	}
	
	public class Table {
	    @SerializedName(Constants.TIMESLOT)
	    private Timeslot timeslot;
	    @SerializedName(Constants.UNIT)
	    private Unit unit;

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
