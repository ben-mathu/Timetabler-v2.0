package com.bernard.timetabler.crud_servlets.department;

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
import com.bernard.timetabler.dbinit.model.Department;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetDepartments
 */
@WebServlet("/departments/*")
public class GetDepartments extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetDepartments.class.getSimpleName();
	
	private List<Department> departments;
	
	private Statement statement;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter(Constants.FACULTY_ID);
		
		try {
			String jsonDeps = getJsonString(getDepartmentsByFacultyId(id));
			response.setContentType(Constants.APPLICATION_JSON);
			PrintWriter out = response.getWriter();
			out.write(jsonDeps);
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	private String getJsonString(List<Department> departmentsByFacultyId) {
		Gson gson = new Gson();
		
		return new String(gson.toJson(new DepartmentResponse(departmentsByFacultyId)));
	}
	
	private class DepartmentResponse {
		@SerializedName("departments")
		private List<Department> departments;
		public DepartmentResponse(List<Department> departments) {
			this.departments = departments;
		}
	}

	private List<Department> getDepartmentsByFacultyId(String id) throws SQLException {
		departments = new ArrayList<Department>();
		initializeDb();
		
		String query = "SELECT * FROM " + Constants.TABLE_DEPARTMENTS +
				" WHERE " + Constants.FACULTY_ID + "='" + id + "'";
		
		ResultSet resultSet = statement.executeQuery(query);
		
		while (resultSet.next()) {
			Department dep = new Department();
			dep.setDepartmentId(resultSet.getString(Constants.DEPARTMENT_ID));
			dep.setDepartmentName(resultSet.getString(Constants.DEPARTMENT_NAME));
			dep.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
			departments.add(dep);
		}
		
		return departments;
	}

	private void initializeDb() {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
		statement = ct.getStatement();
	}

}
