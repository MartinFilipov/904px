package com.project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.exceptions.UserException;
import com.project.model.user.UserDAO;

@Controller
public class LoginController {
	
	@Autowired
	private UserDAO userDAO;

	@RequestMapping(value="/login",method = RequestMethod.GET)
	public String load(Model model) {
		return "login";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(HttpServletRequest request){
		String username=request.getParameter("username");
		String password=request.getParameter("pass");
		try {
			int id=userDAO.login(username, password);
		  	HttpSession session=request.getSession();
			session.setAttribute("user_id", id);
			session.setMaxInactiveInterval(1000);
			request.setAttribute("user_id", id);
			return "forward:/index";
		} catch (UserException e) {
			System.out.println("wrong username or password!");
			return "login";
		}
	}
}
