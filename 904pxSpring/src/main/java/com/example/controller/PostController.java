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
		if (request.getSession(false) == null) {
			return "index";
		}
		System.out.println("\n\n Post COntroller servlet");
		PostDAO dao = PostDAO.getInstance();
		
		try {
			List<Album> albums=UserDAO.getInstance().getAllAlbums((int)request.getSession(false).getAttribute("user_id"));
			model.addAttribute("albums",albums);
		} catch (UserException e1) {
			System.out.println("Couldn't get albums in postDetails");
		}
		try {
			dao.increasePostViewsById(id);
			Post post = dao.getPostById(id);
			model.addAttribute("post", post);

			try{
				List<Comment> comments = dao.getAllComments(id);
				model.addAttribute("comments", comments);
				System.out.println("\n Komentarite beha getnati");
				System.out.println("\nComments: "+comments+"\n");
				
				return "postDetails";
			}catch(PostException e){
				System.out.println("\nSomething went wrong while getting the comments");
			}
			
			return "postDetails";
			
		} catch (PostException e) {
			System.out.println("Could not create post");
		}
		return "pageNotFound";
	}
	@RequestMapping(value = "/postDetails/{postId}/{commentId}", method =RequestMethod.GET)
	public String likeComment(HttpServletRequest request, Model model,
			@PathVariable(value = "postId")Integer postId,
			@PathVariable(value = "commentId") Integer commentId){
		System.out.println("\n\n Like comment");
		PostDAO.getInstance().increaseLikesByCommentID(commentId);
		return "forward:/postDetails/"+postId;
	}
}
