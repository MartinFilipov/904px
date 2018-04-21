package com.project.user.interfaces;

import com.project.exceptions.UserException;

public interface IUserDAO {
	public int login(String username, String password) throws UserException;
	public boolean register(String username, String password, String email);
}
