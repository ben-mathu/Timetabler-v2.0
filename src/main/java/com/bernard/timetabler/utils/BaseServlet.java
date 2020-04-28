package com.bernard.timetabler.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bernard
 */
public class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Logger logger = Logger.getRootLogger();;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.setLevel(Level.DEBUG);
    }
}