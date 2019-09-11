package com.bernard.timetabler.crud_servlets.users;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.bernard.timetabler.dbinit.model.admin.Admin;
import com.bernard.timetabler.utils.BufferRequest;
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
		PrintWriter writer;
		
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		Gson gson = new Gson();
		AdminRequest adminRequest = gson.fromJson(jsonRequest, AdminRequest.class);
		
		ct = new CreateSchemaTimeTabler("benard", adminRequest.getDbPassword());
		
		statement = ct.getStatement();
		try {
			if (createAdminTableIfNotExist(Constants.TABLE_ADMIN)) {
				MessageReport report = new MessageReport();
				String jsonResponse = "";
				if (updateTableAdmin(adminRequest.getAdmin())) {
					
					report.setMessage("Successfully created");
					
					jsonResponse = gson.toJson(report);
					
					Log.d(TAG, jsonResponse);
					
					response.setStatus(201);
					writer = response.getWriter();
					writer.write(jsonResponse);
				} else {
					report.setMessage("Admin not created");
					
					jsonResponse = gson.toJson(report);
					
					response.setStatus(304);
					writer = response.getWriter();
					writer.write(jsonRequest);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean updateTableAdmin(Admin admin) throws SQLException {
		String query = "INSERT INTO " + Constants.TABLE_ADMIN
				+ " VALUES ('" + admin.getAdminId() + "','"
				+ admin.getfName() + "','"
				+ admin.getmName() + "','"
				+ admin.getlName() + "','"
				+ admin.getUsername() + "','"
				+ admin.getPassword() + "','"
				+ admin.getEmail() + "')";
		
		if (statement.executeUpdate(query) > 0) {
			return true;
		}
		
		return false;
	}

	private boolean createAdminTableIfNotExist(String tableAdmin) throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_ADMIN + "("
				+ Constants.ADMIN_ID + " VARCHAR(15) PRIMARY KEY,"
				+ Constants.F_NAME + " VARCHAR(15),"
				+ Constants.M_NAME + " VARCHAR(15),"
				+ Constants.L_NAME + " VARCHAR(15),"
				+ Constants.USERNAME + " VARCHAR(15) UNIQUE,"
				+ Constants.PASSWORD + " VARCHAR(32) UNIQUE,"
				+ Constants.EMAIL + " VARCHAR(255) UNIQUE)";
		
		if (statement.executeUpdate(query) == 0) {
			return true;
		}
		
		return false;
	}

	private class AdminRequest {
		@SerializedName("admin")
		private Admin admin;
		@SerializedName("password")
		private String dbPassword;
		
		public Admin getAdmin() {
			return admin;
		}
		
		@SuppressWarnings("unused")
		public void setAdmin(Admin admin) {
			this.admin = admin;
		}
		
		public String getDbPassword() {
			return dbPassword;
		}
		
		@SuppressWarnings("unused")
		public void setDbPassword(String dbPassword) {
			this.dbPassword = dbPassword;
		}
	}

}
