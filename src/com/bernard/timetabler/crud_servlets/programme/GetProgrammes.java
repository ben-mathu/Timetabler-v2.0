package com.bernard.timetabler.crud_servlets.programme;

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
import com.bernard.timetabler.dbinit.model.programme.Programme;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetProgrammes
 */
@WebServlet("/programmes/*")
public class GetProgrammes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Statement statement;
	
	private List<Programme> list;
	
	public GetProgrammes() {
		statement = UtilCommonFunctions.initialize("ben", "");
		
		list = new ArrayList<>();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		// Query campuses then stringify the object list
		String programmesJsonList = "";
		try {
			list = getProgrammes(request.getParameter(Constants.DEPARTMENT_ID));
			
			ProgrammesResponse resp = new ProgrammesResponse();
			resp.setProgrammes(list);
			
			Gson gson = new Gson();
			programmesJsonList = gson.toJson(resp, ProgrammesResponse.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PrintWriter writer = response.getWriter();
		
		writer.write(programmesJsonList);
	}
	
	private class ProgrammesResponse {
		@SerializedName("programmes")
		private List<Programme> programmes;
		
		public List<Programme> getProgrammes() {
			return programmes;
		}
		
		public void setProgrammes(List<Programme> programmes) {
			this.programmes = programmes;
		}
	}
		
	private List<Programme> getProgrammes(String departmentId) throws SQLException {
		List<Programme> programmeList = new ArrayList<>();
		// query
		String programmesQuery = "SELECT * FROM " + Constants.TABLE_PROGRAMMES +
				" WHERE " + Constants.DEPARTMENT_ID + "='" + departmentId + "'" 
				+ " AND " + Constants.IS_REMOVED + "=" + false;
		
		ResultSet resultSet = statement.executeQuery(programmesQuery);
		
		while (resultSet.next()) {
			Programme prog = new Programme();
			prog.setDepartmentId(resultSet.getString(Constants.DEPARTMENT_ID));
			prog.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			prog.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
			prog.setProgrammeName(resultSet.getString(Constants.PROGRAMME_NAME));
			programmeList.add(prog);
		}
		return programmeList;
	}

}
