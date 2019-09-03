package com.bernard.timetabler.crud_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.Unit;
import com.bernard.timetabler.dbinit.model.UnitList;
import com.google.gson.Gson;

/**
 * Servlet implementation class DeleteUnitRecord
 */
@WebServlet("/delete-unit/*")
public class DeleteUnitRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DeleteUnitRecord.class.getSimpleName();
	
	private Statement statement;
	
	private UnitList list = new UnitList();

    public DeleteUnitRecord() {
        super();
        
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
        CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
        statement = ct.getStatement();
    }

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		list = gson.fromJson(strBuffer.toString(), UnitList.class);
		
		try {
			if (req.getParameterMap().containsKey(Constants.STUDENT_ID)) {
				String studentId = req.getParameter(Constants.STUDENT_ID);
				removeUnitByStudentId(studentId, resp);
			} else if (req.getParameterMap().containsKey(Constants.LECTURER_ID)) {
				String lecturerId = req.getParameter(Constants.LECTURER_ID);
				removeUnitByLecturerId(lecturerId, resp);
			} else {
				removeUnits(resp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void removeUnits(HttpServletResponse resp) throws SQLException, IOException {
		String query = "";
		for (Unit unit : list.getUnitList()) {
			query = "UPDATE " + Constants.TABLE_LECTURER_UNITS
					+ " SET " + Constants.IS_REMOVED + "=" + true
					+ " WHERE " + Constants.UNIT_ID + "='" + unit.getId() + "'";
			statement.executeUpdate(query);
		}
		
		for (Unit unit : list.getUnitList()) {
			query = "UPDATE " + Constants.TABLE_STUDENT_UNITS
					+ " SET " + Constants.IS_REMOVED + "=" + true
					+ " WHERE " + Constants.UNIT_ID + "='" + unit.getId() + "'";
			statement.executeUpdate(query);
		}
		String jsonStr = "";
		MessageReport report = new MessageReport();
		report.setMessage("Successfully removed");
		
		Gson gson = new Gson();
		jsonStr = gson.toJson(report);
		PrintWriter writer = resp.getWriter();
		writer.write(jsonStr);
	}

	private void removeUnitByLecturerId(String lecturerId, HttpServletResponse resp) throws SQLException, IOException {
		String query = "";
		for (Unit unit : list.getUnitList()) {
			query = "UPDATE " + Constants.TABLE_LECTURER_UNITS
					+ " SET " + Constants.IS_REMOVED + "=" + true
					+ " WHERE " + Constants.LECTURER_ID + "='" + lecturerId + "'"
					+ " AND " + Constants.UNIT_ID + "='" + unit.getId() + "'";
			statement.executeUpdate(query);
		}
		
		String jsonStr = "";
		MessageReport report = new MessageReport();
		report.setMessage("Successfully removed");
		
		Gson gson = new Gson();
		jsonStr = gson.toJson(report);
		PrintWriter writer = resp.getWriter();
		writer.write(jsonStr);
	}

	private void removeUnitByStudentId(String studentId, HttpServletResponse resp) throws SQLException, IOException {
		String query = "";
		for (Unit unit : list.getUnitList()) {
			query = "UPDATE " + Constants.TABLE_STUDENT_UNITS
					+ " SET " + Constants.IS_REMOVED + "=" + true
					+ " WHERE " + Constants.STUDENT_ID + "='" + studentId + "'"
					+ " AND " + Constants.UNIT_ID + "='" + unit.getId() + "'";
			statement.executeUpdate(query);
		}
		
		String jsonStr = "";
		MessageReport report = new MessageReport();
		report.setMessage("Successfully removed");
		
		Gson gson = new Gson();
		jsonStr = gson.toJson(report);
		PrintWriter writer = resp.getWriter();
		writer.write(jsonStr);
	}

}
