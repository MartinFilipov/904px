package com.project.model.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.project.model.database.DBConnection;
import com.project.model.imageCharacteristics.ImageCharacteristics;

public class PostDAO {
	private static final int INVALID_ID = 0;
	private static final int KEY_COLUMN_ID = 1;
	private static final String SELECT_CATEGORY_FROM_DATABASE = "SELECT category_id FROM categories WHERE category_name = ?;";
	private static final String SELECT_CAMERA_MODEL_FROM_DATABASE = "SELECT camera_id FROM cameras WHERE model=?";
	private static final String CHECK_FOR_CAMERA_MODEL_IN_DATABASE = "SELECT COUNT(*) FROM cameras WHERE model = ?;";
	private static final String ADD_IMAGE_CHARACTERISTICS_TO_DATABASE = "INSERT INTO image_characteristics(focal_length, f_number, exposure_time, iso_speed_ratings, date_taken, camera_id) VALUES(?,?,?,?,?,?);";
	private static final String ADD_POST_TO_DATABASE = "INSERT INTO posts(image_url, title, description, nsfw, image_characteristics_id, location_id, user_id, category_id) VALUES(?,?,?,?,?,?,?,?);";
	private static final String DELETE_CAMERA_FROM_DATABASE = "DELETE FROM cameras WHERE camera_id = ?";
	private static final String ADD_CAMERA_MODEL_TO_DATABASE = "INSERT INTO cameras(model) VALUES(?);";
	private static final String ADD_LOCATION_TO_DATABASAE = "INSERT INTO locations(city, country) VALUES(?,?);";
	private static final String POST_DATA = "SELECT p.image_url, p.description, p.nsfw, p.title, ";
	private static final String LOCATION_DATA = "l.city, l.country, ";
	private static final String IMAGE_CHARACTERISTICS_DATA = "i.date_taken, i.exposure_time, i.f_number, i.focal_length, i.iso_speed_ratings, ";
	private static final String GET_POST_BY_ID = POST_DATA + LOCATION_DATA + IMAGE_CHARACTERISTICS_DATA
			+ "c.model, cat.category_name " + "FROM POSTS p "
			+ "JOIN categories cat ON cat.category_id = p.category_id "
			+ "JOIN locations l ON l.location_id = p.location_id "
			+ "JOIN image_characteristics i ON p.image_characteristics_id = i.image_characteristics_id "
			+ "JOIN cameras c ON c.camera_id = i.camera_id " + "WHERE p.post_id = ?;";
	private static final String GET_ALL_USER_UPLOAD_IDS = "SELECT p.post_id FROM posts p JOIN users u on p.user_id = u.user_id WHERE u.user_id = ?;";
	private static final String ADD_COMMENT_TO_DATABASE = "INSERT into comments(comment,post_id,user_id,likes) values (?,?,?,0);";
	private static final String GET_ALL_COMMENTS_FROM_DATABASE = "SELECT comment,username,likes,comment_id FROM comments c JOIN users u on c.user_id=u.user_id WHERE post_id=? ORDER BY likes DESC";
	private static final String INCREASE_LIKES_OF_COMMENT_BY_COMMENT_ID="UPDATE comments SET likes=likes+1 WHERE comment_id=?;";
	private static final int CAMERA_EXISTS = 1;
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

	private int selectCameraModel(String model) {
		try {
			PreparedStatement st = connection.prepareStatement(SELECT_CAMERA_MODEL_FROM_DATABASE);
			st.setString(1, model);

			ResultSet set = st.executeQuery();

			if (set.next()) {
				return set.getInt(KEY_COLUMN_ID);
			}
		} catch (SQLException e) {
			System.out.println("--selectCameraModel-- SQL syntax error");
		}
		return INVALID_ID;
	}

	private int addCameraModel(String model) {
		try {
			PreparedStatement addCameraModelStatement = connection.prepareStatement(ADD_CAMERA_MODEL_TO_DATABASE,
					PreparedStatement.RETURN_GENERATED_KEYS);

			addCameraModelStatement.setString(1, model);

			addCameraModelStatement.executeUpdate();

			System.out.println("newly added camera id = " + getLastIdFromStatement(addCameraModelStatement));

			return getLastIdFromStatement(addCameraModelStatement);
		} catch (SQLException e) {
			System.out.println("--addCameraModel-- SQL syntax error");
		}
		return INVALID_ID;
	}

