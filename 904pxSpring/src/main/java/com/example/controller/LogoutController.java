package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogoutController {

	@RequestMapping(method = RequestMethod.GET, value = "/logout")
	public String logout(HttpServletRequest request) {
		if (request.getSession(false) != null && request.getSession(false).getAttribute("user_id") != null) {
			request.getSession(false).invalidate();
		}
		return "forward:/index";
	}

}
