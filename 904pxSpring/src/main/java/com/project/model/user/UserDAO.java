package com.project.model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.model.database.DBConnection;
import com.project.model.exceptions.UserException;
import com.project.model.interfaces.IUserDAO;
import com.project.model.post.PostException;

@Component
public class UserDAO implements IUserDAO {
	private static final int INVALID_ID = 0;
	private static final int KEY_COLUMN_ID = 1;
	private static final String GET_USERNAME_OF_FILE_OWNER_BY_FILE_NAME = 
			"SELECT u.username from posts p JOIN users u ON p.user_id = u.user_id WHERE image_url LIKE ?;";
	private static final String GET_USERNAME_OF_PROFILE_PICTURE="SELECT username from users where profile_picture LIKE ? or cover_photo LIKE ?;";
	private static final String ADD_USER_TO_DB = "INSERT INTO users(username, password, email) VALUES (?,sha1(?),?);";
	private static final String VALIDATE_USER = "SELECT user_id, username, password FROM users WHERE username = ? AND password = sha1(?);";
	private static final String GET_USERNAME_FROM_DB = "SELECT username from users WHERE user_id = ?;";
	private static final String GET_USER_FROM_DB = "SELECT email,username,first_name,last_name,profile_picture,cover_photo,affection,photo_views FROM users WHERE user_id = ?;";
	private static final String UPDATE_USER_FROM_DB = "UPDATE users set first_name=?,last_name=?,profile_picture=?,cover_photo=? where user_id=?;";
	private static final String ADD_ALBUM_TO_DB = "INSERT INTO albums(user_id, name) VALUES (?,?);";
	private static final String GET_ALL_ALBUMS_BY_ID = "select name, album_id FROM albums WHERE user_id=?";
	private static final String ADD_POST_TO_ALBUM = "INSERT into albums_has_posts (post_id,album_id) VALUES(?,?);";
	private static final String REMOVE_POST_FROM_ALBUM="DELETE from albums_has_posts where post_id=? and album_id=?;";
	private static final String GET_ALL_POST_IDS_IN_ALBUM_BY_ALBUMID = "select post_id from albums_has_posts where album_id=?;";
	private static final String GET_ALBUM_BY_ALBUM_ID = "SELECT album_id, name FROM albums WHERE album_id=?;";
	private static final String GET_USER_FROM_DB_BY_USERNAME="SELECT email,first_name,last_name,profile_picture,cover_photo,affection,photo_views FROM users WHERE username = ?;";
	private static final String GET_USER_ID_BY_USERNAME="SELECT user_id FROM users WHERE username=?;";
	private static final String GET_FOLLOWED_USERS="SELECT user_id FROM users_has_followers WHERE follower_id=?;";
	private static final String FOLLOW_USER="INSERT INTO users_has_followers (user_id,follower_id) VALUES(?,?)";
	private static final String UNFOLLOW_USER="delete from users_has_followers where user_id=? and follower_id=?;";
	private static final String GET_COMMENT_LIKES_BY_COMMENT_ID = "select count(*) from comments_has_likes WHERE commend_id=?";
	private static final String CHECK_IF_ALBUM_EXISTS="select count(*) as contains from albums where user_id=? and name=?;";
	private static final String CHECK_IF_USER_IS_FOLLOWING_USER="select count(*) from users_has_followers where user_id=? and follower_id=?;";
	private static final String GET_ALBUM_CREATOR_BY_ALBUM_ID="select u.user_id from users u JOIN albums a on u.user_id=a.user_id where album_id=?;";
	private static final String MAKE_USER_ANONYMOUS =
			"UPDATE users SET anonymous_user = 1 WHERE user_id = ?;";
	private static final String REMOVE_SQL_SAFE_UPDATES = 
			"SET SQL_SAFE_UPDATES=0;";
	private static final String ADD_SQL_SAFE_UPDATES = 
			"SET SQL_SAFE_UPDATES=1;";
	private static final String DELETE_ANONYMOUS_USERS = 
		"DELETE FROM users WHERE user_id IN (SELECT u.user_id AS uid FROM (SELECT * FROM users WHERE anonymous_user = 1) u);";
	
	@Autowired
	private DBConnection database;

