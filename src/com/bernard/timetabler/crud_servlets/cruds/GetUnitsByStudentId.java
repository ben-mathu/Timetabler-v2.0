package com.bernard.timetabler.crud_servlets.cruds;

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
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.Unit;
import com.bernard.timetabler.dbinit.model.UnitList;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetUnitsByLecturerId
 */
@WebServlet("/students/units/*")
public class GetUnitsByStudentId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CreateSchemaTimeTabler ct;
	private Statement statement;
	
	private List<Unit> unitList;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		unitList= new ArrayList<>();
		response.setContentType("application/json");
		
		// Initialize db
		initDb();
		
		try {
			String id = request.getParameter(Constants.STUDENT_ID);
			getUnitsByLecturerId(id);
			
			// Prepare for response
			UnitResponse unitRespone = new UnitResponse();
			unitRespone.setItems(unitList);
			
			Gson gson = new Gson();
			
			String jsonResponse = gson.toJson(unitRespone, UnitResponse.class);
			
			PrintWriter writer = response.getWriter();
			writer.write(jsonResponse);
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initDb() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		ct = new CreateSchemaTimeTabler();
		statement = ct.getStatement();
	}

	private void getUnitsByLecturerId(String studentId) throws SQLException {
		
		List<Unit> units = new ArrayList<Unit>();
		
		String unitQuery = "SELECT * FROM " + Constants.TABLE_UNITS + " un"
						+ " INNER JOIN " + Constants.TABLE_STUDENT_UNITS + " su"
						+ " ON un." + Constants.UNIT_ID + "=su." + Constants.UNIT_ID
						+ " WHERE su." + Constants.STUDENT_ID + "='" + studentId + "'";
		ResultSet resultSet = statement.executeQuery(unitQuery);
		while (resultSet.next()) {
			Unit unit = new Unit();
			unit.setId(resultSet.getString(Constants.UNIT_ID));
			unit.setUnitName(resultSet.getString(Constants.UNIT_NAME));
			unit.setPractical(resultSet.getBoolean(Constants.IS_PRACTICAL));
			unit.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
			unit.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			units.add(unit);
		}
		
		setUnits(units);
	}
	
	public class UnitResponse {
		@SerializedName("units")
		List<Unit> items;
		
		public void setItems(List<Unit> items) {
			this.items = items;
		}
		
		public List<Unit> getItems() {
			return items;
		}
	}

	private void setUnits(List<Unit> units) {
		unitList = units;
	}

}
