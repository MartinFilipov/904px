package com.project.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.project.database.DBConnection;
import com.project.exceptions.UserException;
import com.project.user.interfaces.IUserDAO;

public class UserDAO implements IUserDAO{
	private static final String ADD_USER_TO_DB = "INSERT INTO users(username, password, email) VALUES (?,sha1(?),?);";
	private static final String VALIDATE_USER = 
			"SELECT user_id, username, password FROM users WHERE username = ? AND password = sha1(?);";
	
	private static UserDAO instance;
	private Connection connection;
	
	private UserDAO() {
		try {
			connection = DBConnection.getInstance().getConnection();
			
		} catch (ClassNotFoundException e) {
			System.out.println("Missing database connection driver");
		} catch (SQLException e) {
			System.out.println("Something went wrong with the databse");
		}
	}
	
	public static UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	@Override
	public int login(String username, String password) throws UserException {
		try {
			PreparedStatement statement = connection.prepareStatement(VALIDATE_USER);
			
			statement.setString(1, username);
			statement.setString(2, password);
			
			ResultSet set = statement.executeQuery();
			
			password = null;
			
			if (set.next()) {
				return set.getInt("user_id");
			} else {
				throw new UserException("Wrong username or password");
			}
			
		} catch (SQLException e) {
			throw new UserException("Database is not working", e);
		}
	}

	@Override
	public boolean register(String username, String password, String email) {
		if ((username != null && username.trim().length() < User.MIN_USERNAME_LENGTH) &&
			(password != null && password.trim().length() < User.MIN_PASSWORD_LENGTH) &&
			(email != null && email.trim().length() < User.MIN_EMAIL_LENGTH)) {
			
			try {
				PreparedStatement st = connection.prepareStatement(ADD_USER_TO_DB);
				st.setString(1, username);
				st.setString(2, password);
				st.setString(3, email);
				st.executeUpdate();
				
				password = null;
								
				return true;
				
			} catch (SQLException e) {
				System.out.println("Something went wrong with the database");
			}
			
		}
		return false;
	}

	public List<User> getUsers() {
		try {
			Statement st = connection.createStatement();
			ResultSet set = st.executeQuery("SELECT username, email FROM users;");
			
			List<User> users = new ArrayList<>();
			
			while (set.next()) {
				try {
					users.add(new User(set.getString("username"), set.getString("email")));
				} catch (UserException e) {
					System.out.println("Error creating user");
				}
			}
			
			return users;
			
		} catch (SQLException e) {
			System.out.println("Something went wrong with the database");
			return new ArrayList<>();
		}
		
	}
	
}