	public String getUsername(int id) throws UserException {
		try {
			PreparedStatement statement = database.getConnection().prepareStatement(GET_USERNAME_FROM_DB);
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

	public boolean updateUser(int id, String firstName, String lastName, String profilePictureURL, String coverPhotoURL)
			throws UserException {
		try {
			PreparedStatement statement = database.getConnection().prepareStatement(UPDATE_USER_FROM_DB);
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, profilePictureURL);
			statement.setString(4, coverPhotoURL);
			statement.setInt(5, id);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new UserException("Database is not working", e);
		}
	}
	
	public int getUserIDByUsername(String username) throws UserException {
		try {
			PreparedStatement statement = database.getConnection().prepareStatement(GET_USER_ID_BY_USERNAME);
			statement.setString(1, username);
			ResultSet set = statement.executeQuery();
			if (set.next()) {
				return set.getInt("user_id");
			} else {
				throw new UserException("Wrong username");
			}
		} catch (SQLException e) {
			throw new UserException("Database is not working", e);
		}
	}
	
	public User getUser(int id) throws UserException {
		try {
			PreparedStatement statement = database.getConnection().prepareStatement(GET_USER_FROM_DB);
			statement.setInt(1, id);
			ResultSet set = statement.executeQuery();
			User user;
			// email,username,first_name,last_name,profile_picture,cover_photo,affection,photo_views
			if (set.next()) {
				String email = set.getString("email");
				String username = set.getString("username");
				String firstName = set.getString("first_name");
				String lastName = set.getString("last_name");
				String profilePictureURL = set.getString("profile_picture");
				String coverPhotoURL = set.getString("cover_photo");
				int affection = set.getInt("affection");
				int photo_views = set.getInt("photo_views");
				user = new User(username, email, firstName, lastName, profilePictureURL, coverPhotoURL, affection,
						photo_views);
				return user;
			} else {
				throw new UserException("Wrong id");
			}
		} catch (SQLException e) {
			throw new UserException("Database is not working", e);
		}
	}
	
	public User getUser(String username) throws UserException {
		try {
			PreparedStatement statement = database.getConnection().prepareStatement(GET_USER_FROM_DB_BY_USERNAME);
			statement.setString(1, username);
			ResultSet set = statement.executeQuery();
			User user;
			// email,username,first_name,last_name,profile_picture,cover_photo,affection,photo_views
			if (set.next()) {
				String email = set.getString("email");
				String firstName = set.getString("first_name");
				String lastName = set.getString("last_name");
				String profilePictureURL = set.getString("profile_picture");
				String coverPhotoURL = set.getString("cover_photo");
				int affection = set.getInt("affection");
				int photo_views = set.getInt("photo_views");
				user = new User(username, email, firstName, lastName, profilePictureURL, coverPhotoURL, affection,
						photo_views);
				return user;
			} else {
				throw new UserException("Wrong id");
			}
		} catch (SQLException e) {
			throw new UserException("Database is not working", e);
		}
	}

	@Override
	public int login(String username, String password) throws UserException {
		try {
			PreparedStatement statement = database.getConnection().prepareStatement(VALIDATE_USER);

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
	public int register(String username, String password, String email) {
		if ((username != null && username.trim().length() < User.MIN_USERNAME_LENGTH)
				&& (password != null && password.trim().length() < User.MIN_PASSWORD_LENGTH)
				&& (email != null && email.trim().length() < User.MIN_EMAIL_LENGTH)) {

			try {
				PreparedStatement registerUserStatement = database.getConnection().prepareStatement(ADD_USER_TO_DB, 
						PreparedStatement.RETURN_GENERATED_KEYS);
				registerUserStatement.setString(1, username);
				registerUserStatement.setString(2, password);
				registerUserStatement.setString(3, email);
				registerUserStatement.executeUpdate();

				password = null;
				
				return getLastIdFromStatement(registerUserStatement);

			} catch (SQLException e) {
				System.out.println("Something went wrong with the database");
			}

		}
		return INVALID_ID;
	}
	
	public int registerAnonymousUser(String username, String password, String email) throws UserException {
		
		try {
			int anonymousUserId = register(username, password, email);
			
			PreparedStatement registerAnonymousUserStatement = database.getConnection().prepareStatement(MAKE_USER_ANONYMOUS);
			registerAnonymousUserStatement.setInt(1, anonymousUserId);
			registerAnonymousUserStatement.executeUpdate();
			
			return anonymousUserId;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("Something went wrong with the database",e);
		}
	}

	public List<User> getUsers() {
		try {
			Statement st = database.getConnection().createStatement();
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

	public void addAlbum(int user_id, String name) {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(ADD_ALBUM_TO_DB);
			st.setInt(1, user_id);
			st.setString(2, name);
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while creating album");
		}
	}

	public List<Album> getAllAlbums(int user_id) throws UserException {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(GET_ALL_ALBUMS_BY_ID);
			st.setInt(1, user_id);
			ResultSet set = st.executeQuery();
			List<Album> albums = new ArrayList<>();
			while (set.next()) {
				albums.add(new Album(set.getString("name"), set.getInt("album_id")));
			}
			return albums;
		} catch (SQLException e) {
			throw new UserException("problem with getting albums from DB");
		}
	}

	public void addPostToAlbum(int post_id, int album_id) {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(ADD_POST_TO_ALBUM);
			st.setInt(1, post_id);
			st.setInt(2, album_id);
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while adding post to album");
		}
	}

	public void removePostFromAlbum(int post_id, int album_id) {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(REMOVE_POST_FROM_ALBUM);
			st.setInt(1, post_id);
			st.setInt(2, album_id);
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while removing post from album");
		}
	}
	
	public int getAlbumCreatorID(int album_id) throws UserException{
		int albumCreatorId=0;
		try {
			PreparedStatement st = database.getConnection().prepareStatement(GET_ALBUM_CREATOR_BY_ALBUM_ID);
			st.setInt(1, album_id);
			ResultSet set = st.executeQuery();
			while (set.next()) {
				albumCreatorId=set.getInt(1);
			}
			return albumCreatorId;
		} catch (SQLException e) {
			throw new UserException("problem with getting album creator ID from DB");
		}
	}
	
	public List<Integer> getAllPostIdsByAlbumID(int album_id) throws UserException {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(GET_ALL_POST_IDS_IN_ALBUM_BY_ALBUMID);
			st.setInt(1, album_id);
			ResultSet set = st.executeQuery();
			List<Integer> postIds = new ArrayList<>();
			while (set.next()) {
				postIds.add(set.getInt("post_id"));
			}
			return postIds;
		} catch (SQLException e) {
			throw new UserException("problem with getting album posts from DB");
		}
	}

