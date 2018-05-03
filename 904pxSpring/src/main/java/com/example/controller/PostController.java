package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.post.Post;
import com.project.model.post.PostDAO;
import com.project.model.post.PostException;

@Controller
public class PostController {
	
	@RequestMapping(value="/post-details/{id}", method=RequestMethod.GET)
	public String getPostDetails(HttpServletRequest request, Model model, @PathVariable Integer id) {
		if (request.getSession(false) == null) {
			return "index";
		}

		PostDAO dao = PostDAO.getInstance();
		
		try {
			Post post = dao.getPostById(id);
			model.addAttribute("post", post);
			model.addAttribute("context-path", request.getContextPath());
			
			return "postDetails";
		} catch (PostException e) {
			System.out.println("Could not create post");
		}
		return "pageNotFound";		
	}
	
}
