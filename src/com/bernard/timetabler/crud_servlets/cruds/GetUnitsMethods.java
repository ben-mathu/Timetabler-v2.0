package com.bernard.timetabler.crud_servlets.cruds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.Unit;

public class GetUnitsMethods {
	private Statement statement;
	
	private List<Unit> unitList;
	
	public GetUnitsMethods() {		
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
	}
	
	public List<Unit> getUnitsByStudentId (String studentId) throws SQLException {
		unitList = new ArrayList<>();
		
		String unitsQuery = "SELECT un." + Constants.UNIT_ID + ",un." + Constants.UNIT_NAME +
				", un." + Constants.PROGRAMME_ID + ",un." + Constants.FACULTY_ID +
				",un." + Constants.IS_PRACTICAL +
				" FROM " + Constants.TABLE_UNITS +
				" un INNER JOIN " + Constants.TABLE_STUDENT_UNITS + " su " +
				"ON un." + Constants.UNIT_ID + "=su." + Constants.UNIT_ID +
				" WHERE su." + Constants.STUDENT_ID + "='" + studentId + "'";
		
		ResultSet resultSet = statement.executeQuery(unitsQuery);
		
		while (resultSet.next()) {
			Unit unit = new Unit();
			unit.setId(resultSet.getString(Constants.UNIT_ID));
			unit.setPractical(resultSet.getBoolean(Constants.IS_PRACTICAL));
			unit.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			unit.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
			unit.setUnitName(resultSet.getString(Constants.UNIT_NAME));
			unitList.add(unit);
		}
		
		return unitList;
	}

	public List<Unit> getUnitsByLecturerId(String lecturerId) throws SQLException {
		unitList = new ArrayList<>();
		
		String unitsQuery = "SELECT un." + Constants.UNIT_ID + ",un." + Constants.UNIT_NAME +
				", un." + Constants.PROGRAMME_ID + ",un." + Constants.FACULTY_ID +
				",un." + Constants.IS_PRACTICAL +
				" FROM " + Constants.TABLE_UNITS +
				" un INNER JOIN " + Constants.TABLE_LECTURER_UNITS + " lu " +
				"ON un." + Constants.UNIT_ID + "=lu." + Constants.UNIT_ID +
				" WHERE lu." + Constants.STUDENT_ID + "='" + lecturerId + "'";
		
		ResultSet resultSet = statement.executeQuery(unitsQuery);
		
		while (resultSet.next()) {
			Unit unit = new Unit();
			unit.setId(resultSet.getString(Constants.UNIT_ID));
			unit.setPractical(resultSet.getBoolean(Constants.IS_PRACTICAL));
			unit.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			unit.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
			unit.setUnitName(resultSet.getString(Constants.UNIT_NAME));
			unitList.add(unit);
		}
		
		return unitList;
	}

	public List<Unit> getUnitsByProgrammeId(String programmeId) throws SQLException {
		unitList = new ArrayList<>();
		
		String unitsQuery = "SELECT * FROM " + Constants.TABLE_UNITS +
				" WHERE " + Constants.PROGRAMME_ID + "='" + programmeId + "'"; 
		
		ResultSet resultSet = statement.executeQuery(unitsQuery);
		
		while (resultSet.next()) {
			Unit unit = new Unit();
			unit.setId(resultSet.getString(Constants.UNIT_ID));
			unit.setPractical(resultSet.getBoolean(Constants.IS_PRACTICAL));
			unit.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			unit.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
			unit.setUnitName(resultSet.getString(Constants.UNIT_NAME));
			unitList.add(unit);
		}
		return unitList;
	}
}
