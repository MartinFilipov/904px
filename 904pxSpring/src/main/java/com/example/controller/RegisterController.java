package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {

	
	@RequestMapping(value="/register",method = RequestMethod.GET)
	public String loadRegisterPage(Model model) {
		return "register";
	}	
	
}