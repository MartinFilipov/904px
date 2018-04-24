package com.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.user.UserDAO;

/**
 * Servlet implementation class registerServlet
 */
@WebServlet("/register")
public class registerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username=request.getParameter("name");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		
		UserDAO dao= UserDAO.getInstance();
		if(!dao.register(username, password, email)){
			response.sendRedirect("./register.jsp");
			return;
		}
		
		request.setAttribute("registered", "yes");
		request.getRequestDispatcher("./").forward(request, response);
		/*
		request.getSession().setAttribute("registered", "yes");
		response.sendRedirect("./");
		*/
	}

}
