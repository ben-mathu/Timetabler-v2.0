package com.bernard.timetabler.crud_servlets.course;

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

import com.bernard.timetabler.crud_servlets.cruds.GetUnitsMethods;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetRegisteredUnits
 */
@WebServlet("/registered-units/*")
public class GetRegisteredUnits extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String TAG = GetRegisteredUnits.class.getSimpleName();
    
    private Statement statement;
    
    private GetUnitsMethods getUnitsMethods = new GetUnitsMethods();
    
    private String studentId = "", lecturerId = "";
    public GetRegisteredUnits() {
        super();
        
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
    	
    	CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
    	statement = ct.getStatement();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(Constants.APPLICATION_JSON);
		if (request.getParameterMap().containsKey(Constants.STUDENT_ID)) {
			// get student id
			String studentId = request.getParameter(Constants.STUDENT_ID);
			try {
				List<Unit> units = getUnitsMethods.getUnitsByStudentId(studentId);
				UnitContainer container = new UnitContainer();
				container.setUnitList(units);
				
				Gson gson = new Gson();
				String json = gson.toJson(container);
				
				PrintWriter writer = response.getWriter();
				
				writer.write(json);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (request.getParameterMap().containsKey(Constants.LECTURER_ID)) {
			String lecturerId = request.getParameter(Constants.LECTURER_ID);
			try {
				List<Unit> units = getUnitsMethods.getUnitsByLecturerId(lecturerId);
				UnitContainer container = new UnitContainer();
				container.setUnitList(units);
				
				Gson gson = new Gson();
				String json = gson.toJson(container);
				
				PrintWriter writer = response.getWriter();
				
				writer.write(json);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				List<Unit> units = getUnits();
				UnitContainer container = new UnitContainer();
				container.setUnitList(units);
				
				Gson gson = new Gson();
				String json = gson.toJson(container);
				
				PrintWriter writer = response.getWriter();
				
				writer.write(json);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private List<Unit> getUnits() throws SQLException {
		List<Unit> unitList = new ArrayList<>();
		String query = "SELECT DISTINCT un." + Constants.UNIT_ID + ",un." + Constants.UNIT_NAME
				+ ", un." + Constants.PROGRAMME_ID + ",un." + Constants.FACULTY_ID
				+ ",un." + Constants.IS_PRACTICAL + ",un." + Constants.DEPARTMENT_ID
				+ ",un." + Constants.IS_COMMON
				+ " FROM " + Constants.TABLE_UNITS + " un"
				+ " INNER JOIN " + Constants.TABLE_STUDENT_UNITS + " su "
				+ "ON un." + Constants.UNIT_ID + "=su." + Constants.UNIT_ID
				+ " INNER JOIN " + Constants.TABLE_LECTURER_UNITS + " lu "
				+ "ON un." + Constants.UNIT_ID + "=lu." + Constants.UNIT_ID
				+ " WHERE lu." + Constants.IS_REMOVED + "=" + 0
				+ " AND su." + Constants.IS_REMOVED + "=" + 0;
		
		ResultSet result = statement.executeQuery(query);
		
		while (result.next()) {
			Unit unit = new Unit();
			unit.setId(result.getString(Constants.UNIT_ID));
			unit.setUnitName(result.getString(Constants.UNIT_NAME));
			unit.setProgrammeId(result.getString(Constants.PROGRAMME_ID));
			unit.setFacultyId(result.getString(Constants.FACULTY_ID));
			unit.setPractical(result.getBoolean(Constants.IS_PRACTICAL));
			unit.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
			unit.setCommon(result.getBoolean(Constants.IS_COMMON));
			unitList.add(unit);
		}
		
		return unitList;
	}

	private class UnitContainer {
		@SerializedName("units")
		private List<Unit> unitList;
		
		public void setUnitList(List<Unit> unitList) {
			this.unitList = unitList;
		}
		
		public List<Unit> getUnitList() {
			return unitList;
		}
	}
}
