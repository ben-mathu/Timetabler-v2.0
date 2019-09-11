package com.bernard.timetabler.crud_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.Hall;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetHalls
 */
@WebServlet("/halls/*")
public class GetHalls extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetHalls.class.getSimpleName();
    
	private List<Hall> halls;
	
	private Statement statement;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter(Constants.FACULTY_ID);
		
		try {
			String jsonhalls = getJsonString(getHallsByFacultyId(id));
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter out = response.getWriter();
			out.write(jsonhalls);
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	private String getJsonString(List<Hall> hallsByFacultyId) {
		Gson gson = new Gson();
		
		return new String(gson.toJson(new HallResponse(hallsByFacultyId)));
	}
	
	private class HallResponse {
		@SerializedName("halls")
		private List<Hall> halls;
		public HallResponse(List<Hall> halls) {
			this.halls = halls;
		}
	}

	private List<Hall> getHallsByFacultyId(String id) throws SQLException {
		halls = new ArrayList<>();
		initializeDb();
		
		String query = "SELECT * FROM " + Constants.TABLE_HALLS +
				" WHERE " + Constants.FACULTY_ID + "='" + id + "'";
		
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Hall hall = new Hall();
			hall.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			hall.setHallId(resultSet.getString(Constants.HALL_ID));
			hall.setHallName(resultSet.getString(Constants.HALL_NAME));
			halls.add(hall);
		}
		
		return halls;
	}

	private void initializeDb() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
	}

}
