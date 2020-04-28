package com.bernard.timetabler.crud_servlets.faculty;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.bernard.timetabler.dbinit.model.faculty.FacultyList;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class GetFaculties
 */
@WebServlet("/faculties/*")
public class GetFacultiesByCampusId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private List<Faculty> faculties;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String campusId = request.getParameter(Constants.CAMPUS_ID);
		try {
			faculties = queryFaculties(campusId);
			FacultyList facultyList = new FacultyList();
			facultyList.setFacultyList(faculties);
			
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter printWriter = response.getWriter();
			
			Gson gson = new Gson();
			String jsonFaculties = gson.toJson(facultyList);
			
			printWriter.write(jsonFaculties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private List<Faculty> queryFaculties(String campusId) throws SQLException {
		Statement statement = UtilCommonFunctions.initialize("ben", "");
		
		List<Faculty> list = new ArrayList();
		
		String query = "SELECT * FROM " + Constants.TABLE_FACULTIES +
				" WHERE " + Constants.CAMPUS_ID + "='" + campusId + "'"
				+ " AND " + Constants.IS_REMOVED + "=" + false;
		
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Faculty faculty = new Faculty();
			faculty.setCampusId(resultSet.getString(Constants.CAMPUS_ID));
			faculty.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			faculty.setFacultyName(resultSet.getString(Constants.FACULTY_NAME));
			list.add(faculty);
		}
		return list;
	}

}
