package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.model.exceptions.PostException;
import com.project.model.post.Post;
import com.project.model.post.PostCategory;
import com.project.model.post.PostDAO;

@Controller
@MultipartConfig
public class UploadController {
	private static final String FILE_PATH = "E:\\Uploads\\";
	
	@RequestMapping(value="/upload", method= {RequestMethod.GET, RequestMethod.POST})
	public String loadPage(Model model, HttpServletRequest request) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "login";
		}
		
		model.addAttribute("categories", PostDAO.getInstance().getAllCategories());
		
		return "upload";
	}
	
	@RequestMapping(value="/download/{filename:.+}", method = RequestMethod.GET)
	public void downloadImage(HttpServletResponse response, @PathVariable("filename") String fileName) throws PostException {
		File serverFile = new File(FILE_PATH + fileName);
		try {
			Files.copy(serverFile.toPath(), response.getOutputStream());
		} catch (IOException e) {
			throw new PostException("Could not download image",e);
		}
	}
	
	@RequestMapping(value="/uploadImage", method=RequestMethod.POST)
	public String selectImage(Model model, HttpServletRequest request, @RequestParam("filename") MultipartFile file) throws PostException {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		
		try {
			String fileName = file.getOriginalFilename();
			System.out.println("\n predi otvarqne na file");
			File savedFile = new File(FILE_PATH + fileName);		
			System.out.println("\n predi copy ");
			Files.copy(file.getInputStream(), savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File name in uploadImage controller ------------" + fileName);
			model.addAttribute("filename", fileName);
			return "forward:/upload";
		} catch (IOException e) {
			e.printStackTrace();
			throw new PostException("Could not upload file", e);
			
		}
	}
	
	@RequestMapping(value="/upload/{filename:.+}", method=RequestMethod.POST)
	public String uploadPost(HttpServletRequest request, @PathVariable("filename") String fileName) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		
		String imageURL = FILE_PATH + fileName;
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
		
		PostDAO dao = PostDAO.getInstance();
		dao.uploadPostToUser(userId, imageURL, title, description, category, city, country, nsfw);
		
		return "forward:/uploaded";
	}
	
	@RequestMapping(value="/uploaded", method= {RequestMethod.POST, RequestMethod.GET})
	public String loadUserPhotos(Model model, HttpServletRequest request) {
		if (request.getSession().getAttribute("user_id") == null) {
			return "forward:/index";
		}
		
		int userId = (int) (request.getSession(false).getAttribute("user_id"));
		
		PostDAO dao = PostDAO.getInstance();
		
		List<Post> uploads = (List<Post>) dao.getUserUploads(userId);
		
		model.addAttribute("uploads", uploads);
		
		return "uploaded";
	}

//	@RequestMapping(value="/post-details/{id}/test",method=RequestMethod.GET)
//	public String getComments(Model model,@PathVariable Integer id){
//		try {
//			List<Comment> comments=PostDAO.getInstance().getAllComments(id);
//			model.addAttribute("comments", comments);
//		} catch (PostException e) {
//			System.out.println("Something went wrong while getting comments.");
//		}
//		//Adjust return
//		return "forward:/postDetails";
//	}
	
	
	
	
//	@RequestMapping(value="/postDetails/{id}/addComment",method=RequestMethod.POST)
//	public String addComment(Model model,HttpServletRequest request,@PathVariable Integer id){
//		System.out.println("/n/n/n id: "+id+"/n/n");
//		int user_id=(int) request.getSession(false).getAttribute("user_id");
////			String username=UserDAO.getInstance().getUsername(user_id);
////			Comment comment=new Comment(, username);
//		PostDAO.getInstance().addComment(user_id, id, (String)request.getParameter("text"));
//		//Adjust return
////		return  "forward:/postDetails/{id}";
//		return "forward:/postDetails/"+id;
//	}

}
