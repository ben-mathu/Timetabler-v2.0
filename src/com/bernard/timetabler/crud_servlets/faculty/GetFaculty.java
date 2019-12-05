package com.bernard.timetabler.crud_servlets.faculty;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.bernard.timetabler.dbinit.model.faculty.FacultyRequest;
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
 * @author bernard
 */
@WebServlet("/faculty/*")
public class GetFaculty extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = GetFaculty.class.getSimpleName();

    private Statement statement;

    public GetFaculty() {
        statement = UtilCommonFunctions.initialize("ben", "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String facultyId = req.getParameter(Constants.FACULTY_ID);

        try {
            FacultyRequest request = new FacultyRequest(getFaculty(facultyId));

            if (request.getFaculty() != null) {
                resp.setContentType(Constants.APPLICATION_JSON);
                resp.setStatus(HttpServletResponse.SC_OK);

                // serialize faculty
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(request);

                // log output
                Log.d(TAG, "Faculty: " + jsonResponse);

                PrintWriter writer = resp.getWriter();
                writer.write(jsonResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Faculty getFaculty(String facultyId) throws SQLException {
        Faculty faculty = new Faculty();

        String query = "SELECT * FROM " + Constants.TABLE_FACULTIES +
                " WHERE " + Constants.FACULTY_ID + "='" + facultyId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next()) {
            faculty.setRemoved(resultSet.getBoolean(Constants.IS_REMOVED));
            faculty.setFacultyName(resultSet.getString(Constants.FACULTY_NAME));
            faculty.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
            faculty.setCampusId(resultSet.getString(Constants.CAMPUS_ID));
        }
        return faculty;
    }
}