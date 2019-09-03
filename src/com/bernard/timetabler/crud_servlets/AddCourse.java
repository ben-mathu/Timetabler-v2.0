package com.bernard.timetabler.crud_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.Unit;
import com.bernard.timetabler.utils.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Servlet implementation class AddCourse
 */
@WebServlet("/add-unit")
public class AddCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = AddCourse.class.getSimpleName();
	private Statement statement;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer strBuffer = new StringBuffer();
		String line = "";
		
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
		}catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		Gson gson = new Gson();
		UnitReq unitReq = gson.fromJson(strBuffer.toString(), UnitReq.class);
		
		String passCode = unitReq.passCode;
		inidb(passCode);
		
		try {
			if (addUnit(unitReq.getUnit())) {
				MessageReport report = new MessageReport();
				report.setMessage("Successfully added unit" + unitReq.getUnit().getUnitName());
				
				writer.write(gson.toJson(report));
			} else {
				MessageReport report = new MessageReport();
				report.setMessage("Could not add unit" + unitReq.getUnit().getUnitName());
				
				writer.write(gson.toJson(report));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean addUnit(Unit unit) throws SQLException {
		String strQuery = "INSERT INTO " + Constants.TABLE_UNITS +
                " VALUES ('" + unit.getId() +
                "', '" + unit.getUnitName() +
                "', '" + unit.getProgrammeId() +
                "', '" + unit.getFacultyId() +
                "', '" + unit.getDepartmentId() +
                "', " + unit.isPractical() +
                ", " + unit.isCommon() +
                ")";
		if (statement.executeUpdate(strQuery) != 0)
			return true;
		else
			return false;
	}

	private void inidb(String passCode) {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
        CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("benard", passCode);
        
        statement = ct.getStatement();
	}
	
	public class UnitReq {
        @SerializedName("unit")
        private Unit unit;
        @SerializedName("passcode")
        private String passCode;

        public UnitReq(Unit unit, String passCode) {
            this.unit = unit;
            this.passCode = passCode;
        }
        
        public UnitReq() {}
        
        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        public Unit getUnit() {
            return unit;
        }
    }
}
