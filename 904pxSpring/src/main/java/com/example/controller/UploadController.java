package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.post.Post;
import com.project.model.post.PostCategory;
import com.project.model.post.PostDAO;

@Controller
public class UploadController {
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public String loadPage(Model model, HttpServletRequest request) {
		if(request.getSession(false)==null || request.getSession(false).getAttribute("user_id")==null){
			return "login";
		}
		
		model.addAttribute("categories", PostDAO.getInstance().getAllCategories());
		
		return "upload";
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String uploadPost(HttpServletRequest request) {
		if (request.getSession(false) == null) {
			return "index";
		}
		
		String imageURL = request.getParameter("imageURL");
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String city = request.getParameter("city");
		String country = request.getParameter("country");
		String category = request.getParameter("category");
		boolean nsfw = request.getParameter("nsfw") != null;
		
		if (imageURL == null) {
			return "upload";
		}
		
		if (title == null) title = "";
		if (description == null) description = "";
		if (city == null) city = "";
		if (country == null) country = "";
		if (category == null) category = PostCategory.UNCATEGORIZED.toString();
		
		int userId = (int) (request.getSession(false).getAttribute("user_id"));
		
		PostDAO dao = PostDAO.getInstance();
		dao.uploadPostToUser(userId, imageURL, title, description, category, city, country, nsfw);
		
		return "forward:/uploaded";
	}
	
	@RequestMapping(value="/uploaded", method= {RequestMethod.POST, RequestMethod.GET})
	public String loadUserPhotos(Model model, HttpServletRequest request) {
		if (request.getSession(false) == null) {
			return "index";
		}
		
		int userId = (int) (request.getSession(false).getAttribute("user_id"));
		
		PostDAO dao = PostDAO.getInstance();
		
		List<Post> uploads = (List<Post>) dao.getUserUploads(userId);
		
		model.addAttribute("uploads", uploads);
		
		return "uploaded";
	}
}
