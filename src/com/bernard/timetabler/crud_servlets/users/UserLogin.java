package com.bernard.timetabler.crud_servlets.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bernard.timetabler.dbinit.Constants;

/**
 * Servlet implementation class UserLogin
 */
@WebServlet("/user-login")
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// send back a salt
		
		boolean isValid = authenticateUser(req, resp);
		if (isValid) {
			
		}
	}

	public boolean authenticateUser(HttpServletRequest req, HttpServletResponse res) {
		String username = req.getParameter(Constants.USERNAME);
		String password = req.getParameter(Constants.PASSWORD);
		return false;
	}
}
