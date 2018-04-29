package com.project.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.database.DBConnection;
import com.project.exceptions.UserException;
import com.project.imageCharacteristics.ImageCharacteristics;
import com.project.user.User;

public class PostDAO {
	private static final int INVALID_ID = 0;
	private static final int KEY_COLUMN_ID = 1;
	private static final String ADD_IMAGE_CHARACTERISTICS_TO_DATABASE = 
			"INSERT INTO image_characteristics(focal_length, f_number, exposure_time, iso_speed_ratings, date_taken, camera_id) VALUES(?,?,?,?,?,?);";
	private static final String ADD_POST_TO_DATABASE = 
			"INSERT INTO posts(image_url, title, description, nsfw, image_characteristics_id) VALUES(?,?,?,?,?);";
	private static final String DELETE_CAMERA_FROM_DATABASE = 
			"DELETE FROM cameras WHERE camera_id = ?";
	private static final String ADD_CAMERA_MODEL_TO_DATABASE = 
			"INSERT INTO cameras(model) VALUES(?);";
	private static final String ADD_COMMENT_TO_DATABASE=
			"INSERT into comments(text,post_id,user_id) values (?,?,?);";
	private static final String GET_ALL_COMMENTS_FROM_DATABASE=
			"SELECT text,username FROM comments c JOIN users u on c.user_id=u.user_id WHERE post_id=?";
	private static PostDAO instance;
	private Connection connection;

	private PostDAO() {
		try {
			connection = DBConnection.getInstance().getConnection();

		} catch (ClassNotFoundException e) {
			System.out.println("Missing database connection driver");
		} catch (SQLException e) {
			System.out.println("Something went wrong with the databse");
		}
	}
	
	public static PostDAO getInstance() {
		if (instance == null) {
			instance = new PostDAO();
		}
		return instance;
	}
	
	private int addCameraModel(String model) {
		try {
			PreparedStatement addCameraModelStatement = 
					connection.prepareStatement(ADD_CAMERA_MODEL_TO_DATABASE, PreparedStatement.RETURN_GENERATED_KEYS);
			
			addCameraModelStatement.setString(1, model);
			
			addCameraModelStatement.executeUpdate();
			
			ResultSet keys = addCameraModelStatement.getGeneratedKeys();
			if (keys.next()) {
				return keys.getInt(KEY_COLUMN_ID);
			}
		} catch (SQLException e) {
			System.out.println("--addCameraModel-- SQL syntax error");
		}
		return INVALID_ID;
	}
	
	//fix method for adding camera
	private int addImageCharacteristics(String model, ImageCharacteristics imageCharacteristics) {
		int cameraId = addCameraModel(model);
		
		if (cameraId == INVALID_ID) {
			return INVALID_ID;
		}
		
		String focalLength = imageCharacteristics.getFocalLength();
		String fNumber = imageCharacteristics.getfNumber();
		String exposureTime = imageCharacteristics.getExposureTime();
		String isoSpeedRatings = imageCharacteristics.getIsoSpeedRatings();
		String dateTaken = imageCharacteristics.getDateTaken();
		
		try {
			PreparedStatement addImageCharacteristicsStatement = 
					connection.prepareStatement(ADD_IMAGE_CHARACTERISTICS_TO_DATABASE, PreparedStatement.RETURN_GENERATED_KEYS);
			
			addImageCharacteristicsStatement.setString(1, focalLength);
			addImageCharacteristicsStatement.setString(2, fNumber);
			addImageCharacteristicsStatement.setString(3, exposureTime);
			addImageCharacteristicsStatement.setString(4, isoSpeedRatings);
			addImageCharacteristicsStatement.setString(5, dateTaken);
			addImageCharacteristicsStatement.setInt(6, cameraId);
			
			addImageCharacteristicsStatement.executeUpdate();
			
			ResultSet keys = addImageCharacteristicsStatement.getGeneratedKeys();
			if (keys.next()) {
				return keys.getInt(KEY_COLUMN_ID);
			}
	
			
		} catch (SQLException e) {
			System.out.println("--addImageCharacteristics-- SQL syntax error");
			
			deleteCameraModelFromDatabaseById(cameraId);
			
		}
		return INVALID_ID;
	}
	
