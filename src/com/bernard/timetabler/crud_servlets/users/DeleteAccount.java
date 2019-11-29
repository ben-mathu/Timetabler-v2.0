package com.bernard.timetabler.crud_servlets.users;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.utils.BufferRequest;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author bernard
 */
@WebServlet("/delete-account/*")
public class DeleteAccount extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = DeleteAccount.class.getSimpleName();

    private Statement statement;
    private Gson gson;

    public DeleteAccount() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
        gson = new Gson();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(req);
        String userId = req.getParameter(Constants.USER_ID);
        String role = req.getParameter(Constants.ROLE);

        PrintWriter writer;
        String jsonResponse = "";
        MessageReport report = new MessageReport();

        try {
            if (deleteAccount(userId, role)) {
                report.setMessage(Constants.MESSAGE_SUCCESS);
                jsonResponse = gson.toJson(report);

                resp.setStatus(HttpServletResponse.SC_OK);
                writer = resp.getWriter();
                writer.write(jsonResponse);
            } else {
                report.setMessage(Constants.OTHER_ISSUE);
                jsonResponse = gson.toJson(report);

                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                writer = resp.getWriter();
                writer.write(jsonResponse);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean deleteAccount(String userId, String role) throws SQLException {
        if (role.equalsIgnoreCase("admin")) {
            String deleteStm = "UPDATE TABLE " + Constants.TABLE_ADMIN +
                    " SET " + Constants.IS_REMOVED + "=" + true +
                    " WHERE " + Constants.ADMIN_ID + "='" + userId + "'";

            return statement.executeUpdate(deleteStm) > 0;
        } else if (role.equalsIgnoreCase("lecturer")) {
            String deleteStm = "UPDATE TABLE " + Constants.TABLE_LECTURERS +
                    " SET " + Constants.IS_REMOVED + "=" + true +
                    " WHERE " + Constants.LECTURER_ID + "='" + userId + "'";

            return statement.executeUpdate(deleteStm) > 0;
        } else if (role.equalsIgnoreCase("student")) {
            String deleteStm = "UPDATE TABLE " + Constants.TABLE_STUDENTS +
                    " SET " + Constants.IS_REMOVED + "=" + true +
                    " WHERE " + Constants.STUDENT_ID + "='" + userId + "'";

            return statement.executeUpdate(deleteStm) > 0;
        }
        return false;
    }
}