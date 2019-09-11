package com.bernard.timetabler.crud_servlets.users;

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
import com.bernard.timetabler.dbinit.model.Lecturer;
import com.bernard.timetabler.dbinit.model.LecturerResponse;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class GetLectures
 */
@WebServlet("/lecturers")
public class GetLecturers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = GetLecturers.class.getSimpleName();
	private Statement statement;
	
    public GetLecturers() {
        super();
        
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
        
        CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
        statement = ct.getStatement();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			LecturerResponseList list = getList();
			
			Gson gson = new Gson();
			String jsonStr = gson.toJson(list);
			
			Log.d(TAG, "Json String: " + jsonStr);
			
			response.setContentType(Constants.APPLICATION_JSON);
			
			PrintWriter writer = response.getWriter();
			
			writer.write(jsonStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private LecturerResponseList getList() throws SQLException {
		LecturerResponseList list = new LecturerResponseList();
		List<Lecturer> lecturers = new ArrayList<>();
		String query = "SELECT * FROM " + Constants.TABLE_LECTURERS;
		
		ResultSet result = statement.executeQuery(query);
		
		while (result.next()) {
			Lecturer lec = new Lecturer();
			lec.setId(result.getString(Constants.LECTURER_ID));
			lec.setFirstName(result.getString(Constants.F_NAME));
			lec.setMiddleName(result.getString(Constants.M_NAME));
			lec.setLastName(result.getString(Constants.L_NAME));
			lec.setUsername(result.getString(Constants.USERNAME));
			lec.setInSession(result.getBoolean(Constants.IN_SESSION));
			lec.setFacultyId(result.getString(Constants.FACULTY_ID));
			lec.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
			lecturers.add(lec);
		}
		
		list.setList(lecturers);
		
		return list;
	}

	public class LecturerResponseList {
		@SerializedName("lecturers")
		private List<Lecturer> list;
		
		public List<Lecturer> getList() {
			return list;
		}
		
		public void setList(List<Lecturer> list) {
			this.list = list;
		}
	}

}
