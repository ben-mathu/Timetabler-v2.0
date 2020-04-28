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

import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import static com.bernard.timetabler.dbinit.Constants.*;

/**
 * Servlet implementation class GetUnitsByLecturerId
 */
@WebServlet("/lecturers/units/*")
public class GetUnitsByLecturerId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CreateSchemaTimeTabler ct;
	private Statement statement;
	
	private List<Unit> unitList;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		unitList= new ArrayList();
		response.setContentType("application/json");
		
		// Initialize db
		initDb();
		
		try {
			String id = request.getParameter(LECTURER_ID);
			getUnitsByLecturerId(id);
			
			// Prepare for response
			UnitResponse unitRespone = new UnitResponse();
			unitRespone.setItems(unitList);
			
			Gson gson = new Gson();
			
			String jsonResponse = gson.toJson(unitRespone, UnitResponse.class);
			
			PrintWriter writer = response.getWriter();
			writer.write(jsonResponse);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDb() {
		CreateSchemaTimeTabler.setDatabase(DATABASE_NAME);
		ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
	}

	private void getUnitsByLecturerId(String lecturerId) throws SQLException {
		
		List<Unit> units = new ArrayList<Unit>();
		
		String unitQuery = "SELECT * FROM " + TABLE_UNITS + " un"
						+ " INNER JOIN " + TABLE_LECTURER_UNITS + " lu"
						+ " ON un." + UNIT_ID + "=lu." + UNIT_ID
						+ " WHERE lu." + LECTURER_ID + "='" + lecturerId + "'"
						+ " AND lu." + IS_REMOVED + "=0";
		ResultSet resultSet = statement.executeQuery(unitQuery);
		while (resultSet.next()) {
			Unit unit = new Unit();
			unit.setId(resultSet.getString(UNIT_ID));
			unit.setUnitName(resultSet.getString(UNIT_NAME));
			unit.setPractical(resultSet.getBoolean(IS_PRACTICAL));
			unit.setProgrammeId(resultSet.getString(PROGRAMME_ID));
			unit.setFacultyId(resultSet.getString(FACULTY_ID));
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
