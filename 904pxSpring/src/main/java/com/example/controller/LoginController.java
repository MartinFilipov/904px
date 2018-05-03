package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.exceptions.UserException;
import com.project.model.user.UserDAO;


//@RequestMapping(value="/login")
@Controller
public class LoginController {

	@RequestMapping(value="/login",method = RequestMethod.GET)
	public String load(Model model) {
		return "login";
	}	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(HttpServletRequest request){
		String username=request.getParameter("username");
		String password=request.getParameter("pass");
		
		UserDAO dao= UserDAO.getInstance();
		try {
			int id=dao.login(username, password);
		  	HttpSession session=request.getSession();
			session.setAttribute("user_id", id);
			session.setMaxInactiveInterval(1000);
			return "index";
		} catch (UserException e) {
			System.out.println("wrong username or password!");
			return "login";
		}
	}
}
