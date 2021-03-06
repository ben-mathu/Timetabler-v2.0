package com.bernard.timetabler.crud_servlets.cruds;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetTimetableByStudentId
 */
@WebServlet("/students/timetables/copy/*")
public class GetTimetableByStudentId_bak extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetTimetableByStudentId_bak.class.getSimpleName();
	
	private Statement statement;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TimetableResponse timetableResponse = new TimetableResponse();
		
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		
		initDb();
		
		try {
			timetableResponse.setTimetableList(getTimetableList(request.getParameter(Constants.STUDENT_ID)));
			String jsonTimetableResponse = "";
			
			Gson gson = new Gson();
			
			jsonTimetableResponse = gson.toJson(timetableResponse);
			writer.write(jsonTimetableResponse);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initDb() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
	}

	private List<Timetable> getTimetableList(String studentId) throws SQLException {
		List<Timetable> list = new ArrayList<>();
		String timetablesQuery = "SELECT * FROM " + Constants.TIMETABLES + " tt "
				+ " INNER JOIN " + Constants.TABLE_STUDENT_UNITS + " su "
				+ " ON su." + Constants.UNIT_ID + "=tt." + Constants.UNIT_ID
				+ " WHERE su." + Constants.STUDENT_ID + "='" + studentId + "'";
		
		ResultSet resultSet = statement.executeQuery(timetablesQuery);
		
		while (resultSet.next()) {
			Timetable timetable = new Timetable();
			Pattern pattern = Pattern.compile("^(\\w+)\\s+(.*)$");
			Matcher matcher = pattern.matcher(resultSet.getString(Constants.TIMESLOT));
			
			String day = "", time = "";
			while (matcher.find()) {
				day = matcher.group(1);
				time = matcher.group(2);
			}
			
			Unit unit = getUnit(resultSet.getString(Constants.UNIT_ID));
			timetable.setTimeslot(new Timeslot(day, time));
			timetable.setUnit(unit);
			list.add(timetable);
			
		}
		return list;
	}

	private Unit getUnit(String unitId) throws SQLException {
		String unitQuery = "SELECT * FROM " + Constants.TABLE_UNITS
				+ " WHERE " + Constants.UNIT_ID + "='" + unitId + "'";
		
		ResultSet resultSet = statement.executeQuery(unitQuery);
		
		Unit unit = new Unit();
		while (resultSet.next()) {
			unit.setId(resultSet.getString(Constants.UNIT_ID));
			unit.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			unit.setPractical(resultSet.getBoolean(Constants.IS_PRACTICAL));
			unit.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
			unit.setUnitName(resultSet.getString(Constants.UNIT_NAME));
		}
		return unit;
	}

	public class TimetableResponse {
	    @SerializedName("timetable")
	    private List<Timetable> timetableList;

	    public List<Timetable> getTimetableList() {
	        return timetableList;
	    }

	    public void setTimetableList(List<Timetable> timetableList) {
	        this.timetableList = timetableList;
	    }
	}
	
	public class Timetable {
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
	    @SerializedName("day")
	    private String day;
	    @SerializedName("time")
	    private String time;

	    public Timeslot(String day, String time) {
	        this.day = day;
	        this.time = time;
	    }

	    public Timeslot() {
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
