package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.model.exceptions.UserException;
import com.project.model.post.Post;
import com.project.model.post.PostDAO;
import com.project.model.post.PostException;
import com.project.model.user.Album;
import com.project.model.user.User;
import com.project.model.user.UserDAO;

@Controller
public class ProfileController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PostDAO postDAO;

	private static final String FILE_PATH = "D:\\Uploads\\";

	@RequestMapping(method = RequestMethod.GET, value = "/editProfile")
	public String getProfileEdit(HttpServletRequest request) {
		if (request.getSession(false) == null || request.getSession(false).getAttribute("user_id") == null) {
			return "login";
		}
		return "editProfile";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addAlbum")
	public String addAlbum(HttpServletRequest request) {
		if (request.getSession(false) == null || request.getSession(false).getAttribute("user_id") == null) {
			return "login";
		}
		int user_id = (int) request.getSession(false).getAttribute("user_id");
		userDAO.addAlbum(user_id, request.getParameter("albumName"));
		return "forward:/profile";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/editor")
	public String editProfile(Model model, HttpServletRequest request, @RequestParam("profilePicture") MultipartFile profilePicture,
			@RequestParam("coverPhoto") MultipartFile coverPhoto) {
		if (request.getSession(false) == null || request.getSession(false).getAttribute("user_id") == null) {
			return "login";
		}

		int userID = (int) request.getSession(false).getAttribute("user_id");

		try {
			User user = userDAO.getUser(userID);
			String firstName = request.getParameter("firstName");
			if (firstName.equals("")) {
				firstName = user.getFirstName();
			}
			String lastName = request.getParameter("lastName");
			if (lastName.equals("")) {
				lastName = user.getLastName();
			}

			String profilePictureName = profilePicture.getOriginalFilename();
			String fullProfilePicturePath = "";

			if (profilePictureName != "") {

				String username = userDAO.getUsername((int) request.getSession(false).getAttribute("user_id"));
				
				File usernameFolder = new File(FILE_PATH + username);
				
				if (!usernameFolder.exists()) {
					usernameFolder.mkdir();
				}
				
				fullProfilePicturePath = FILE_PATH + username + File.separator + profilePictureName;

				File savedProfilePicture = new File(fullProfilePicturePath);

				Files.copy(profilePicture.getInputStream(), savedProfilePicture.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				

			}
			model.addAttribute("profilePictureName", profilePictureName);

			String coverPhotoName = profilePicture.getOriginalFilename();
			String fullCoverPhotoPath = "";

			if (coverPhotoName != "") {

				String username = userDAO.getUsername((int) request.getSession(false).getAttribute("user_id"));
				
				File usernameFolder = new File(FILE_PATH + username);
				
				if (!usernameFolder.exists()) {
					usernameFolder.mkdir();
				}
				
				fullCoverPhotoPath = FILE_PATH + username + File.separator + coverPhotoName;

				File savedCoverPhoto = new File(fullCoverPhotoPath);

				Files.copy(coverPhoto.getInputStream(), savedCoverPhoto.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				
			}
			model.addAttribute("coverPhotoName", coverPhotoName);
			
			

			// String profilePictureURL = new
			// File(request.getParameter("profilePicture")).getAbsolutePath();
			// if (profilePictureURL.equals("")) {
			// profilePictureURL = user.getProfilePictureURL();
			// }
			// String coverPhotoURL = new
			// File(request.getParameter("coverPhoto")).getAbsolutePath();
			// if (coverPhotoURL.equals("")) {
			// coverPhotoURL = user.getCoverPhotoURL();
			// }
			HttpSession session = request.getSession(false);
			userDAO.updateUser((int) session.getAttribute("user_id"), firstName, lastName, fullProfilePicturePath,
					fullCoverPhotoPath);
			
			return "forward:/profile";
			// return "profile";
		} catch (UserException e) {
			return "index";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "forward:/profile";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/profile/{username}/follow")
	public String followUser(HttpServletRequest request, @PathVariable String username)
			throws ServletException, IOException {
		if (request.getSession(false) == null || request.getSession(false).getAttribute("user_id") == null) {
			return "login";
		}
		int user_id = (int) request.getSession(false).getAttribute("user_id");
		try {
			int followed_id = userDAO.getUserIDByUsername(username);
			userDAO.followUser(user_id, followed_id);
			return "forward:/profile/" + username;
		} catch (UserException e) {
			System.out.println("\nProfileController:\nCouldn't follow user\n");
		}
		return "pageNotFound";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/profile")
	public String load(HttpServletRequest request) throws ServletException, IOException {
		if (request.getSession(false) == null || request.getSession(false).getAttribute("user_id") == null) {
			return "login";
		}
		int userID = (int) (request.getSession(false).getAttribute("user_id"));
		User user;
		try {
			user = userDAO.getUser(userID);
		} catch (UserException e) {
			return "index";
		}
		try {
			List<Album> albums = userDAO.getAllAlbums(userID);
			request.setAttribute("albums", albums);
		} catch (UserException e) {
			System.out.println("Couldn't get albums from the DB");
		}
		List<Integer> followedUserIds = userDAO.getUserIdsOfFollowedUsers(userID);
		List<String> followedUsernames = new ArrayList<>();
		for (Integer followedID : followedUserIds) {
			try {
				followedUsernames.add(userDAO.getUsername(followedID));
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
			if (request.getSession(false).getAttribute("user_id") != null
					&& username.equals(userDAO.getUsername((int) request.getSession(false).getAttribute("user_id")))) {
				return "forward:/profile";
			}
			User user = userDAO.getUser(username);
			// model.addAttribute("userID", dao.getUserIDByUsername(username));
			// model.addAttribute("userSessionID",request.getSession(false).getAttribute("user_id"));
			model.addAttribute("user", user);
			List<Album> albums = userDAO.getAllAlbums(userDAO.getUserIDByUsername(username));
			model.addAttribute("albums", albums);
		} catch (UserException e) {
			System.out.println("Something went wrong while getting user from DB");
		}
		return "userProfile";
	}

	@RequestMapping(value = "/profile/album/{id}", method = RequestMethod.GET)
	public String getPostDetails(HttpServletRequest request, Model model, @PathVariable(value = "id") Integer albumId) {
		// if (request.getSession(false) == null) {
		// return "index";
		// }
		try {
			List<Integer> postIds = userDAO.getAllPostIdsByAlbumID(albumId);
			List<Post> posts = new ArrayList<>();
			for (Integer postIdIndex : postIds) {
				posts.add(postDAO.getPostById(postIdIndex));
			}
			model.addAttribute("posts", posts);
			model.addAttribute("album", userDAO.getAlbumByID(albumId));
			return "album";
		} catch (UserException | PostException e) {
			System.out.println("Couldn't get all posts by album ID");
		}
		return "pageNotFound";
	}

	@RequestMapping(value = "/profile/album/{id}/add/{postId}", method = RequestMethod.GET)
	public String addPostToAlbum(HttpServletRequest request, Model model, @PathVariable(value = "id") Integer albumId,
			@PathVariable(value = "postId") Integer postId) {
		if (request.getSession(false) == null) {
			return "index";
		}
		userDAO.addPostToAlbum(postId, albumId);
		// model.addAttribute("id", albumId);
		return "forward:/profile/album/" + albumId;
	}

}
