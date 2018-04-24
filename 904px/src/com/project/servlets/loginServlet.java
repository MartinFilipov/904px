package com.project.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.exceptions.UserException;
import com.project.user.UserDAO;


@WebServlet("/login")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		String password=request.getParameter("pass");
		
		UserDAO dao= UserDAO.getInstance();
		int id=0;
		try {
			id=dao.login(username, password);
		  	HttpSession session=request.getSession();
			session.setAttribute("username", id);
			session.setMaxInactiveInterval(1000);
			response.sendRedirect("./");
		} catch (UserException e) {
			System.out.println("wrong username or password!");
			response.sendRedirect("./login.jsp");
		}
		
	}

}
