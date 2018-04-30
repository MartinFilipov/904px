package com.project.model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.model.database.DBConnection;
import com.project.model.exceptions.UserException;
import com.project.model.interfaces.IUserDAO;


public class UserDAO implements IUserDAO {
	private static final String ADD_USER_TO_DB = "INSERT INTO users(username, password, email) VALUES (?,sha1(?),?);";
	private static final String VALIDATE_USER = "SELECT user_id, username, password FROM users WHERE username = ? AND password = sha1(?);";
	private static final String GET_USERNAME_FROM_DB = "SELECT username from users WHERE user_id = ?;";
	private static final String GET_USER_FROM_DB ="SELECT email,username,first_name,last_name,profile_picture,cover_photo,affection,photo_views FROM users WHERE user_id = ?;";				
	private static final String UPDATE_USER_FROM_DB="UPDATE users set first_name=?,last_name=?,profile_picture=?,cover_photo=? where user_id=?;";
	
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
		synchronized(UserDAO.class) {
			if (instance == null) {
				instance = new UserDAO();
			}
		}
		return instance;
	}
	
	public String getUsername(int id) throws UserException {
		try {
			PreparedStatement statement = connection.prepareStatement(GET_USERNAME_FROM_DB);
			statement.setInt(1, id);
			ResultSet set = statement.executeQuery();
			if (set.next()) {
				return set.getString(1);
			} else {
				throw new UserException("Wrong id");
			}
		} catch (SQLException e) {
			throw new UserException("Database is not working", e);
		}
	}
	public boolean updateUser(int id,String firstName,String lastName,String profilePictureURL,String coverPhotoURL) throws UserException{
		try{
		PreparedStatement statement=connection.prepareStatement(UPDATE_USER_FROM_DB);
		statement.setString(1, firstName);
		statement.setString(2, lastName);
		statement.setString(3, profilePictureURL);
		statement.setString(4, coverPhotoURL);
		statement.setInt(5, id);
		statement.executeUpdate();
		return true;
		}catch(SQLException e){
			throw new UserException("Database is not working",e);
		}
	}
	public User getUser(int id) throws UserException{
		try{
			PreparedStatement statement=connection.prepareStatement(GET_USER_FROM_DB);
			statement.setInt(1, id);
			ResultSet set=statement.executeQuery();
			User user;
			//email,username,first_name,last_name,profile_picture,cover_photo,affection,photo_views
			if(set.next()){
				String email=set.getString("email");
				String username=set.getString("username");
				String firstName=set.getString("first_name");
				String lastName=set.getString("last_name");
				String profilePictureURL=set.getString("profile_picture");
				String coverPhotoURL=set.getString("cover_photo");
				int affection=set.getInt("affection");
				int photo_views=set.getInt("photo_views");
				user=new User(username,email,firstName,lastName,profilePictureURL,coverPhotoURL,affection,photo_views);
				return user;
			}else{
				throw new UserException("Wrong id");
			}
		}
		catch(SQLException e){
			throw new UserException("Database is not working",e);
		}
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
		if ((username != null && username.trim().length() < User.MIN_USERNAME_LENGTH)
				&& (password != null && password.trim().length() < User.MIN_PASSWORD_LENGTH)
				&& (email != null && email.trim().length() < User.MIN_EMAIL_LENGTH)) {

			try {
//				System.out.println(connection == null);
				PreparedStatement st = connection.prepareStatement(ADD_USER_TO_DB);
//				System.out.println("text");
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
