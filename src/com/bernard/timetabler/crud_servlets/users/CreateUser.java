package com.bernard.timetabler.crud_servlets.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.lecturer.PackageRequest;
import com.bernard.timetabler.dbinit.utils.GenerateRandomString;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class CreateUser
 */
@WebServlet("/create-user")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String TAG = CreateUser.class.getSimpleName();
    
    private Statement statement;
    
    public CreateUser() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(Constants.APPLICATION_JSON);
		PrintWriter writer = response.getWriter();
		
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		Log.d(TAG, "Buffered Line: " + jsonRequest);
		
		Gson gson = new Gson();
		PackageRequest req = gson.fromJson(jsonRequest, PackageRequest.class);
		
		try {
			String code = createLecturer(req);
			req.getLecRequest().setCode(code);
			req.getLecRequest().setMessage("Create an account with the access code below.");
			
			String jsonResp = gson.toJson(req);
			
			Log.d(TAG, "json String " + jsonResp);
			writer.write(jsonResp);
		} catch (SQLException e) {
//			SuccessfulReport report = new SuccessfulReport();
//			report.setMessage("username already in use, please use change you username");
//			writer.write(gson.toJson(report));
			writer.write("username already in use, please use change you username");
			e.printStackTrace();
		}
	}
	
	private String createLecturer(PackageRequest pacRequest) throws SQLException {
//		String code = GenerateAlphanumeric.generateIdAlphanumeric(7);
		GenerateRandomString randomStringGenerator = new GenerateRandomString(7);
		String code = randomStringGenerator.nextString();
		
		Pattern pattern = Pattern.compile("(\\w*|\\w*.\\w*)@.*$");
		Matcher matcher = pattern.matcher(pacRequest.getLecRequest().getEmail());

		String username = "";
		if (matcher.find()) {
			username = matcher.group(1);
			
			String query = "CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + code + "'";
			
			if (statement.executeUpdate(query) != 0) {
				String grant = "GRANT ALL ON " + Constants.DATABASE_NAME + ".* TO '" + username + "'@'localhost'";
				
				if (statement.executeUpdate(grant) != 0) {
					String insertQuery = "INSERT INTO " + Constants.TABLE_LECTURERS + "(" + Constants.LECTURER_ID + ","
							+ Constants.EMAIL + "," + Constants.F_NAME + "," + Constants.M_NAME + ","
							+ Constants.L_NAME + "," + Constants.IS_REMOVED
							+ ") VALUES ('" + code + "','" + pacRequest.getLecRequest().getEmail() + "','"
							+ pacRequest.getLecRequest().getFname() + "','" + pacRequest.getLecRequest().getMname() + "','"
							+ pacRequest.getLecRequest().getLname() + "'," + false + ")";
					
					statement.executeUpdate(insertQuery);
				}
			}
		}
		
		return code;
	}

}
