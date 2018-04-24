package com.project.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.exceptions.UserException;
import com.project.user.User;
import com.project.user.UserDAO;

/**
 * Servlet implementation class profileEditorServlet
 */
@WebServlet("/editor")
public class profileEditorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession(false) == null) {
			response.sendRedirect("./login.jsp");
			return;
		}
		int userID = (int) request.getSession(false).getAttribute("username");
		UserDAO dao = UserDAO.getInstance();

		try {
			User user = dao.getUser(userID);
			String firstName = request.getParameter("firstName");
			if (firstName.equals("")) {
				firstName = user.getFirstName();
			}
			String lastName = request.getParameter("lastName");
			if (lastName.equals("")) {
				lastName = user.getLastName();
			}
			String profilePictureURL= request.getParameter("profilePictureURL");
			if (profilePictureURL.equals("")) {
				profilePictureURL = user.getProfilePictureURL();
			}
			String coverPhotoURL= request.getParameter("coverPhotoURL");
			if (coverPhotoURL.equals("")) {
				coverPhotoURL = user.getCoverPhotoURL();
			}
//			String profilePictureURL = new File(request.getParameter("profilePicture")).getAbsolutePath();
//			if (profilePictureURL.equals("")) {
//				profilePictureURL = user.getProfilePictureURL();
//			}
//			String coverPhotoURL = new File(request.getParameter("coverPhoto")).getAbsolutePath();
//			if (coverPhotoURL.equals("")) {
//				coverPhotoURL = user.getCoverPhotoURL();
//			}
			HttpSession session = request.getSession(false);
			dao.updateUser((int) session.getAttribute("username"), firstName, lastName, profilePictureURL,
					coverPhotoURL);
			response.sendRedirect("./profile");
		} catch (UserException e) {
			response.getWriter().println("Something went wrong, please come back later");
			return;
		}
	}

}
