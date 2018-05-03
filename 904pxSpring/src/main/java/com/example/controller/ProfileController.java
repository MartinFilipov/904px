package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.exceptions.UserException;
import com.project.model.post.Comment;
import com.project.model.post.Post;
import com.project.model.post.PostDAO;
import com.project.model.post.PostException;
import com.project.model.user.Album;
import com.project.model.user.User;
import com.project.model.user.UserDAO;



@Controller
public class ProfileController {
	
	@RequestMapping(method=RequestMethod.GET,value="/editProfile")
	public String getProfileEdit(HttpServletRequest request){
		if(request.getSession(false)==null || request.getSession(false).getAttribute("user_id")==null){
			return "login";
		}
		return "editProfile";
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/addAlbum")
	public String addAlbum(HttpServletRequest request){
		if(request.getSession(false)==null || request.getSession(false).getAttribute("user_id")==null){
			return "login";
		}
		int user_id=(int) request.getSession(false).getAttribute("user_id");
		UserDAO.getInstance().addAlbum(user_id,request.getParameter("albumName"));
		return "forward:/profile";
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/editor")
	public String editProfile(HttpServletRequest request){
		if(request.getSession(false)==null || request.getSession(false).getAttribute("user_id")==null){
			return "login";
		}
		int userID = (int) request.getSession(false).getAttribute("user_id");
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
			dao.updateUser((int) session.getAttribute("user_id"), firstName, lastName, profilePictureURL,
					coverPhotoURL);
			return "forward:/profile";
//			return "profile";
		} catch (UserException e) {
			return "index";
		}
	}
	
	@RequestMapping(method ={RequestMethod.GET,RequestMethod.POST}, value = "/profile")
	public String load(HttpServletRequest request) throws ServletException, IOException {
		if(request.getSession(false)==null || request.getSession(false).getAttribute("user_id")==null){
			return "login";
		}
		int userID=(int)(request.getSession(false).getAttribute("user_id"));
		UserDAO dao = UserDAO.getInstance();
		User user;
		try {
			user=dao.getUser(userID);
		} catch (UserException e) {
			return "index";
		}
		try {
			List<Album> albums=dao.getAllAlbums(userID);
			request.setAttribute("albums", albums);
		} catch (UserException e) {
			System.out.println("Couldn't get albums from the DB");
		}
		request.setAttribute("email",user.getEmail());
		request.setAttribute("username",user.getUsername());
		request.setAttribute("firstName",user.getFirstName());
		request.setAttribute("lastName",user.getLastName());
		request.setAttribute("profilePictureURL",user.getProfilePictureURL());
		request.setAttribute("coverPhotoURL",user.getCoverPhotoURL());
		request.setAttribute("affection",user.getAffection());
		request.setAttribute("photoViews",user.getPhotoViews());
		return "profile";		
	}
	
	@RequestMapping(value = "/profile/album/{id}", method = RequestMethod.GET)
	public String getPostDetails(HttpServletRequest request, Model model, @PathVariable(value="id") Integer albumId) {
		if (request.getSession(false) == null) {
			return "index";
		}
		model.addAttribute("id", albumId);
		return "album";
//		PostDAO dao = PostDAO.getInstance();
//		
//		try {
//			Post post = dao.getPostById(albumId);
//			model.addAttribute("post", post);
//			try{
//				List<Comment> comments = dao.getAllComments(albumId);
//				model.addAttribute("comments", comments);
//				System.out.println("\n Komentarite beha getnati");
//				System.out.println("\nComments: "+comments+"\n");
//				return "postDetails";
//			}catch(PostException e){
//				System.out.println("\nSomething went wrong while getting the comments");
//			}
//			return "postDetails";
//		} catch (PostException e) {
//			System.out.println("Could not create post");
//		}

	}
}
