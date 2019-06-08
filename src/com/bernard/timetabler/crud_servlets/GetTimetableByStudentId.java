package com.bernard.timetabler.crud_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.ClassUnit;
import com.bernard.timetabler.model.Timetable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetTimetable
 */
@WebServlet("/students/timetables/*")
public class GetTimetableByStudentId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private List<Timetable> list;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String studentId = request.getParameter(Constants.STUDENT_ID);
		
		list = queryTimetableByStudentId(studentId);
		
		// Create a response
		TimetableResponse timetableResponse = new TimetableResponse();
		timetableResponse.setClassUnits(list);
		
		response.setContentType(Constants.APPLICATION_JSON);
		PrintWriter printWriter = response.getWriter();
		
		Gson gson = new Gson();
		String jsonTimeable = gson.toJson(timetableResponse);
		
		printWriter.write(jsonTimeable);
	}
	
	private List<Timetable> queryTimetableByStudentId(String studentId) {
		List<Timetable> timetableList = new ArrayList<>();
		
		String query = "SELECT " + Constants.PERIOD + "," + Constants.TIME + "," + Constants.UNIT_ID +
				" FROM " + Constants.TABLE_TIMTABLE + " tt " +
				"INNER JOIN " + Constants.TABLE_STUDENT_UNITS + " su " +
				"ON tt." + Constants.UNIT_ID + "=su." + Constants.UNIT_ID +
				" WHERE " + Constants.STUDENT_ID + "='" + studentId + "'";
		
		return timetableList;
	}

	private class TimetableResponse {
		@SerializedName("timetables")
		private List<Timetable> timetables;
		
		public List<Timetable> getClassUnits() {
			return timetables;
		}
		
		public void setClassUnits(List<Timetable> timetables) {
			this.timetables = timetables;
		}
	}
}