	// fix method for adding camera
	private int addImageCharacteristics(String model, ImageCharacteristics imageCharacteristics) {
		int cameraId = 0;

		try {
			PreparedStatement checkCameraStatement = connection.prepareStatement(CHECK_FOR_CAMERA_MODEL_IN_DATABASE);
			checkCameraStatement.setString(1, model);
			ResultSet set = checkCameraStatement.executeQuery();
			if (set.next()) {
				int result = set.getInt(1);

				System.out.println("result is = " + result);

				if (result == CAMERA_EXISTS) {
					cameraId = selectCameraModel(model);
				} else {
					cameraId = addCameraModel(model);
				}
			}

		} catch (SQLException e1) {
			System.out.println("--checkForCameraModel-- SQL syntax error");
			e1.printStackTrace();
		}

		System.out.println("cameraId in addImage... = " + cameraId);

		if (cameraId == INVALID_ID) {
			return INVALID_ID;
		}

		String focalLength = imageCharacteristics.getFocalLength();
		String fNumber = imageCharacteristics.getfNumber();
		String exposureTime = imageCharacteristics.getExposureTime();
		String isoSpeedRatings = imageCharacteristics.getIsoSpeedRatings();
		String dateTaken = imageCharacteristics.getDateTaken();

		try {
			PreparedStatement addImageCharacteristicsStatement = connection
					.prepareStatement(ADD_IMAGE_CHARACTERISTICS_TO_DATABASE, PreparedStatement.RETURN_GENERATED_KEYS);

			addImageCharacteristicsStatement.setString(1, focalLength);
			addImageCharacteristicsStatement.setString(2, fNumber);
			addImageCharacteristicsStatement.setString(3, exposureTime);
			addImageCharacteristicsStatement.setString(4, isoSpeedRatings);
			addImageCharacteristicsStatement.setString(5, dateTaken);
			addImageCharacteristicsStatement.setInt(6, cameraId);

			addImageCharacteristicsStatement.executeUpdate();

			System.out.println("imageCharId = " + getLastIdFromStatement(addImageCharacteristicsStatement));

			return getLastIdFromStatement(addImageCharacteristicsStatement);

		} catch (SQLException e) {
			System.out.println("--addImageCharacteristics-- SQL syntax error");

			deleteCameraModelFromDatabaseById(cameraId);

		}
		return INVALID_ID;
	}

	private int addLocation(String city, String country) {
		try {
			PreparedStatement addLocationStatement = connection.prepareStatement(ADD_LOCATION_TO_DATABASAE,
					PreparedStatement.RETURN_GENERATED_KEYS);
			addLocationStatement.setString(1, city);
			addLocationStatement.setString(2, country);

			addLocationStatement.executeUpdate();

			return getLastIdFromStatement(addLocationStatement);

		} catch (SQLException e) {
			System.out.println("--addLocation-- SQL syntax error");
		}
		return INVALID_ID;
	}

	private int selectCategory(String category) {
		try {
			PreparedStatement selectCategoryStatement = connection.prepareStatement(SELECT_CATEGORY_FROM_DATABASE);
			selectCategoryStatement.setString(1, category);
			ResultSet set = selectCategoryStatement.executeQuery();
			if (set.next()) {
				return set.getInt(KEY_COLUMN_ID);
			}
		} catch (SQLException e) {
			System.out.println("--selectCategory-- SQL syntax error");
			e.printStackTrace();
		}
		return INVALID_ID;
	}

	public int uploadPostToUser(int userId, String imageURL, String title, String description, String category,
			String city, String country, boolean isNsfw) {

		int locationId = addLocation(city, country);

		if (locationId == INVALID_ID) {
			return INVALID_ID;
		}

		int categoryId = selectCategory(category);

		if (categoryId == INVALID_ID) {
			return INVALID_ID;
		}

		Post post = new Post.Builder(imageURL).title(title).description(description).category(category)
				.location(city, country).nsfw(isNsfw).build();

		String cameraModel = post.getCameraModel();
		ImageCharacteristics imageCharacteristics = post.getImageCharacteristics();

		int imageCharacteristicsId = addImageCharacteristics(cameraModel, imageCharacteristics);

		System.out.println("imageCharId in addPost = " + imageCharacteristicsId);

		if (imageCharacteristicsId == INVALID_ID) {
			return INVALID_ID;
		}

		try {
			PreparedStatement addPostStatement = connection.prepareStatement(ADD_POST_TO_DATABASE,
					PreparedStatement.RETURN_GENERATED_KEYS);

			addPostStatement.setString(1, imageURL);
			addPostStatement.setString(2, title);
			addPostStatement.setString(3, description);
			addPostStatement.setString(4, (isNsfw ? "T" : "F"));
			addPostStatement.setInt(5, imageCharacteristicsId);
			addPostStatement.setInt(6, locationId);
			addPostStatement.setInt(7, userId);
			addPostStatement.setInt(8, categoryId);

			addPostStatement.executeUpdate();

			System.out.println("postId = " + getLastIdFromStatement(addPostStatement));

			return getLastIdFromStatement(addPostStatement);

		} catch (SQLException e) {
			System.out.println("--uploadPost-- SQL syntax error");
			deleteImageCharacteristicsFromDatabaseById(imageCharacteristicsId);
		}
		return INVALID_ID;
	}

