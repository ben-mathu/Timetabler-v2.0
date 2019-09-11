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
import com.bernard.timetabler.dbinit.model.Department;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetDepartments
 */
@WebServlet("/departments")
public class GetAllDepartments extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetAllDepartments.class.getSimpleName();
	
	private List<Department> departments;
	
	private Statement statement;
	
	public GetAllDepartments() {
		statement = UtilCommonFunctions.initialize("ben", "");
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String jsonDeps = getJsonString(getDepartments());
			
			response.setContentType(Constants.APPLICATION_JSON);
			
			PrintWriter out = response.getWriter();
			out.write(jsonDeps);
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	private String getJsonString(List<Department> departments) {
		Gson gson = new Gson();
		
		return new String(gson.toJson(new DepartmentResponse(departments)));
	}

	private List<Department> getDepartments() throws SQLException {
		departments = new ArrayList<Department>();
		
		String query = "SELECT * FROM " + Constants.TABLE_DEPARTMENTS;
		
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
	
	private class DepartmentResponse {
		@SerializedName("departments")
		private List<Department> departments;
		
		public DepartmentResponse(List<Department> departments) {
			this.departments = departments;
		}
		
		public List<Department> getDepartments() {
			return departments;
		}
		
		public void setDepartments(List<Department> departments) {
			this.departments = departments;
		}
	}

}
