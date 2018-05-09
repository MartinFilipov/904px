package com.project.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.model.post.PostException;
import com.project.model.exceptions.UserException;
import com.project.model.post.Post;
import com.project.model.post.PostCategory;
import com.project.model.post.PostDAO;
import com.project.model.user.UserDAO;

@Controller
@MultipartConfig
public class UploadController {
	@Autowired
	private PostDAO postDAO;
	@Autowired
	private UserDAO userDAO;
	
	private static final String FILE_PATH = "E:\\Uploads\\";
	
	@RequestMapping(value="/upload", method= RequestMethod.GET)
	public String loadPage(Model model, HttpServletRequest request) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "login";
		}
		
		model.addAttribute("categories", postDAO.getAllCategories());
		
		return "upload";
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public String loadFillForm(Model model, HttpServletRequest request) {
		if(request.getSession(false)==null || request.getSession(false).getAttribute("user_id")==null){
			return "login";
		}
		
		model.addAttribute("categories", postDAO.getAllCategories());
		
		return "upload";
	}
	
	@RequestMapping(value="/download/{filename:.+}", method = RequestMethod.GET)
	public void downloadImage(HttpServletResponse response, HttpServletRequest request, @PathVariable("filename") String fileName) throws PostException {
		String usernameOfFileOwner = "";
		try {
			usernameOfFileOwner = userDAO.getUsernameOfFileOwnerByFileName(fileName);
		} catch (PostException e) {
			try {
				usernameOfFileOwner = userDAO.getUsername((int)request.getSession(false).getAttribute("user_id"));
			} catch (UserException e1) {
				e1.printStackTrace();
				System.out.println("Could not get username from session");
			}
		}
		System.out.println(usernameOfFileOwner);
		File serverFile = new File(FILE_PATH + usernameOfFileOwner + "\\" + fileName);
		try {
			Files.copy(serverFile.toPath(), response.getOutputStream());
		} catch (IOException e) {
			throw new PostException("Could not download image",e);
		}
	}
	
	@RequestMapping(value="/uploadImage", method=RequestMethod.POST)
	public String selectImage(Model model, HttpServletRequest request, @RequestParam("filename") MultipartFile file) throws PostException, UserException {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		
		try {
			String fileName = file.getOriginalFilename();			
			if (fileName != "") {			
				String username = userDAO.getUsername((int)request.getSession(false).getAttribute("user_id"));
				File usernameFolder = new File(FILE_PATH + username);
				if (!usernameFolder.exists()) {
					usernameFolder.mkdir();
				}
				File savedFile = new File(FILE_PATH + username + File.separator + fileName);
				Files.copy(file.getInputStream(), savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				model.addAttribute("filename", fileName);
			}
			return "forward:/upload";
		} catch (IOException e) {
			e.printStackTrace();
			throw new PostException("Could not upload file", e);
			
		} catch (UserException e) {
			e.printStackTrace();
			throw new UserException("Could not get username", e);
		}
	}
	
	@RequestMapping(value="/upload/{filename:.+}", method=RequestMethod.POST)
	public String uploadPost(HttpServletRequest request, @PathVariable("filename") String fileName) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		String username;
		try {
			username = userDAO.getUsername((int)request.getSession(false).getAttribute("user_id"));
			
			String imageURL = FILE_PATH + username + File.separator + fileName;
			
			System.out.println(imageURL);
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String city = request.getParameter("city");
			String country = request.getParameter("country");
			String category = request.getParameter("category");
			boolean nsfw = request.getParameter("nsfw") != null;
			
			if (fileName == null) {
				return "upload";
			}
			
			if (title == null) title = "";
			if (description == null) description = "";
			if (city == null) city = "";
			if (country == null) country = "";
			if (category == null) category = PostCategory.UNCATEGORIZED.toString();
			
			int userId = (int) (request.getSession(false).getAttribute("user_id"));
			
			postDAO.uploadPostToUser(userId, imageURL, title, description, category, city, country, nsfw);
			
			return "forward:/uploaded";
			
		} catch (UserException e) {
			e.printStackTrace();
			System.out.println();
		}
		
		return "pageNotFound";
		
	}
	
	@RequestMapping(value="/uploaded", method= {RequestMethod.POST, RequestMethod.GET})
	public String loadUserPhotos(Model model, HttpServletRequest request) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		
		int userId = (int) (request.getSession(false).getAttribute("user_id"));
				
		List<Post> uploads = (List<Post>) postDAO.getUserUploads(userId);
		
		model.addAttribute("uploads", uploads);
		
		return "uploaded";
	}
}
