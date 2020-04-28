package com.bernard.timetabler.crud_servlets.course;

import java.io.BufferedReader;
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
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.bernard.timetabler.dbinit.model.course.UnitReq;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

import static com.bernard.timetabler.dbinit.Constants.TABLE_UNITS;

/**
 * Servlet implementation class AddCourse
 */
@WebServlet("/add-unit")
public class AddCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = AddCourse.class.getSimpleName();
	private Statement statement;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		Gson gson = new Gson();
		UnitReq unitReq = gson.fromJson(strBuffer.toString(), UnitReq.class);
		
		String passCode = unitReq.getPassCode();
		inidb(passCode);
		
		try {
			if (addUnit(unitReq.getUnit())) {
				MessageReport report = new MessageReport();
				report.setMessage("Successfully added unit" + unitReq.getUnit().getUnitName());
				
				writer.write(gson.toJson(report));
			} else {
				MessageReport report = new MessageReport();
				report.setMessage("Could not add unit" + unitReq.getUnit().getUnitName());
				
				writer.write(gson.toJson(report));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean addUnit(Unit unit) throws SQLException {
		String strQuery = "INSERT INTO " + TABLE_UNITS +
                " VALUES ('" + unit.getId() +
                "', '" + unit.getUnitName() +
                "', '" + unit.getProgrammeId() +
                "', '" + unit.getFacultyId() +
                "', '" + unit.getDepartmentId() +
                "', " + unit.isPractical() +
                ", " + unit.isCommon() +
                ", " + unit.isRemoved() +
                ")";
		if (statement.executeUpdate(strQuery) != 0)
			return true;
		else
			return false;
	}

	private void inidb(String passCode) {
		statement = UtilCommonFunctions.initialize("benard", passCode);
	}
}
