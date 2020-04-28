package com.bernard.timetabler.crud_servlets.hall;

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

import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.hall.Hall;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

/**
 * Servlet implementation class GetHalls
 */
@WebServlet("/halls/*")
public class GetHallsByFacultyId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetHallsByFacultyId.class.getSimpleName();
    
	private List<Hall> halls;
	
	private Statement statement;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter(FACULTY_ID);
		
		try {
			String jsonhalls = getJsonString(getHallsByFacultyId(id));
			response.setContentType(APPLICATION_JSON);
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
		halls = new ArrayList();
		initializeDb();
		
		String query = "SELECT * FROM " + TABLE_HALLS +
				" WHERE " + FACULTY_ID + "='" + id + "'";
		
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Hall hall = new Hall();
			hall.setFacultyId(resultSet.getString(FACULTY_ID));
			hall.setHallId(resultSet.getString(HALL_ID));
			hall.setHallName(resultSet.getString(HALL_NAME));
			halls.add(hall);
		}
		
		return halls;
	}

	private void initializeDb() {
		CreateSchemaTimeTabler.setDatabase(DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
	}

}
