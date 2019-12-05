package com.bernard.timetabler.crud_servlets.hall;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.hall.Hall;
import com.bernard.timetabler.dbinit.model.hall.HallsDao;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author bernard
 */
@WebServlet("/halls")
public class GetHalls extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Statement statement;

    public GetHalls() {
        super();

        statement = UtilCommonFunctions.initialize("ben", "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HallsDao res = new HallsDao();
            res.setHallList(getHallsFromDb());

            resp.setContentType(Constants.APPLICATION_JSON);
            PrintWriter writer = resp.getWriter();

            Gson gson = new Gson();
            String json = gson.toJson(res);

            writer.write(json);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Hall> getHallsFromDb() throws SQLException {
        List<Hall> items = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.TABLE_HALLS +
                " WHERE " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Hall hall = new Hall();
            hall.setHallId(resultSet.getString(Constants.HALL_ID));
            hall.setHallName(resultSet.getString(Constants.HALL_NAME));
            hall.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
            items.add(hall);
        }
        return items;
    }
}