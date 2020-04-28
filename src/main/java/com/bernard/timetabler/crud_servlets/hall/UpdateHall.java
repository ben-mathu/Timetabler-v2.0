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
@WebServlet("/update-hall")
public class UpdateHall extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Statement statement;
    private Gson gson;
    private MessageReport report;
    private String jsonResponse;
    private PrintWriter writer;

    public UpdateHall() {
        super();

        statement = UtilCommonFunctions.initialize("ben", "");
        gson = new Gson();
        report = new MessageReport();
        jsonResponse = "";
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(req);

        try {
            HallDao request = gson.fromJson(jsonRequest, HallDao.class);
            if (updateHall(request)) {
                report.setMessage("Successfully update.");
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
        report.setMessage("Could not update hall.");
        jsonResponse = gson.toJson(report);

        resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        writer = resp.getWriter();
        writer.write(jsonResponse);
    }

    private boolean updateHall(HallDao request) throws SQLException {
        String query = "INSERT INTO " + Constants.TABLE_HALLS +
                " VALUE ('" + request.getHall().getHallId() + "','" +
                request.getHall().getHallName() + "','" +
                request.getHall().getFacultyId() + "'," +
                request.getHall().isRemoved() + ")";

        return !statement.execute(query);
    }
}