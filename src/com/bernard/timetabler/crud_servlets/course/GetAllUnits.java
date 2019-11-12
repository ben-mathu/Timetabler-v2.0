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
@WebServlet("/units")
public class GetAllUnits extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String TAG = GetAllUnits.class.getSimpleName();
    
    private Statement statement;
    
    private GetUnitsMethods getUnitsMethods = new GetUnitsMethods();
    
    private String studentId = "", lecturerId = "";
    public GetAllUnits() {
        super();
        
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
    	
    	CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
    	statement = ct.getStatement();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(Constants.APPLICATION_JSON);
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

	private List<Unit> getUnits() throws SQLException {
		List<Unit> unitList = new ArrayList<>();
		String query = "SELECT * FROM " + Constants.TABLE_UNITS;
		
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
