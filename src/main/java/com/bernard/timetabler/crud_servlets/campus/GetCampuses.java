package com.bernard.timetabler.crud_servlets.campus;

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
import com.bernard.timetabler.dbinit.model.campus.Campus;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

/**
 * Servlet implementation class GetCampuses
 */
@WebServlet("/campuses")
public class GetCampuses extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Statement statement;
	
	private List<Campus> list;
	
	public GetCampuses() {
		// initialize the db
		CreateSchemaTimeTabler.setDatabase(DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
		
		list = new ArrayList();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(APPLICATION_JSON);
		
		// Query campuses then stringify the object list
		String campusesJsonList = "";
		try {
			list = getCampuses();
			
			CampusResponse resp = new CampusResponse();
			resp.setCampuses(list);
			
			Gson gson = new Gson();
			campusesJsonList = gson.toJson(resp, CampusResponse.class);
			
			PrintWriter writer = response.getWriter();
			writer.write(campusesJsonList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private class CampusResponse {
		@SerializedName("campuses")
		private List<Campus> campuses;
		
		public List<Campus> getCampuses() {
			return campuses;
		}
		
		public void setCampuses(List<Campus> campuses) {
			this.campuses = campuses;
		}
	}

	private List<Campus> getCampuses() throws SQLException {
		List<Campus> campusList = new ArrayList();
		// query
		String campusQuery = "SELECT * FROM " + TABLE_CAMPUS
				+ " WHERE " + IS_REMOVED + "=" + false;
		
		ResultSet resultSet = statement.executeQuery(campusQuery);
		
		while (resultSet.next()) {
			Campus campus = new Campus();
			campus.setCampusId(resultSet.getString(CAMPUS_ID));
			campus.setCampusName(resultSet.getString(CAMPUS_NAME));
			campusList.add(campus);
		}
		return campusList;
	}

}