	private int getLastIdFromStatement(PreparedStatement preparedStatement) throws SQLException {
		ResultSet keys = preparedStatement.getGeneratedKeys();
		if (keys.next()) {
			return keys.getInt(KEY_COLUMN_ID);
		}
		return INVALID_ID;
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
			PreparedStatement getCameraId = connection.prepareStatement(
					"select camera_id from image_characteristics where image_characteristics_id = ?;");

			getCameraId.setInt(1, imageCharacteristicsId);

			ResultSet set = getCameraId.executeQuery();
			if (set.next()) {
				int cameraId = set.getInt(1);

				PreparedStatement st = connection
						.prepareStatement("DELETE FROM image_characteristics where image_characteristics_id = ?;");
				st.setInt(1, imageCharacteristicsId);
				st.executeUpdate();

				deleteCameraModelFromDatabaseById(cameraId);
			}
		} catch (SQLException e) {
			System.out.println("--deleteImageCharacteristicsFromDatabaseById-- SQL syntax error");
		}
		return false;
	}

	public Post getPostById(int id) throws PostException {
		try {
			PreparedStatement st = connection.prepareStatement(GET_POST_BY_ID);
			st.setInt(1, id);

			ResultSet set = st.executeQuery();

			if (set.next()) {
				String imageURL = set.getString("image_url");
				String title = set.getString("title");
				String description = set.getString("description");
				String category = set.getString("category_name");
				String city = set.getString("city");
				String country = set.getString("country");
				boolean nfsw = set.getString("nsfw") == "T";

				String dateTaken = set.getString("date_taken");
				String exposureTime = set.getString("exposure_time");
				String fNumber = set.getString("f_number");
				String focalLength = set.getString("focal_length");
				String isoSpeedRatings = set.getString("iso_speed_ratings");
				String cameraModel = set.getString("model");

				Post post = new Post.Builder(imageURL).title(title).category(category).id(id).description(description)
						.location(city, country).build();

				return post;
			} else {
				throw new PostException("No such post");
			}

		} catch (SQLException e) {
			System.out.println("--getPostById-- SQL syntax error");
			e.printStackTrace();
		}

		return new Post.Builder("").build();
	}

	public Collection<Post> getUserUploads(int userId) {
		try {
			PreparedStatement getUserUploadsStatement = connection.prepareStatement(GET_ALL_USER_UPLOAD_IDS);
			getUserUploadsStatement.setInt(1, userId);

			ResultSet set = getUserUploadsStatement.executeQuery();

			List<Post> userUploads = new ArrayList<>();

			while (set.next()) {
				int postId = set.getInt("post_id");

				Post post;
				try {
					post = getPostById(postId);
				} catch (PostException e) {
					System.out.println("Could not get post");
					return new ArrayList<Post>();
				}

				userUploads.add(post);

			}

			return Collections.unmodifiableList(userUploads);

		} catch (SQLException e) {
			System.out.println("--getUserUploads-- SQL syntax erro");
			e.printStackTrace();
		}
		return new ArrayList<Post>();
	}

	public List<PostCategory> getAllCategories() {
		return Collections.unmodifiableList(Arrays.asList(PostCategory.values()));
	}

	public boolean addComment(int userID, int postID, String comment) {
		try {
			PreparedStatement statement = connection.prepareStatement(ADD_COMMENT_TO_DATABASE);
			statement.setString(1, comment);
			statement.setInt(2, postID);
			statement.setInt(3, userID);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("Something went wrong with the database while adding a comment");
			return false;
		}
	}


	public List<Comment> getAllComments(int postID) throws PostException {
		try {
			PreparedStatement statement = connection.prepareStatement(GET_ALL_COMMENTS_FROM_DATABASE);
			statement.setInt(1, postID);
			ResultSet set = statement.executeQuery();
			List<Comment> comments = new ArrayList<>();
			while (set.next()) {
				comments.add(new Comment(set.getString("comment"), set.getString("username"),set.getInt("likes"),set.getInt("comment_id")));
			}
			System.out.println("\n Zaqvkata mina");
			return comments;
		} catch (SQLException e) {
			throw new PostException("Something went wrong with the database");
		}
	}
	public void increaseLikesByCommentID(int commentId){
		try {
			PreparedStatement statement = connection.prepareStatement(INCREASE_LIKES_OF_COMMENT_BY_COMMENT_ID);
			statement.setInt(1, commentId);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while updating likes");
		}
	}

	private void loadAllCategoriesToDatabase() {
	 List<PostCategory> categories = PostDAO.getInstance().getAllCategories();
	
	 for (PostCategory cat : categories) {
	 try {
	 PreparedStatement st = connection.prepareStatement("INSERT INTO categories(category_name) VALUES(?);");
	 st.setString(1, cat.toString());
	 st.executeUpdate();
	
	 } catch (SQLException e) {
	 System.out.println("Inserting failed");
	 }
	
	 }
	 }

	public static void main(String[] args) {
		PostDAO.getInstance().loadAllCategoriesToDatabase();
	}

}
