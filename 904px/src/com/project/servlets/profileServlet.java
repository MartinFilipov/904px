package com.project.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.exceptions.UserException;
import com.project.user.UserDAO;

@WebServlet("/profile")
public class profileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession(false)==null || request.getSession(false).getAttribute("username")==null){
			response.sendRedirect("./login.jsp");
			return;
		}
		
		UserDAO dao = UserDAO.getInstance();
//		List<Post> posts = dao.getAllPosts();
//		request.setAttribute("posts", posts);
		String username="Invalid username";
		try {
			username = dao.getUsername((int)(request.getSession(false).getAttribute("username")));
		} catch (UserException e) {
			response.getWriter().println("Something went wrong, please come back later");
			return;
		}
		request.setAttribute("name", username);
		RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
		dispatcher.forward(request, response);
		
	}
}
