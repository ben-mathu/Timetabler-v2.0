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

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
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

	private Statement st;
	private DeadlineSettings settings;
	
	public GetRegistrationSchedule() {
		// set up database environement
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		
		st = ct.getStatement();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameterMap().containsKey(Constants.LECTURER_ID)) {
			String userId = request.getParameter(Constants.LECTURER_ID);
			
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter printWriter = response.getWriter();
			
			Gson gson = new Gson();
			try {
				settings = querySettingsLec();
				
				String jsonSchedule = gson.toJson(settings);
				
				Log.d(TAG, jsonSchedule);
				
				printWriter.write(jsonSchedule);
			} catch (SQLException e) {
				Log.d(TAG, "Error: " + e.getLocalizedMessage() + "\n");
				e.printStackTrace();
				
				MessageReport report = new MessageReport();
				report.setMessage(e.getLocalizedMessage());
				printWriter.write(gson.toJson(report));
			}
		} else {
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
				
				MessageReport report = new MessageReport();
				report.setMessage(e.getLocalizedMessage());
				printWriter.write(gson.toJson(report));
			}
		}
		
	}
	
	private DeadlineSettings querySettingsLec() throws SQLException {
		DeadlineSettings scheduleSettings = new DeadlineSettings();
		
		// write the query statement
		String query = "SELECT * FROM " + Constants.TABLE_SCHEDULE_LEC + " WHERE " + Constants.ACTIVITY + "=1";
		
		ResultSet resultSet = st.executeQuery(query);
		
		while (resultSet.next()) {
			scheduleSettings.setStartDate(resultSet.getString(Constants.STARTDATE));
			scheduleSettings.setDeadline(resultSet.getString(Constants.DEADLINE));
			scheduleSettings.setActive(resultSet.getBoolean(Constants.ACTIVITY));
		}
		return scheduleSettings;
	}

	private DeadlineSettings querySettings() throws SQLException {
		DeadlineSettings scheduleSettings = new DeadlineSettings();
		
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
	    @SerializedName(Constants.STARTDATE)
	    private String startDate;
	    @SerializedName(Constants.DEADLINE)
	    private String deadline;
	    @SerializedName(Constants.ACTIVITY)
	    private boolean isActive = false;

	    public String getDeadline() {
	        return deadline;
	    }

	    void setDeadline(String deadline) {
	        this.deadline = deadline;
	    }

	    public String getStartDate() {
	        return startDate;
	    }

	    void setStartDate(String startDate) {
	        this.startDate = startDate;
	    }

	    public boolean isActive() {
	        return isActive;
	    }

	    void setActive(boolean active) {
	        isActive = active;
	    }
	}
}
