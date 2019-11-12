package com.bernard.timetabler.crud_servlets.users;

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
import com.bernard.timetabler.dbinit.model.lecturer.Lecturer;
import com.bernard.timetabler.dbinit.model.lecturer.LecturerResponseList;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class GetLectures
 */
@WebServlet("/lecturers")
public class GetLecturers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetLecturers.class.getSimpleName();
	private Statement statement;
	
    public GetLecturers() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(Constants.APPLICATION_JSON);
		try {
			LecturerResponseList list = getList();
			
			Gson gson = new Gson();
			String jsonStr = gson.toJson(list);
			
			Log.d(TAG, "Json String: " + jsonStr);
			
			PrintWriter writer = response.getWriter();
			
			writer.write(jsonStr);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			LecturerResponseList items = new LecturerResponseList();
			Gson gson = new Gson();
			String jsonStr = gson.toJson(items);
			PrintWriter writer =  response.getWriter();
			writer.write(jsonStr);
		}
	}
	
	private LecturerResponseList getList() throws SQLException {
		LecturerResponseList list = new LecturerResponseList();
		List<Lecturer> lecturers = new ArrayList<>();
		String query = "SELECT * FROM " + Constants.TABLE_LECTURERS
				+ " WHERE " + Constants.IS_REMOVED + "=" + false;
		
		ResultSet result = statement.executeQuery(query);
		
		while (result.next()) {
			Lecturer lec = new Lecturer();
			lec.setId(result.getString(Constants.LECTURER_ID));
			lec.setFirstName(result.getString(Constants.F_NAME));
			lec.setMiddleName(result.getString(Constants.M_NAME));
			lec.setLastName(result.getString(Constants.L_NAME));
			lec.setUsername(result.getString(Constants.USERNAME));
			lec.setInSession(result.getBoolean(Constants.IN_SESSION));
			lec.setFacultyId(result.getString(Constants.FACULTY_ID));
			lec.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
			lecturers.add(lec);
		}
		
		list.setList(lecturers);
		
		return list;
	}
}
