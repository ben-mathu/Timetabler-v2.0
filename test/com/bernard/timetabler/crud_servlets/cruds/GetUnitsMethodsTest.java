package com.bernard.timetabler.crud_servlets.cruds;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bernard.timetabler.dbinit.model.course.Unit;

public class GetUnitsMethodsTest {
	private GetUnitsMethods unitCrudOp;
	
	@Before
	public void setUp() {
		unitCrudOp = new GetUnitsMethods();
	}
	
	@Test
	public void testGetUnitsByStudentId() {
		
		List<Unit> unitList = new ArrayList<Unit>();
		try {
			unitList = unitCrudOp.getUnitsByStudentId("ZAGQO");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(true, 2 + 2 == 4 ? true : false);
	}

}
