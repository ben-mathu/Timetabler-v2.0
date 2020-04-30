package com.bernard.timetabler.utils;

import com.bernard.timetabler.crud_servlets.reponses.MessageReport;
import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.Gson;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author com.bernard
 */
@WebFilter(urlPatterns = "/*", description = "Session Checker Filter")
public class TokenFilters implements Filter {
    private FilterConfig config = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        config.getServletContext().log("Initializing Session Checker");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        // verify authorization token
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        StringBuffer url = req.getRequestURL();
        System.out.println(url.toString());
        StringBuffer endPoint = req.getRequestURL();
        if (!req.getRequestURI().endsWith("/validate-user")
                && !req.getRequestURI().endsWith("/")
                && !req.getRequestURI().contains("src/main/webapp/static")
        ) {
            if (req.getHeader("Authorization") == null) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter writer = resp.getWriter();

                MessageReport report = new MessageReport();
                report.setStatus(403);
                report.setMessage("Requires token");
                Gson gson = new Gson();
                writer.write(gson.toJson(report));
            } else {
                String token = nomalizeToken(req);
                if (jwtTokenUtil.verifyToken(Constants.ISSUER, "login", token)) {
                    chain.doFilter(request, response);
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    PrintWriter writer = resp.getWriter();

                    MessageReport report = new MessageReport();
                    report.setStatus(403);
                    report.setMessage("Invalid Token");
                    Gson gson = new Gson();
                    writer.write(gson.toJson(report));
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private String nomalizeToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        return token.trim();
    }

    @Override
    public void destroy() {
        config.getServletContext().log("Destroying session checker filter.");
    }
}
