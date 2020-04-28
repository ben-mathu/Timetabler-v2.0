package com.bernard.timetabler.crud_servlets.department;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.department.DepartmentRequest;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author com.bernard
 */
@WebServlet("/department/*")
public class GetDepartment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = GetDepartment.class.getSimpleName();
    private Statement statement;

    public GetDepartment() {
        statement = UtilCommonFunctions.initialize("ben", "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String departmentId = req.getParameter(Constants.DEPARTMENT_ID);

        try {
            DepartmentRequest request = new DepartmentRequest();
            request.setDepartment(getDepartment(departmentId));

            if (request.getDepartment() != null) {
                Gson gson = new Gson();
                String jsonRequest = gson.toJson(request);

                resp.setStatus(HttpServletResponse.SC_OK);

                PrintWriter writer = resp.getWriter();

                writer.write(jsonRequest);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Department getDepartment(String departmentId) throws SQLException {
        Department department = new Department();
        String query = "SELECT * FROM " + Constants.TABLE_DEPARTMENTS +
                " WHERE " + Constants.DEPARTMENT_ID + "='" + departmentId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            department.setRemoved(resultSet.getBoolean(Constants.IS_REMOVED));
            department.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
            department.setDepartmentName(resultSet.getString(Constants.DEPARTMENT_NAME));
            department.setDepartmentId(resultSet.getString(Constants.DEPARTMENT_ID));
        }
        return department;
    }
}