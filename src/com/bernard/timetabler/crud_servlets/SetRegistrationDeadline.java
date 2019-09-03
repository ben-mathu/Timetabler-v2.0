package com.bernard.timetabler.crud_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private long remainder = 0;
	
    public SetRegistrationDeadline() {
    	CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
    	
    	ct = new CreateSchemaTimeTabler("ben", "");
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		response.setContentType("application/json");
		out = response.getWriter();
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		DeadlineRequest deadlineRequest = gson.fromJson(strBuffer.toString(), DeadlineRequest.class);
		
		try {
			saveSchedule(deadlineRequest);
			
			try {
				Date end = format.parse(deadlineRequest.getDeadline());
				
				String startDate = format.format(end.getTime() - TimeUnit.DAYS.toMillis(2));
				String endDate = deadlineRequest.getStartDate();
				
				saveScheduleLec(startDate, endDate, true);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageReport successfulReport = new MessageReport();
			successfulReport.setMessage("Unit Registration Schedule Set");
			
			String jsonResponse = gson.toJson(successfulReport);
			out.write(jsonResponse);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void saveSchedule(DeadlineRequest deadlineRequest) throws SQLException {
		boolean isActive = true;
		String insertStatement = "INSERT INTO " + Constants.TABLE_SCHEDULE + "(" 
				+ Constants.STARTDATE + ","
				+ Constants.DEADLINE + ","
				+ Constants.ACTIVITY + ")"
				+ " VALUES('" + deadlineRequest.getStartDate() + "','"
				+ deadlineRequest.getDeadline() + "', "
				+ isActive + ")";
		statement = ct.getStatement();
		statement.executeUpdate(insertStatement);
		
		// start timer
		
		ResultSet result = statement.executeQuery("SELECT LAST_INSERT_ID()");
		startTimer(deadlineRequest, result.next() ? result.getString("LAST_INSERT_ID()") : "");
	}
	
	private void saveScheduleLec(String startDate, String endDate, boolean isActive) throws SQLException {
		String insertStatement = "INSERT INTO " + Constants.TABLE_SCHEDULE_LEC + "(" 
				+ Constants.STARTDATE + ","
				+ Constants.DEADLINE + ","
				+ Constants.ACTIVITY + ")"
				+ " VALUES('" + startDate + "','"
				+ endDate + "', "
				+ isActive + ")";
		statement = ct.getStatement();
		statement.executeUpdate(insertStatement);
	}

	private void startTimer(DeadlineRequest deadlineRequest, String id) {
		// get timer remaining in 
		try {
			Date today = Calendar.getInstance().getTime();
			Date end = format.parse(deadlineRequest.getDeadline());
			
			Log.d(TAG, "time today" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(today));
			
			remainder = end.getTime() - today.getTime();
			Timer timer = new Timer();
			
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					remainder -= 1000;
					
					Log.d(TAG, "Timer" + remainder);
					if (remainder < 1000) {
						boolean isActive = false;
						String updateStatement = "UPDATE " + Constants.TABLE_SCHEDULE +
								" SET " + Constants.ACTIVITY + "=" + isActive +
								" WHERE " + Constants.SCHEDULE_ID + "='" + id + "'";
						
						try {
							statement = ct.getStatement();
							statement.executeUpdate(updateStatement);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Init.main(new String[]{});
						
						timer.cancel();
						timer.purge();
					}
				}
			}, 0, 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class DeadlineRequest {
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("deadline")
        private String deadline;

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
    }
}
