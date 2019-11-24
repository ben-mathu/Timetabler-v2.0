package com.bernard.timetabler.crud_servlets.users.admin;

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
import com.bernard.timetabler.dbinit.model.admin.Admin;
import com.bernard.timetabler.dbinit.model.admin.AdminRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class RegisterAdmin
 */
@WebServlet("/register_admin")
public class RegisterAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = RegisterAdmin.class.getSimpleName();
	
	private Statement statement;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter writer;
		
		String jsonRequest = BufferRequest.bufferRequest(request);
		
		Gson gson = new Gson();
		AdminRequest adminRequest = gson.fromJson(jsonRequest, AdminRequest.class);
		
		statement = UtilCommonFunctions.initialize("benard", adminRequest.getDbPassword());
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
					writer.write(jsonResponse);
				}
			}
		} catch (SQLException e) {
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

		return statement.executeUpdate(query) > 0;
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

		return statement.executeUpdate(query) == 0;
	}
}
