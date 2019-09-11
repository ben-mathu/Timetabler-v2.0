package com.bernard.timetabler.crud_servlets.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.cruds.GetUnitsMethods;
import com.bernard.timetabler.crud_servlets.reponses.ErrorReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.Unit;
import com.bernard.timetabler.dbinit.model.UnitList;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class GetUnits
 * handle all instances of units
 */
@WebServlet("/units-on-offer/*")
public class GetUnitsOnOffer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetUnitsOnOffer.class.getSimpleName();
	
	private PrintWriter out = null;
	private Gson gson;
	
	private GetUnitsMethods unitCrudOps = new GetUnitsMethods();
	private UnitList unitListJson;
	
	private List<Unit> unitList = null;
	
	public GetUnitsOnOffer() {
		gson = new GsonBuilder().create();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		out = response.getWriter();

		String param = request.getParameter(Constants.STUDENT_ID);
		
		try {
			if (request.getParameterMap().containsKey(Constants.STUDENT_ID)) {
				handleGetUnitsByStudentId(request, response);
			} else if (request.getParameterMap().containsKey(Constants.LECTURER_ID)) {
				handleGetUnitsByLecturerId(request, response);
			} else if (request.getParameterMap().containsKey(Constants.PROGRAMME_ID)) {
				handleGetUnitsByProgrammeId(request, response);
			} else if (request.getParameterMap().containsKey(Constants.DEPARTMENT_ID)) {
				handleGetUnitsByDepartmentId(request, response);
			}
		} catch (Throwable t) {
			Log.e(TAG, "Error " + t);
			t.printStackTrace();
		}
	}
	
	/**
	 * Handle CRUD operation to get all units based on a chosen department
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 */
	private void handleGetUnitsByDepartmentId(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String departmentId = request.getParameter(Constants.DEPARTMENT_ID);
		
		if (departmentId != null && !departmentId.equals("")) {
			// Get units based on department id
			unitList = new ArrayList<>();
			unitList = unitCrudOps.getUnitsByDepartment(departmentId);
			
			unitListJson = new UnitList();
			unitListJson.setUnitList(unitList);
			
			String jsonUnitsStr = gson.toJson(unitListJson);
			out.write(jsonUnitsStr);
		} else {
			ErrorReport errorReport = new ErrorReport();
			errorReport.setErrorMessage("Bad request!");
			
			String responseStr = gson.toJson(errorReport);
			out.write(responseStr);
		}
	}

	/**
	 * Handle CRUD operation to get all units based on a chosen programme
	 * 
	 * @param req
	 * @param res
	 * @throws Throwable
	 */
	private void handleGetUnitsByProgrammeId(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		String programmeId = req.getParameter(Constants.PROGRAMME_ID);
		
		if (programmeId != null && !programmeId.equals("")) {
			// get units based on student id
			unitList = new ArrayList<>();
			unitList = unitCrudOps.getUnitsByProgrammeId(programmeId);
			
			unitListJson = new UnitList();
			unitListJson.setUnitList(unitList);
			
			String jsonUnitsStr = gson.toJson(unitListJson);
			out.write(jsonUnitsStr);
		} else {
			// implement an error message
			ErrorReport errorReport = new ErrorReport();
			errorReport.setErrorMessage("Bad request!");
			
			String responseStr = gson.toJson(errorReport);
			out.write(responseStr);
		}
	}

	/**
	 * Handle CRUD operation to get units taught by a lecturer
	 * 
	 * @param req
	 * @param res
	 * @throws Throwable
	 */
	private void handleGetUnitsByLecturerId(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		String lecturerId = req.getParameter(Constants.LECTURER_ID);
		
		if (lecturerId != null && !lecturerId.equals("")) {
			// get units based on student id
			unitList = new ArrayList<>();
			unitList = unitCrudOps.getUnitsByLecturerId(lecturerId);
			
			unitListJson = new UnitList();
			unitListJson.setUnitList(unitList);
			
			String jsonUnitsStr = gson.toJson(unitListJson);
			out.write(jsonUnitsStr);
		} else {
			// implement an error message
			ErrorReport errorReport = new ErrorReport();
			errorReport.setErrorMessage("Bad request!");
			
			String responseStr = gson.toJson(errorReport);
			out.write(responseStr);
		}
	}

	/**
	 * Handle CRUD operation to get units registered by student
	 * 
	 * @param req to handle requests from clients
	 * @param resp To handle responses back to the client
	 * @throws Throwable handle errors
	 */
	public void handleGetUnitsByStudentId(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		String studentId = req.getParameter(Constants.STUDENT_ID);
		
		if (studentId != null && !studentId.equals("")) {
			// get units based on student id
			unitList = new ArrayList<>();
			unitList = unitCrudOps.getUnitsByStudentId(studentId);
			
			unitListJson = new UnitList();
			unitListJson.setUnitList(unitList);
			
			String jsonUnitsStr = gson.toJson(unitListJson);
			out.write(jsonUnitsStr);
			
		} else {
			// implement an error message
			ErrorReport errorReport = new ErrorReport();
			errorReport.setErrorMessage("Bad request!");
			
			String responseStr = gson.toJson(errorReport);
			out.write(responseStr);
		}
	}
}
