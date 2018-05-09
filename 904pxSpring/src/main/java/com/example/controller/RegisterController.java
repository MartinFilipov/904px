package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.user.UserDAO;

@Controller
public class RegisterController {

	
	@RequestMapping(value="/register",method = RequestMethod.GET)
	public String loadRegisterPage(Model model) {
		return "register";
	}
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String register(HttpServletRequest request) {
		String username=request.getParameter("name");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		
		UserDAO dao= UserDAO.getInstance();
		if(!dao.register(username, password, email)){
			return "register";
		}
		
		request.setAttribute("registered", "yes");
		return "forward:/index";
	}
	
}