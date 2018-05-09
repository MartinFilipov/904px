package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping()
	public String sayHello(HttpServletRequest request, Model model) {
		List<Post> posts=new ArrayList<>();
		PostDAO dao=PostDAO.getInstance();
		if (request.getSession().getAttribute("user_id") != null) {
			posts.addAll(dao.getAllFollowedUserPostsByUserID((int)request.getSession().getAttribute("user_id")));
		}else{
			try {
				posts.addAll(dao.getFreshPosts());
			} catch (PostException e) {
				System.out.println("Something went wrong while getting fresh posts");
				e.printStackTrace();
			}
		}		
		model.addAttribute("posts",posts);
		return "index";
	}	

}
