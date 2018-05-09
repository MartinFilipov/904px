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

	@RequestMapping(method = RequestMethod.GET, value = "/editProfile")
	public String getProfileEdit(HttpServletRequest request) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "login";
		}
		return "editProfile";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addAlbum")
	public String addAlbum(HttpServletRequest request) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "login";
		}
		String albumName = request.getParameter("albumName");
		if (albumName.trim().length() > 0) {
			int user_id = (int) request.getSession(false).getAttribute("user_id");
			UserDAO dao = UserDAO.getInstance();
			try {
				if (!dao.albumExists(user_id, albumName)) {
					dao.addAlbum(user_id, albumName);
				}
			} catch (UserException e) {
				return "pageNotFound";
			}
		}
		return "forward:/profile";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/editor")
	public String editProfile(HttpServletRequest request) {
		if (request.getSession().getAttribute("user_id") == null) {
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
			String profilePictureURL = request.getParameter("profilePictureURL");
			if (profilePictureURL.equals("")) {
				profilePictureURL = user.getProfilePictureURL();
			}
			String coverPhotoURL = request.getParameter("coverPhotoURL");
			if (coverPhotoURL.equals("")) {
				coverPhotoURL = user.getCoverPhotoURL();
			}
			HttpSession session = request.getSession(false);
			dao.updateUser((int) session.getAttribute("user_id"), firstName, lastName, profilePictureURL,
					coverPhotoURL);
			return "forward:/profile";
		} catch (UserException e) {
			return "forward:/index";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/profile/{username}/follow")
	public String followUser(HttpServletRequest request, @PathVariable String username)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/login";
		}
		UserDAO dao = UserDAO.getInstance();
		int user_id = (int) request.getSession(false).getAttribute("user_id");
		try {
			int followed_id = dao.getUserIDByUsername(username);
			dao.followUser(user_id, followed_id);
			return "forward:/profile/" + username;
		} catch (UserException e) {
			System.out.println("\nProfileController:\nCouldn't follow user\n");
		}
		return "pageNotFound";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/profile/{username}/unfollow")
	public String unfollowUser(HttpServletRequest request, @PathVariable String username)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("user_id") == null) {
			return "login";
		}
		UserDAO dao = UserDAO.getInstance();
		int user_id = (int) request.getSession(false).getAttribute("user_id");
		try {
			int followed_id = dao.getUserIDByUsername(username);
			dao.unfollowUser(user_id, followed_id);
			return "forward:/profile/" + username;
		} catch (UserException e) {
			System.out.println("\nProfileController:Couldn't follow user");
		}
		return "pageNotFound";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/profile")
	public String load(HttpServletRequest request) throws ServletException, IOException {
		if (request.getSession().getAttribute("user_id") == null) {
			return "login";
		}
		int userID = (int) (request.getSession(false).getAttribute("user_id"));
		UserDAO dao = UserDAO.getInstance();
		User user;
		try {
			user = dao.getUser(userID);
		} catch (UserException e) {
			return "forward:/index";
		}
		try {
			List<Album> albums = dao.getAllAlbums(userID);
			request.setAttribute("albums", albums);
		} catch (UserException e) {
			System.out.println("Couldn't get albums from the DB");
		}
		List<Integer> followedUserIds = dao.getUserIdsOfFollowedUsers(userID);
		List<String> followedUsernames = new ArrayList<>();
		for (Integer followedID : followedUserIds) {
			try {
				followedUsernames.add(dao.getUsername(followedID));
			} catch (UserException e) {
				System.out.println("Couldn't get followed users");
			}
		}
		request.setAttribute("followed", followedUsernames);
		request.setAttribute("email", user.getEmail());
		request.setAttribute("username", user.getUsername());
		request.setAttribute("firstName", user.getFirstName());
		request.setAttribute("lastName", user.getLastName());
		request.setAttribute("profilePictureURL", user.getProfilePictureURL());
		request.setAttribute("coverPhotoURL", user.getCoverPhotoURL());
		request.setAttribute("affection", user.getAffection());
		request.setAttribute("photoViews", user.getPhotoViews());
		return "profile";
	}

	@RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
	public String getUserProfile(HttpServletRequest request, Model model,
			@PathVariable(value = "username") String username) {
		try {
			UserDAO dao = UserDAO.getInstance();
			if (request.getSession().getAttribute("user_id") != null
					&& username.equals(dao.getUsername((int) request.getSession(false).getAttribute("user_id")))) {
				return "forward:/profile";
			}
			User user = dao.getUser(username);
			model.addAttribute("user", user);
			int profileUserId = dao.getUserIDByUsername(username);
			List<Album> albums = dao.getAllAlbums(profileUserId);
			model.addAttribute("albums", albums);
			if (request.getSession().getAttribute("user_id") != null) {
				int user_id = (int) request.getSession(false).getAttribute("user_id");
				if (dao.checkUserFollowsUser(user_id, profileUserId)) {
					model.addAttribute("followed", "yes");
				}
			}

		} catch (UserException e) {
			System.out.println("Something went wrong while getting user from DB");
		}
		return "userProfile";
	}

	@RequestMapping(value = "/profile/album/{id}", method = RequestMethod.GET)
	public String getPostDetails(HttpServletRequest request, Model model, @PathVariable(value = "id") Integer albumId) {
		try {
			List<Integer> postIds = UserDAO.getInstance().getAllPostIdsByAlbumID(albumId);
			PostDAO dao = PostDAO.getInstance();
			List<Post> posts = new ArrayList<>();
			for (Integer postIdIndex : postIds) {
				posts.add(dao.getPostById(postIdIndex));
			}
			UserDAO userDao= UserDAO.getInstance();
			model.addAttribute("albumCreatorID",userDao.getAlbumCreatorID(albumId));
			model.addAttribute("posts", posts);
			model.addAttribute("album",userDao.getAlbumByID(albumId));
			return "album";
		} catch (UserException | PostException e) {
			System.out.println("Couldn't get all posts by album ID");
		}
		return "pageNotFound";
	}

	@RequestMapping(value = "/profile/album/{id}/add/{postId}", method = RequestMethod.GET)
	public String addPostToAlbum(HttpServletRequest request, Model model, @PathVariable(value = "id") Integer albumId,
			@PathVariable(value = "postId") Integer postId) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		UserDAO.getInstance().addPostToAlbum(postId, albumId);
		return "forward:/postDetails/" + postId;
	}
	@RequestMapping(value = "/profile/album/{id}/remove/{postId}", method = RequestMethod.GET)
	public String removePostFromAlbum(HttpServletRequest request, Model model, @PathVariable(value = "id") Integer albumId,
			@PathVariable(value = "postId") Integer postId) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		UserDAO.getInstance().removePostFromAlbum(postId, albumId);
		return "forward:/profile/album/" + albumId;
	}

}
