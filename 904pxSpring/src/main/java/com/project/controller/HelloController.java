package com.project.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.post.Post;
import com.project.model.post.PostDAO;
import com.project.model.post.PostException;

@Controller
@RequestMapping(value="/index")
public class HelloController {
	
	@Autowired
	private PostDAO postDAO;
	
	@RequestMapping()
	public String sayHello(HttpServletRequest request, Model model) {
		List<Post> posts=new ArrayList<>();
		if (request.getSession().getAttribute("user_id") != null) {
			posts.addAll(postDAO.getAllFollowedUserPostsByUserID((int)request.getSession().getAttribute("user_id")));
		}else{
			try {
				posts.addAll(postDAO.getFreshPosts());
			} catch (PostException e) {
				System.out.println("Something went wrong while getting fresh posts");
				e.printStackTrace();
			}
		}		
		model.addAttribute("posts",posts);
		return "index";
	}	

}
