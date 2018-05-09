package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.project.model.user.UserDAO;

@Controller
public class PostController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PostDAO postDAO;
	
	@Scheduled(fixedDelay=60000)
	public void doSomething() {
		try {
			postDAO.calculateRating();
			postDAO.cleanViewsTable();
			userDAO.deleteAnonymousUsers();
		} catch (PostException e) {
			e.printStackTrace();
			System.out.println("Something went wrong with cleaning views");
		} catch (UserException e) {
			e.printStackTrace();
			System.out.println("Something went wrong with cleaning anonymous users");
		}
	}
	
	@RequestMapping(value="/postDetails/{postId}/increaseLikes", method = RequestMethod.POST)
	public void increaseLikes(HttpServletRequest request, 
			@PathVariable(value="postId") Integer postId,
			HttpServletResponse response) {
		
		try {
			postDAO.increasePostLikesById(postId, (int)request.getSession(false).getAttribute("user_id"));
			response.setStatus(200);
		} catch (PostException e) {
			System.out.println("Could not increase likes");
		}
		
	}
	
	@RequestMapping(value="/postDetails/{postId}/decreaseLikes", method = RequestMethod.POST)
	public void decreaseLikes(HttpServletRequest request, 
			@PathVariable(value="postId") Integer postId,
			HttpServletResponse response) {
		
		try {
			postDAO.decreasePostLikesById(postId, (int)request.getSession(false).getAttribute("user_id"));
			response.setStatus(200);
		} catch (PostException e) {
			System.out.println("Could not increase likes");
		}
		
	}

	@RequestMapping(value = "/postDetails/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	public String getPostDetails(HttpServletRequest request, Model model, @PathVariable Integer id) {
		System.out.println("\n\n Post COntroller servlet");
		int userId = 0;
		if (request.getSession().getAttribute("user_id") != null) {
			userId = (int) request.getSession(false).getAttribute("user_id");
			try {
				List<Album> albums = userDAO.getAllAlbums(userId);
				model.addAttribute("albums", albums);
			} catch (UserException e1) {
				System.out.println("Couldn't get albums in postDetails");
			}
		} else {
			boolean userExists = false;
			
			int anonymousUserDetails = request.getRemoteAddr().hashCode();
			
			try {
				userExists = userDAO.getUserIDByUsername(""+anonymousUserDetails) != 0;
			} catch (UserException e1) {
				try {
					userId = userDAO.registerAnonymousUser(""+anonymousUserDetails,""+anonymousUserDetails, ""+anonymousUserDetails);
				} catch (UserException e) {
					e.printStackTrace();
					System.out.println("Could not register anonymous user");
				}
			}
			try {
				if (userExists) {
					userId = userDAO.getUserIDByUsername(""+anonymousUserDetails);
				}
					
				System.out.println("Successfully registered anonymous user");
				System.out.println("His id = " + userId);
			} catch (UserException e) {
				e.printStackTrace();
				System.out.println("Could not register anonymous user");
			}
		}
		System.out.println("userId = " + userId);
		try {
			
			boolean userHasSeenPost = postDAO.checkIfUserHasSeenPost(id, userId);
			
			System.out.println("user has seen post - " + userHasSeenPost);
			
			if (!userHasSeenPost) {
				postDAO.increasePostViewsById(id, userId);
				System.out.println("Increasing views");
			}
			
			Post post = postDAO.getPostById(id);
			
			boolean userHasLikedPost = postDAO.checkIfUserHasLikedPost(id, userId);
			
			if (userHasLikedPost) {
				model.addAttribute("liked", true);
				System.out.println("Should not be here");
			}
			
			model.addAttribute("post", post);

			try {
				List<Comment> comments = postDAO.getAllComments(id);
				model.addAttribute("comments", comments);
				System.out.println("\n Komentarite beha getnati");
				System.out.println("\nComments: " + comments + "\n");

				return "postDetails";
			} catch (PostException e) {
				System.out.println("\nSomething went wrong while getting the comments");
			}

			return "postDetails";

		} catch (PostException e) {
			System.out.println("Could not create post");
		}
		return "pageNotFound";
	}

	@RequestMapping(value = "/postDetails/{postId}/{commentId}", method = RequestMethod.GET)
	public String likeComment(HttpServletRequest request, Model model, @PathVariable(value = "postId") Integer postId,
			@PathVariable(value = "commentId") Integer commentId) {
		System.out.println("\n\n Like comment");
		postDAO.increaseLikesByCommentID(commentId);
		return "forward:/postDetails/" + postId;
	}
	
	@RequestMapping(value="/popular", method=RequestMethod.GET)
	public String loadPopularPage(HttpServletRequest request, Model model) {
		try {
			List<Post> popularPosts = postDAO.getPopularPosts();
			model.addAttribute("popular", popularPosts);
			
			return "popular";
		} catch (PostException e) {
			e.printStackTrace();
			System.out.println("Could not get popular posts");
		}
		
		return "pageNotFound";
	}

	@RequestMapping(value = "/fresh", method = RequestMethod.GET)
	public String getFreshPage(HttpServletRequest request, Model model) {
		try {
			List<Post> posts = 
					postDAO.getFreshPosts();
			model.addAttribute("posts", posts);
			return "fresh";
		} catch (PostException e) {
			System.out.println("Something went wrong while getting fresh posts");
		}
		return "pageNotFound";
	}
}
