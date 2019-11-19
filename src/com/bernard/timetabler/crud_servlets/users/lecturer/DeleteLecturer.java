package com.bernard.timetabler.crud_servlets.users.lecturer;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.course.UnitReq;
import com.bernard.timetabler.dbinit.model.lecturer.LecRequest;
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

@WebServlet("/delete-lecturer")
public class DeleteLecturer extends HttpServlet {
    /**
     * Remove lecturer entry.
     */
    private static final long serialVersionUID = 1L;

    private final Statement statement;

    public DeleteLecturer() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(req);

        // deserialise json string: jsonRequest
        Gson gson = new Gson();
        LecturerRequest lecRequest = gson.fromJson(jsonRequest, LecturerRequest.class);

        resp.setContentType(Constants.APPLICATION_JSON);
        PrintWriter writer;

        MessageReport report = new MessageReport();
        String jsonResponse = "";

        try {
            if (deleteLecturer(lecRequest)) {
                report.setMessage("Successfully deleted lecturer: " + lecRequest.getLecturer().getId());
                jsonResponse = gson.toJson(report);

                resp.setStatus(HttpServletResponse.SC_OK);
                writer = resp.getWriter();
                writer.write(jsonResponse);
            } else {
                report.setMessage("Could not delete " + lecRequest.getLecturer().getId());
                jsonResponse = gson.toJson(report);

                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                writer = resp.getWriter();
                writer.write(jsonResponse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The object lecturer is not completely deleted,
     * its property is_removed is set to false, and queriied as false.
     *
     * @param lecRequest Lecturer object to be modified.
     * @return true if query runs successfully or false if otherwise
     * @see com.bernard.timetabler.dbinit.model.lecturer.Lecturer
     */
    private boolean deleteLecturer(LecturerRequest lecRequest) throws SQLException {
        String updateQuery ="UPDATE " + Constants.TABLE_LECTURERS
                + " SET " + Constants.IS_REMOVED + "=" + true
                + " WHERE " + Constants.LECTURER_ID + "='" + lecRequest.getLecturer().getId() + "'";
        if (statement.executeUpdate(updateQuery) != 0) {
            return true;
        }

        return false;
    }
}
