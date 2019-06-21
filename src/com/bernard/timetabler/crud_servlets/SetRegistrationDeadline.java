package com.bernard.timetabler.crud_servlets;

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

import com.bernard.timetabler.crud_servlets.reponses.SuccessfulReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class SetRegistrationDeadline
 */
@WebServlet("/set-registration-date")
public class SetRegistrationDeadline extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = SetRegistrationDeadline.class.getSimpleName();
	
	private Statement statement;
	private PrintWriter out;
	
	private CreateSchemaTimeTabler ct;
	
    public SetRegistrationDeadline() {
    	CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
    	
    	ct = new CreateSchemaTimeTabler("ben", "");
    	statement = ct.getStatement();
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
			
			SuccessfulReport successfulReport = new SuccessfulReport();
			successfulReport.setMessage("Unit Registration Schedule Set");
			
			String jsonResponse = gson.toJson(successfulReport);
			out.write(jsonResponse);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void saveSchedule(DeadlineRequest deadlineRequest) throws SQLException {
		String insertStatement = "INSERT INTO TABLE " + Constants.TABLE_SCHEDULE +
				" VALUES('" + deadlineRequest.getStartDate() + "','" + deadlineRequest.getDeadline() + "')";
		
		statement.executeUpdate(insertStatement);
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
