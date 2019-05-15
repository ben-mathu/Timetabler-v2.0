package com.bernard.timetabler.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

/**
 * Servlet implementation class Extreneous
 */
@WebServlet("/roles/*")
public class Extreneous extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = Extreneous.class.getSimpleName();
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	private Statement statement;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String saltStr = "";
		
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler();
		
		statement = ct.getStatement();
		// get role
		String role = req.getParameter(Constants.ROLE);
		
		// get role and salt from db
		try {
			saltStr = getSaltByRole(role);
		} catch (SQLException e) {
			Log.e(TAG, "" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		if (saltStr.isEmpty()) {
			byte[] salt = createSalt(role.length());
			
			resp.setContentType("application/json");
			
//			saltStr = new String(convertByteToString(salt));
			saltStr = new String(salt);
			
			// also you can do this
//			String s = Base64.getEncoder().encodeToString(salt);
		}
		
		try {
			saveSaltedRole(saltStr, role);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Convert to json format then send
		Gson gson = new Gson();
		SaltClass saltClass = new SaltClass();
		saltClass.setSalt(saltStr);
		saltStr = gson.toJson(saltClass);
		out.write(saltStr);
		
//		try {
//			Log.d(TAG, "" + saveRoleAndSaltCombo(saltStr, role));
//		} catch (SQLException e) {
//			Log.e(TAG, e.getLocalizedMessage());
//			e.printStackTrace();
//		}
	}
	
//	private int saveRoleAndSaltCombo(String saltStr, String role) throws SQLException {
//		String updateSmt = "INSERT INTO " + Constants.SALTROLE +
//				" VALUES ('" + saltStr + "','" + role + "'";
//		
//		return statement.executeUpdate(updateSmt);
//	}

	private class SaltClass {
		@SerializedName("salt")
		private String salt;
		
		public void setSalt(String salt) {
			this.salt = salt;
		}
		
		public String getSalt() {
			return salt;
		}
	}
	
	private String getSaltByRole(String role) throws SQLException {
		String query = "SELECT " + Constants.SALT +
				" FROM " + Constants.SALTROLE +
				" WHERE " + Constants.ROLE + "='" + role + "'";
		
		ResultSet resultSet = statement.executeQuery(query);
		String salt = "";
		while(resultSet.next()) {
			salt = resultSet.getString(Constants.SALT);
		}
		
		return salt;
	}

	private void saveSaltedRole(String saltStr, String role) throws SQLException {
		
		String strQuery = "INSERT INTO " + Constants.SALTROLE +
                " VALUES ('" + saltStr + "', '" + role + "')";
		int result = statement.executeUpdate(strQuery);
	}

	private char[] convertByteToString(byte[] salt) {
		String saltString = new String(salt);
		
		// get a string of bytes
//		char[] hexChars = new char[salt.length * 2];
//		for (int i = 0; i < salt.length; i++) {
//			int v = salt[i] & 0xFF;
//			hexChars[i * 2] = hexArray[v >> 4];
//			hexChars[i * 2 + 1] = hexArray[v & 0xFF];
//		}
		
		return new char[40];
	}

	private byte[] createSalt(int length) {
		byte[] byt = new byte[length];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(byt);
		return byt;
	}

}
