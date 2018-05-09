package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping(value = "/postDetails/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	public String getPostDetails(HttpServletRequest request, Model model, @PathVariable Integer id) {

		PostDAO dao = PostDAO.getInstance();

		if (request.getSession().getAttribute("user_id") != null) {
			try {
				List<Album> albums = UserDAO.getInstance()
						.getAllAlbums((int) request.getSession(false).getAttribute("user_id"));
				model.addAttribute("albums", albums);
			} catch (UserException e1) {
				System.out.println("Couldn't get albums in postDetails");
			}
		}
		try {
			dao.increasePostViewsById(id);
			Post post = dao.getPostById(id);
			model.addAttribute("post", post);
			
			String username=dao.getPostCreator(id);
			model.addAttribute("username",username);
			try {
				List<Comment> comments = dao.getAllComments(id);
				if (request.getSession().getAttribute("user_id") != null) {
					int userId=(int) request.getSession().getAttribute("user_id");
					for (Comment c : comments) {
						if (dao.checkIfCommentIsLikedByUser(c.getId(), userId)) {
							c.setLikedByCurrentUser(true);
						}
					}
				}
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

	@RequestMapping(value = "/fresh", method = RequestMethod.GET)
	public String getFreshPage(HttpServletRequest request, Model model) {
		try {
			List<Post> posts = PostDAO.getInstance().getFreshPosts();
			model.addAttribute("posts", posts);
			return "fresh";
		} catch (PostException e) {
			System.out.println("Something went wrong while getting fresh posts");
		}
		return "pageNotFound";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String getSearchPage(HttpServletRequest request, Model model) {
		PostDAO dao=PostDAO.getInstance();
		List<Post> searchResults=dao.searchForPosts(request.getParameter("q"));
		model.addAttribute("searchRequest", "yes");
		model.addAttribute("searchResults", searchResults);
		return "forward:/index";
	}
	
}
