package com.bernard.timetabler.crud_servlets.users.lecturer;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.lecturer.LecturerRequest;
import com.bernard.timetabler.utils.BufferRequest;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author com.bernard
 */
@WebServlet("/update-lecturer/*")
public class UpdateLecturer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Statement statement;

    public UpdateLecturer() {
        super();

        statement = UtilCommonFunctions.initialize("ben", "");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(request);

        // deserialise json string
        Gson gson = new Gson();
        LecturerRequest req = gson.fromJson(jsonRequest, LecturerRequest.class);

        try {
            response.setContentType(Constants.APPLICATION_JSON);
            PrintWriter writer;

            MessageReport report = new MessageReport();
            String jsonResponse = "";

            if (updateLecturer(req)) {
                report.setMessage("Successfully updated lecturer of id: " + req.getLecturer().getId());
                jsonResponse = gson.toJson(report);

                writer = response.getWriter();
                writer.write(jsonResponse);
            } else {
                report.setMessage("Could not update " + req.getLecturer().getId() + ". \nPlease try again, or contact admin.");
                jsonResponse = gson.toJson(report);

                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                writer = response.getWriter();
                writer.write(jsonResponse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean updateLecturer(LecturerRequest req) throws SQLException {
        String updateQuery = "UPDATE " + Constants.TABLE_LECTURERS
                + " SET " + Constants.F_NAME + "='" + req.getLecturer().getFirstName() + "'"
                + " , " + Constants.M_NAME + "='" + req.getLecturer().getMiddleName() + "'"
                + " , " + Constants.L_NAME + "='" + req.getLecturer().getLastName() + "'"
                + " , " + Constants.USERNAME + "='" + req.getLecturer().getUsername() + "'"
                + " , " + Constants.FACULTY_ID + "='" + req.getLecturer().getFacultyId() + "'"
                + " , " + Constants.DEPARTMENT_ID + "='" + req.getLecturer().getDepartmentId() + "'"
                + " , " + Constants.IN_SESSION + "=" + req.getLecturer().isInSesson()
                + " WHERE " + Constants.LECTURER_ID + "='" + req.getLecturer().getId() + "'";

        if (statement.executeUpdate(updateQuery) != 0) {
            return true;
        }
        return false;
    }
}