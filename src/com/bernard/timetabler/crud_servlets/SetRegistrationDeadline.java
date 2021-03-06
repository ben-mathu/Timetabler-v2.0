package com.bernard.timetabler.crud_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.Init;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class SetRegistrationDeadline
 */
@WebServlet("/set-registration-date")
public class SetRegistrationDeadline extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = SetRegistrationDeadline.class.getSimpleName();
	private static final String THREAD_NAME = "timer";
	
	private Statement statement;
	private PrintWriter out;
	
	private CreateSchemaTimeTabler ct;
	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private long remainder = 0;
	private String lecTimeId;

	public SetRegistrationDeadline() {
    	CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
    	
    	ct = new CreateSchemaTimeTabler("ben", "");
    }

    @Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		
		response.setContentType("application/json");
		out = response.getWriter();
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		DeadlineRequest deadlineRequest = gson.fromJson(sb.toString(), DeadlineRequest.class);
		
		try {
			saveSchedule(deadlineRequest);
			
			scheduleLecUnitRegistration(deadlineRequest);
			
			MessageReport successfulReport = new MessageReport();
			successfulReport.setMessage("Unit Registration Schedule Set");
			
			String jsonResponse = gson.toJson(successfulReport);
			out.write(jsonResponse);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Schedule unit registration time for lecturers
	 * @param deadlineRequest object stores start date and deadline.
	 * @see DeadlineRequest
	 *
	 * @throws SQLException thrown when query is incorrect or execution failed.
	 */
	private void scheduleLecUnitRegistration(DeadlineRequest deadlineRequest) throws SQLException {
		try {
			Date end = format.parse(deadlineRequest.getDeadline());

			// change the start date to be deadline for lecturer to register
			String startDate = format.format(end.getTime() - TimeUnit.DAYS.toMillis(2));
			String endDate = deadlineRequest.getStartDate();

			saveScheduleLec(startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void saveSchedule(DeadlineRequest deadlineRequest) throws SQLException {
		String insertStatement = "INSERT INTO " + Constants.TABLE_SCHEDULE + "(" 
				+ Constants.STARTDATE + ","
				+ Constants.DEADLINE + ","
				+ Constants.ACTIVITY + ")"
				+ " VALUES('" + deadlineRequest.getStartDate() + "','"
				+ deadlineRequest.getDeadline() + "', "
				+ true + ")";
		statement = ct.getStatement();
		statement.executeUpdate(insertStatement);
		
		// start timer
		
		ResultSet result = statement.executeQuery("SELECT LAST_INSERT_ID()");
		startTimer(deadlineRequest, result.next() ? result.getString("LAST_INSERT_ID()") : "");
	}
	
	private void saveScheduleLec(String startDate, String endDate) throws SQLException {
		String insertStatement = "INSERT INTO " + Constants.TABLE_SCHEDULE_LEC + "(" 
				+ Constants.STARTDATE + ","
				+ Constants.DEADLINE + ","
				+ Constants.ACTIVITY + ")"
				+ " VALUES('" + startDate + "','"
				+ endDate + "', "
				+ true + ")";
		statement = ct.getStatement();
		statement.executeUpdate(insertStatement);

		ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
		lecTimeId = resultSet.next() ? resultSet.getString("LAST_INSERT_ID()") : "";
	}

	private void startTimer(DeadlineRequest deadlineRequest, String id) {
		// get timer remaining in 
		try {
			Date today = Calendar.getInstance().getTime();
			Date end = format.parse(deadlineRequest.getDeadline());
			
			Log.d(TAG, "time today" + new SimpleDateFormat(Constants.DATE_FORMAT).format(today));
			
			remainder = end.getTime() - today.getTime();
			Timer timer = new Timer();
			
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					remainder -= 1000;
					
					Log.d(TAG, "Timer " + remainder);
					deactivateSchedule(timer, id);
				}
			}, 0, 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void deactivateSchedule(Timer timer, String id) {
		if (remainder < 1000) {
			boolean isActive = false;
			String updateStatement = "UPDATE " + Constants.TABLE_SCHEDULE +
					" SET " + Constants.ACTIVITY + "=" + isActive +
					" WHERE " + Constants.SCHEDULE_ID + "='" + id + "'";

			String update = "UPDATE " + Constants.TABLE_SCHEDULE_LEC +
					" SET " + Constants.ACTIVITY + "=" + isActive +
					" WHERE " + Constants.SCHEDULE_ID + "='" + lecTimeId + "'";

			try {
				statement = ct.getStatement();
				statement.executeUpdate(updateStatement);

				statement.executeUpdate(update);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			Init.main(new String[]{});

			timer.cancel();
			timer.purge();
		}
	}

	public class DeadlineRequest {
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("deadline")
        private String deadline;

        String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    }
}