	public boolean addPost(String imageURL, String title, String description, PostCategory category,
						String city, String country, boolean isNsfw) {
		Post post = new Post.Builder(imageURL)
				.title(title)
				.description(description)
				.category(category)
				.location(city, country)
				.nsfw(isNsfw)
				.build();		
				
		String cameraModel = post.getCameraModel();
		ImageCharacteristics imageCharacteristics = post.getImageCharacteristics();
		
		int imageCharacteristicsId = addImageCharacteristics(cameraModel, imageCharacteristics);
		
		if (imageCharacteristicsId == INVALID_ID) {
			return false;
		}
		
		try {
			PreparedStatement addPostStatement = 
					connection.prepareStatement(ADD_POST_TO_DATABASE);
		
			addPostStatement.setString(1, imageURL);
			addPostStatement.setString(2, title);
			addPostStatement.setString(3, description);
			addPostStatement.setString(4, (isNsfw ? "T" : "F"));
			addPostStatement.setInt(5, imageCharacteristicsId);
			
			addPostStatement.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			System.out.println("--addPost-- SQL syntax error");
			deleteImageCharacteristicsFromDatabaseById(imageCharacteristicsId);
		}
		return false;
	}
	
	private boolean deleteCameraModelFromDatabaseById(int cameraId) {
		PreparedStatement deleteCameraStatement;
		try {
			deleteCameraStatement = connection.prepareStatement(DELETE_CAMERA_FROM_DATABASE);
			
			deleteCameraStatement.setInt(1, cameraId);
			
			deleteCameraStatement.executeUpdate();
			
			return true;
		} catch (SQLException e1) {
			System.out.println("--deleting camera-- SQL syntax error");
		}
		return false;
	}
	
	private boolean deleteImageCharacteristicsFromDatabaseById(int imageCharacteristicsId) {
		try {
			PreparedStatement getCameraId = 
					connection.prepareStatement("select camera_id from image_characteristics where image_characteristics_id = ?;");
		
			getCameraId.setInt(1, imageCharacteristicsId);
			
			ResultSet set = getCameraId.executeQuery();
			if (set.next()) {
				int cameraId = set.getInt(1);
				
				PreparedStatement st = 
						connection.prepareStatement("DELETE FROM image_characteristics where image_characteristics_id = ?;");
				st.setInt(1, imageCharacteristicsId);
				st.executeUpdate();
				
				deleteCameraModelFromDatabaseById(cameraId);
			}
		} catch (SQLException e) {
			System.out.println("--deleteImageCharacteristicsFromDatabaseById-- SQL syntax error");
		}
		return false;
	}
	
	public boolean addComment(int userID,int postID,String comment){
		try{
			PreparedStatement statement=connection.prepareStatement(ADD_COMMENT_TO_DATABASE);
			statement.setString(1, comment);
			statement.setInt(2, postID);
			statement.setInt(3, userID);
			statement.executeUpdate();
			return true;
		}catch(SQLException e){
			System.out.println("Something went wrong with the database while adding a comment");
			return false;
		}
	}
	
	
	
	// FIX this
	public List<Comment> getAllComments(int postID) throws PostException{
		try{
			PreparedStatement statement=connection.prepareStatement(GET_ALL_COMMENTS_FROM_DATABASE);
			statement.setInt(1, postID);
			statement.executeQuery();
			ResultSet set = statement.executeQuery();
			
			List<Comment> comments = new ArrayList<>();
			while (set.next()) {
				comments.add(new Comment(set.getString("text"),set.getString("username")));				
			}
			return comments;
		}catch(SQLException e){
			throw new PostException("Something went wrong with the database");
		}
	}

}
