package com.bernard.timetabler.crud_servlets.users;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.UserResponse;
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
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author com.bernard
 */
@WebServlet("/change-password")
public class ChangePassword extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = ChangePassword.class.getSimpleName();

    private final Statement statement;
    private final Gson gson;

    public ChangePassword() {
        super();

        statement = UtilCommonFunctions.initialize("ben", "");
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(req);

        resp.setContentType(Constants.APPLICATION_JSON);
        PrintWriter writer;

        UserResponse response = gson.fromJson(jsonRequest, UserResponse.class);

        try {
            MessageReport report = new MessageReport();
            if (changePassword(response.getUserId(), response.getRole(), response.getPassword())) {
                report.setMessage("Successfully changed password.");

                resp.setStatus(HttpServletResponse.SC_OK);
                writer = resp.getWriter();

                writer.write(gson.toJson(report));
            } else {
                report.setMessage("Your password was not changed.");

                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                writer = resp.getWriter();

                writer.write(gson.toJson(report));
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error" + e.getLocalizedMessage() + "\n");
            e.printStackTrace();
        }
    }

    private boolean changePassword(String userId, String role, String passwd) throws SQLException {
        if (role.equalsIgnoreCase("admin")) {

            String update = "UPDATE " + Constants.TABLE_ADMIN +
                    " SET " + Constants.PASSWORD + "='" + passwd + "'" +
                    " WHERE " + Constants.ADMIN_ID + "='" + userId + "'";

            return statement.executeUpdate(update) > 0;
        } else if (role.equalsIgnoreCase("student")) {
            String update = "UPDATE " + Constants.TABLE_STUDENTS +
                    " SET " + Constants.PASSWORD + "='" + passwd + "'" +
                    " WHERE " + Constants.STUDENT_ID + "='" + userId + "'";

            return statement.executeUpdate(update) > 0;
        } else if (role.equalsIgnoreCase("lecturer")) {
            String update = "UPDATE " + Constants.TABLE_LECTURERS +
                    " SET " + Constants.PASSWORD + "='" + passwd + "'" +
                    " WHERE " + Constants.LECTURER_ID + "='" + userId + "'";

            return statement.executeUpdate(update) > 0;
        }
        return false;
    }
}