	public Album getAlbumByID(int album_id) throws UserException {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(GET_ALBUM_BY_ALBUM_ID);
			st.setInt(1, album_id);
			ResultSet set = st.executeQuery();
			if (set.next()) {
				return new Album(set.getString("name"), set.getInt("album_id"));
			} else {
				throw new UserException("Couldn't get album by id");
			}
		} catch (SQLException e) {
			throw new UserException("problem with getting album posts from DB");
		}
	}
	public boolean albumExists(int user_id,String name) throws UserException{
		try {PreparedStatement st = database.getConnection().prepareStatement(CHECK_IF_ALBUM_EXISTS);
		st.setInt(1, user_id);
		st.setString(2, name);
		ResultSet set = st.executeQuery();
		if (set.next()) {
			return set.getInt(1)>0;
		}
		return false;
		}catch (SQLException e) {
			throw new UserException("problem with getting album posts from DB");
		}
	}

	public void followUser(int user_id,int followed_id){
		try {
			PreparedStatement st = database.getConnection().prepareStatement(FOLLOW_USER);
			st.setInt(1, followed_id);
			st.setInt(2, user_id);
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Couldn't follow user");
			return;
		}		
	}
	public void unfollowUser(int user_id,int followed_id){
		try {
			PreparedStatement st = database.getConnection().prepareStatement(UNFOLLOW_USER);
			st.setInt(1, followed_id);
			st.setInt(2, user_id);
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Couldn't unfollow user");
			return;
		}
	}
	public boolean checkUserFollowsUser(int user_id,int followed_id) throws UserException{
		try {PreparedStatement st = database.getConnection().prepareStatement(CHECK_IF_USER_IS_FOLLOWING_USER);
		st.setInt(1, followed_id);
		st.setInt(2, user_id);
		ResultSet set = st.executeQuery();
		if (set.next()) {
			System.out.println("\n\n Followers: "+set.getInt(1)+"\n\n");
			return set.getInt(1)>0;
		}
		return false;
		}catch (SQLException e) {
			throw new UserException("problem with getting follows from DB");
		}
	}
	
	public List<Integer> getUserIdsOfFollowedUsers(int user_id){
		List<Integer> followedUserIds = new ArrayList<>();
		try{
			PreparedStatement st = database.getConnection().prepareStatement(GET_FOLLOWED_USERS);
			st.setInt(1, user_id);
			ResultSet set=st.executeQuery();
			while(set.next()){
				followedUserIds.add(set.getInt("user_id"));
			}
		} catch (SQLException e) {
			System.out.println("Couldn't get followed users");
		}	
		return followedUserIds;
	}
	
	private int getLastIdFromStatement(PreparedStatement preparedStatement) throws SQLException {
		ResultSet keys = preparedStatement.getGeneratedKeys();
		if (keys.next()) {
			return keys.getInt(KEY_COLUMN_ID);
		}
		return INVALID_ID;
	}
	
	public void deleteAnonymousUsers() throws UserException {
		try {
			Statement st1 = database.getConnection().createStatement();
			st1.executeUpdate(REMOVE_SQL_SAFE_UPDATES);
			
			Statement st2 = database.getConnection().createStatement();
			st2.executeUpdate(DELETE_ANONYMOUS_USERS);
			
			Statement st3 = database.getConnection().createStatement();
			st3.executeUpdate(ADD_SQL_SAFE_UPDATES);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("Could not delete anonymous users", e);
		}
	}
	
	public String getUsernameOfFileOwnerByFileName(String fileName) throws PostException {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(GET_USERNAME_OF_FILE_OWNER_BY_FILE_NAME);
			st.setString(1, "%"+fileName);
			
			ResultSet set = st.executeQuery();
			
			if (set.next()) {
				return set.getString("username");
			} else {
				throw new PostException("Could not get username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException("Could not get username");
		}
	}
	public String getUsernameOfCoverPhotoFileOwnerByFileName(String fileName) throws PostException {
		try {
			PreparedStatement st = database.getConnection().prepareStatement(GET_USERNAME_OF_PROFILE_PICTURE);
			st.setString(1, "%"+fileName+"%");
			
			ResultSet set = st.executeQuery();
			
			if (set.next()) {
				return set.getString("username");
			} else {
				throw new PostException("Could not get username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException("Could not get username");
		}
	}
	
	
}
