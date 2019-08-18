package com.bernard.timetabler.crud_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.SuccessfulReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetRegistrationSchedule
 */
@WebServlet("/schedule-registration")
public class GetRegistrationSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetRegistrationSchedule.class.getSimpleName();

	private DeadlineSettings settings;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(Constants.APPLICATION_JSON);
		PrintWriter printWriter = response.getWriter();
		
		Gson gson = new Gson();
		try {
			settings = querySettings();
			
			String jsonSchedule = gson.toJson(settings);
			
			Log.d(TAG, jsonSchedule);
			
			printWriter.write(jsonSchedule);
		} catch (SQLException e) {
			Log.d(TAG, "Error: " + e.getLocalizedMessage() + "\n");
			e.printStackTrace();
			
			SuccessfulReport report = new SuccessfulReport();
			report.setMessage(e.getLocalizedMessage());
			printWriter.write(gson.toJson(report));
		}
	}
	
	private DeadlineSettings querySettings() throws SQLException {
		DeadlineSettings scheduleSettings = new DeadlineSettings();
		
		// set up database environement
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		
		Statement st = ct.getStatement();
		
		// write the query statement
		String query = "SELECT * FROM " + Constants.TABLE_SCHEDULE + " WHERE " + Constants.ACTIVITY + "=1";
		
		ResultSet resultSet = st.executeQuery(query);
		
		while (resultSet.next()) {
			scheduleSettings.setStartDate(resultSet.getString(Constants.STARTDATE));
			scheduleSettings.setDeadline(resultSet.getString(Constants.DEADLINE));
			scheduleSettings.setActive(resultSet.getBoolean(Constants.ACTIVITY));
		}
		return scheduleSettings;
	}

	public class DeadlineSettings {
	    @SerializedName("start_date")
	    private String startDate;
	    @SerializedName("deadline")
	    private String deadline;
	    @SerializedName("activity")
	    private boolean isActive = false;

	    public String getDeadline() {
	        return deadline;
	    }

	    public void setDeadline(String deadline) {
	        this.deadline = deadline;
	    }

	    public String getStartDate() {
	        return startDate;
	    }

	    public void setStartDate(String startDate) {
	        this.startDate = startDate;
	    }

	    public boolean isActive() {
	        return isActive;
	    }

	    public void setActive(boolean active) {
	        isActive = active;
	    }
	}
}
