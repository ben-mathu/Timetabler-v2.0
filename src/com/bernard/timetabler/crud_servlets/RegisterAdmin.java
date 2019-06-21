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
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class RegisterAdmin
 */
@WebServlet("/register_admin")
public class RegisterAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = RegisterAdmin.class.getSimpleName();
	
	private CreateSchemaTimeTabler ct;
	private Statement statement;
	
	public RegisterAdmin() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		response.setContentType("application/json");
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		Gson gson = new Gson();
		AdminRequest adminRequest = gson.fromJson(strBuffer.toString(), AdminRequest.class);
		
		ct = new CreateSchemaTimeTabler("benard", adminRequest.getDbPassword());
		
		statement = ct.getStatement();
		try {
			createAdminTableIfNotExist(Constants.TABLE_ADMIN);
			
			int count = updateTableAdmin(adminRequest.getAdmin());
			
			if (count == 0) {
				SuccessfulReport report = new SuccessfulReport();
				report.setMessage("Successfully created");
				
				writer.write(gson.toJson(report));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int updateTableAdmin(Admin admin) throws SQLException {
		String query = "INSERT INTO " + Constants.TABLE_ADMIN
				+ " VALUES ('" + admin.getAdminId() + "','"
				+ admin.getfName() + "','"
				+ admin.getmName() + "','"
				+ admin.getlName() + "','"
				+ admin.getUsername() + "','"
				+ admin.getPassword() + "')";
		
		return statement.executeUpdate(query);
	}

	private void createAdminTableIfNotExist(String tableAdmin) throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_ADMIN + "("
				+ Constants.ADMIN_ID + " VARCHAR(15) PRIMARY KEY,"
				+ Constants.F_NAME + " VARCHAR(15),"
				+ Constants.M_NAME + " VARCHAR(15),"
				+ Constants.L_NAME + " VARCHAR(15),"
				+ Constants.USERNAME + " VARCHAR(15),"
				+ Constants.PASSWORD + " VARCHAR(32) UNIQUE)";
		
		int count = statement.executeUpdate(query);
	}

	private class AdminRequest {
		@SerializedName("admin")
		private Admin admin;
		@SerializedName("password")
		private String dbPassword;
		
		public Admin getAdmin() {
			return admin;
		}
		
		public void setAdmin(Admin admin) {
			this.admin = admin;
		}
		
		public String getDbPassword() {
			return dbPassword;
		}
		
		public void setDbPassword(String dbPassword) {
			this.dbPassword = dbPassword;
		}
	}

}
