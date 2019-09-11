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

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.utils.GenerateAlphanumeric;
import com.bernard.timetabler.dbinit.utils.GenerateRandomString;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

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
        
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
        
        CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
        statement = ct.getStatement();
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
			
			statement.executeUpdate(query);
			
			String grant = "GRANT ALL ON " + Constants.DATABASE_NAME + ".* TO '" + username + "'@'localhost'";
			
			statement.executeUpdate(grant);
		}
		
		String insertQuery = "INSERT INTO " + Constants.TABLE_LECTURERS + "(" + Constants.LECTURER_ID + ","
				+ Constants.EMAIL + "," + Constants.F_NAME + "," + Constants.M_NAME + ","
				+ Constants.L_NAME + ") VALUES ('" + code + "','" + pacRequest.getLecRequest().getEmail() + "','"
				+ pacRequest.getLecRequest().getFname() + "','" + pacRequest.getLecRequest().getMname() + "','"
				+ pacRequest.getLecRequest().getLname() + "')";
		
		statement.executeUpdate(insertQuery);
		
		return code;
	}
	
	private class PackageRequest {
		@SerializedName("package")
		private LecRequest lecRequest;
		
		public void setLecRequest(LecRequest lecRequest) {
			this.lecRequest = lecRequest;
		}
		
		public LecRequest getLecRequest() {
			return lecRequest;
		}
	}

	private class LecRequest {
		@SerializedName("message")
		private String message;
        @SerializedName("email")
        private String email;
        @SerializedName(Constants.F_NAME)
        private String fname;
        @SerializedName(Constants.M_NAME)
        private String mname;
        @SerializedName(Constants.L_NAME)
        private String lname;
        @SerializedName("access_code")
        private String code;

        public void setMessage(String message) {
			this.message = message;
		}
        
        public String getMessage() {
			return message;
		}
        
        public void setEmail(String email) {
			this.email = email;
		}
        
        public String getEmail() {
            return email;
        }

        public void setFname(String fname) {
			this.fname = fname;
		}
        
        public String getFname() {
            return fname;
        }

        public void setLname(String lname) {
			this.lname = lname;
		}
        
        public String getLname() {
            return lname;
        }
        
        public void setMname(String mname) {
			this.mname = mname;
		}

        public String getMname() {
            return mname;
        }
        
        public void setCode(String code) {
			this.code = code;
		}
        
        public String getCode() {
			return code;
		}
    }
}
