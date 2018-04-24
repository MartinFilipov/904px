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
import com.project.user.User;
import com.project.user.UserDAO;

@WebServlet("/profile")
public class profileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession(false)==null || request.getSession(false).getAttribute("username")==null){
			response.sendRedirect("./login.jsp");
			return;
		}
		int userID=(int)(request.getSession(false).getAttribute("username"));
		UserDAO dao = UserDAO.getInstance();
//		List<Post> posts = dao.getAllPosts();
//		request.setAttribute("posts", posts);
//		String username="Invalid username";
//		try {
//			username = dao.getUsername(userID);
//		} catch (UserException e) {
//			response.getWriter().println("Something went wrong, please come back later");
//			return;
//		}
		User user;
		try {
			user=dao.getUser(userID);
		} catch (UserException e) {
			response.getWriter().println("Something went wrong, please come back later");
			return;
		}
		request.setAttribute("email",user.getEmail());
		request.setAttribute("username",user.getUsername());
		request.setAttribute("firstName",user.getFirstName());
		request.setAttribute("lastName",user.getLastName());
		request.setAttribute("profilePictureURL",user.getProfilePictureURL());
		request.setAttribute("coverPhotoURL",user.getCoverPhotoURL());
		request.setAttribute("affection",user.getAffection());
		request.setAttribute("photoViews",user.getPhotoViews());
		RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
		dispatcher.forward(request, response);
		
	}
}
