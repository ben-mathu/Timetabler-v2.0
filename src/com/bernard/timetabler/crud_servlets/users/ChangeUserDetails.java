package com.bernard.timetabler.crud_servlets.users;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.admin.Admin;
import com.bernard.timetabler.dbinit.model.admin.AdminResponse;
import com.bernard.timetabler.dbinit.model.lecturer.Lecturer;
import com.bernard.timetabler.dbinit.model.lecturer.LecturerResponse;
import com.bernard.timetabler.dbinit.model.student.Student;
import com.bernard.timetabler.dbinit.model.student.StudentResponse;
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
 * @author bernard
 */
@WebServlet("/update-user-details/*")
public class ChangeUserDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = ChangeUserDetails.class.getSimpleName();

    private final Gson gson;
    private Statement statement;

    public ChangeUserDetails() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
        gson = new Gson();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(req);
        String role = req.getParameter(Constants.ROLE);
        String userid = req.getParameter(Constants.USER_ID);

        PrintWriter writer;
        String jsonResponse;
        MessageReport report = new MessageReport();

        try {
            if (changeUserDetails(jsonRequest, role, userid)) {
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

    private boolean changeUserDetails(String jsonRequest, String role, String userId) throws SQLException {
        if (role.equalsIgnoreCase("student")) {
            StudentResponse resp = gson.fromJson(jsonRequest, StudentResponse.class);
            Student student = resp.getStudent();

            String updateStm = "UPDATE " + Constants.TABLE_STUDENTS +
                    " SET " + Constants.F_NAME + "='" + student.getFname() + "'," +
                    Constants.M_NAME + "='" + student.getMname() + "'," +
                    Constants.L_NAME + "='" + student.getLname() + "'," +
                    Constants.EMAIL + "='" + student.getEmail() + "'" +
                    " WHERE " + Constants.STUDENT_ID + "='" + userId + "'";

            return statement.executeUpdate(updateStm) > 0;
        } else if (role.equalsIgnoreCase("lecturer")) {
            LecturerResponse resp = gson.fromJson(jsonRequest, LecturerResponse.class);
            Lecturer lecturer = resp.getLecturer();

            String updateStm = "UPDATE " + Constants.TABLE_LECTURERS +
                    " SET " + Constants.F_NAME + "='" + lecturer.getFirstName() + "'," +
                    Constants.M_NAME + "='" + lecturer.getMiddleName() + "'," +
                    Constants.L_NAME + "='" + lecturer.getLastName() + "'," +
                    Constants.EMAIL + "='" + lecturer.getEmail() + "'" +
                    " WHERE " + Constants.LECTURER_ID + "='" + userId + "'";

            return statement.executeUpdate(updateStm) > 0;
        } else if (role.equalsIgnoreCase("admin")) {
            AdminResponse resp = gson.fromJson(jsonRequest, AdminResponse.class);
            Admin admin = resp.getAdmin();

            String updateStm = "UPDATE " + Constants.TABLE_ADMIN +
                    " SET " + Constants.F_NAME + "='" + admin.getfName() + "'," +
                    Constants.M_NAME + "='" + admin.getmName() + "'," +
                    Constants.L_NAME + "='" + admin.getlName() + "'," +
                    Constants.EMAIL + "='" + admin.getEmail() + "'" +
                    " WHERE " + Constants.ADMIN_ID + "='" + userId + "'";

            return statement.executeUpdate(updateStm) > 0;
        } else {
            return false;
        }
    }
}