package com.bernard.timetabler.crud_servlets.users;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
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
@WebServlet("/update-username")
public class UpdateUsername extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Statement statement;
    private RequestParams requestParams;
    private PrintWriter writer;
    private MessageReport report;
    private String jsonResponse = "";
    private Gson gson;

    public UpdateUsername() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(req);

        gson = new Gson();
        requestParams = gson.fromJson(jsonRequest, RequestParams.class);

        resp.setContentType(Constants.APPLICATION_JSON);

        report = new MessageReport();

        try {
            if (requestParams.getRole().equalsIgnoreCase("admin")) {
                if (updateAdminUsername(requestParams)) {
                    provideResponse(HttpServletResponse.SC_OK, resp);
                } else {
                    provideResponse(HttpServletResponse.SC_NOT_MODIFIED, resp);
                }
            } else if (requestParams.getRole().equalsIgnoreCase("lecturer")) {
                if (updateLecturerUsername(requestParams)) {
                    provideResponse(HttpServletResponse.SC_OK, resp);
                } else {
                    provideResponse(HttpServletResponse.SC_NOT_MODIFIED, resp);
                }
            }else if (requestParams.getRole().equalsIgnoreCase("student")) {
                if (updateStudentUsername(requestParams)) {
                    provideResponse(HttpServletResponse.SC_OK, resp);
                } else {
                    provideResponse(HttpServletResponse.SC_NOT_MODIFIED, resp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean updateStudentUsername(RequestParams req) throws SQLException {
        String updateQuery ="UPDATE " + Constants.TABLE_STUDENTS
                + " SET " + Constants.USERNAME + "='" + req.getName() + "'"
                + " WHERE " + Constants.STUDENT_ID + "='" + req.getUserId() + "'";
        return statement.executeUpdate(updateQuery) != 0;
    }

    private boolean updateLecturerUsername(RequestParams req) throws SQLException {
        String updateQuery ="UPDATE " + Constants.TABLE_LECTURERS
                + " SET " + Constants.USERNAME + "='" + req.getName() + "'"
                + " WHERE " + Constants.LECTURER_ID + "='" + req.getUserId() + "'";
        return statement.executeUpdate(updateQuery) != 0;
    }

    private void provideResponse(int responseStatus, HttpServletResponse resp) throws IOException {
        report.setMessage(responseStatus == 200 ? "Successfully updated to username " + requestParams.getName() : "An error occurred. Please contact the administrator.");
        jsonResponse = gson.toJson(report);

        resp.setStatus(responseStatus);
        writer = resp.getWriter();
        writer.write(jsonResponse);
    }

    /**
     * @param req parameters to be changed.
     * @return true if query runs successfully or false if otherwise
     */
    private boolean updateAdminUsername(RequestParams req) throws SQLException {
        String updateQuery ="UPDATE " + Constants.TABLE_ADMIN
                + " SET " + Constants.USERNAME + "='" + req.getName() + "'"
                + " WHERE " + Constants.ADMIN_ID + "='" + req.getUserId() + "'";
        return statement.executeUpdate(updateQuery) != 0;
    }
}