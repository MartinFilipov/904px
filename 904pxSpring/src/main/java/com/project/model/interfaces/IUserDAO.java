package com.project.model.interfaces;

import com.project.model.exceptions.UserException;


public interface IUserDAO {
	public int login(String username, String password) throws UserException;
	public boolean register(String username, String password, String email);
}
