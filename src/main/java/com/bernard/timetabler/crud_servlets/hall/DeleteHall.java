package com.bernard.timetabler.crud_servlets.hall;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.hall.HallDao;
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
@WebServlet("/delete-hall")
public class DeleteHall extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson;
    private Statement statement;
    private MessageReport report;
    private String jsonResponse;
    private PrintWriter writer;

    public DeleteHall() {
        super();
        statement = UtilCommonFunctions.initialize("ben", "");

        this.gson = new Gson();
        report = new MessageReport();
        jsonResponse = "";
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(req);

        try {
            HallDao request = gson.fromJson(jsonRequest, HallDao.class);
            if (deleteHall(request)) {
                report.setMessage("Successfully deleted.");
                jsonResponse = gson.toJson(report);

                resp.setStatus(HttpServletResponse.SC_OK);
                writer = resp.getWriter();
                writer.write(jsonResponse);
            } else {
                badResponse(resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void badResponse(HttpServletResponse resp) throws IOException {
        report.setMessage("Could not delete hall.");
        jsonResponse = gson.toJson(report);

        resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        writer = resp.getWriter();
        writer.write(jsonResponse);
    }

    private boolean deleteHall(HallDao request) throws SQLException {
        String query = "DELETE FROM " + Constants.TABLE_HALLS +
                " WHERE " + Constants.HALL_ID + "='" + request.getHall().getHallId() + "'";

        return !statement.execute(query);
    }
}