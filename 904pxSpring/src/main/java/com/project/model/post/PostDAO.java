package com.project.model.post;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.database.DBConnection;
import com.project.model.imageCharacteristics.ImageCharacteristics;
import com.project.model.user.UserDAO;

public class PostDAO {
	private static final int INVALID_ID = 0;
	private static final int KEY_COLUMN_ID = 1;
	private static final String SELECT_CATEGORY_FROM_DATABASE = "SELECT category_id FROM categories WHERE category_name = ?;";
	private static final String SELECT_CAMERA_MODEL_FROM_DATABASE = "SELECT camera_id FROM cameras WHERE model=?";
	private static final String CHECK_FOR_CAMERA_MODEL_IN_DATABASE = "SELECT COUNT(*) FROM cameras WHERE model = ?;";
	private static final String ADD_IMAGE_CHARACTERISTICS_TO_DATABASE = "INSERT INTO image_characteristics(focal_length, f_number, exposure_time, iso_speed_ratings, date_taken, camera_id) VALUES(?,?,?,?,?,?);";
	private static final String ADD_POST_TO_DATABASE = "INSERT INTO posts(image_url, title, description, nsfw, image_characteristics_id, location_id, user_id, category_id, date_uploaded) VALUES(?,?,?,?,?,?,?,?,?);";
	private static final String DELETE_CAMERA_FROM_DATABASE = "DELETE FROM cameras WHERE camera_id = ?";
	private static final String ADD_CAMERA_MODEL_TO_DATABASE = "INSERT INTO cameras(model) VALUES(?);";
	private static final String ADD_LOCATION_TO_DATABASAE = "INSERT INTO locations(city, country) VALUES(?,?);";
	private static final String POST_DATA = "SELECT p.image_url, p.description, p.nsfw, p.title,p.views, p.date_uploaded, ";
	private static final String LOCATION_DATA = "l.city, l.country, ";
	private static final String IMAGE_CHARACTERISTICS_DATA = "i.date_taken, i.exposure_time, i.f_number, i.focal_length, i.iso_speed_ratings, ";
	private static final String GET_POST_BY_ID = POST_DATA + LOCATION_DATA + IMAGE_CHARACTERISTICS_DATA
			+ "c.model, cat.category_name " + "FROM POSTS p "
			+ "JOIN categories cat ON cat.category_id = p.category_id "
			+ "JOIN locations l ON l.location_id = p.location_id "
			+ "JOIN image_characteristics i ON p.image_characteristics_id = i.image_characteristics_id "
			+ "JOIN cameras c ON c.camera_id = i.camera_id " + "WHERE p.post_id = ?;";
	private static final String GET_ALL_USER_UPLOAD_IDS = "SELECT p.post_id FROM posts p JOIN users u on p.user_id = u.user_id WHERE u.user_id = ?;";
	private static final String ADD_COMMENT_TO_DATABASE = "INSERT into comments(comment,post_id,user_id) values (?,?,?);";
	private static final String GET_ALL_COMMENTS_FROM_DATABASE = "SELECT c.comment,u.username,c.comment_id,(SELECT count(user_id) from comments_has_likes WHERE comment_id=c.comment_id) as likes FROM comments c JOIN users u on c.user_id=u.user_id WHERE post_id=? ORDER BY likes DESC;";
	private static final String GET_LAST_ADDED_COMMENT_BY_POST_ID = "SELECT c.comment,u.username,c.comment_id,(SELECT count(user_id) from comments_has_likes WHERE comment_id=c.comment_id) as likes FROM comments c JOIN users u on c.user_id=u.user_id WHERE post_id=? ORDER BY comment_id DESC LIMIT 1;";
	private static final String INCREASE_LIKES_OF_COMMENT_BY_COMMENT_ID = "INSERT into comments_has_likes (comment_id,user_id) values(?,?);";
	private static final String DECREASE_LIKES_OF_COMMENT_BY_COMMENT_ID = "DELETE FROM comments_has_likes where comment_id=? and user_id=?;";
	private static final String GET_LIKES_OF_COMMENT_BY_COMMENT_AND_USER_ID = "select count(*) as liked from comments_has_likes where comment_id=? and user_id=?";
	private static final String INCREMENT_POST_VIEWS_BY_ID = "UPDATE posts SET views = views + 1 WHERE post_id = ?";
	private static final String GET_FRESH_POST_IDS = "SELECT post_id FROM posts ORDER BY post_id desc;";
	private static final String GET_POST_CREATOR = "select u.username from users u JOIN posts p on u.user_id=p.user_id where p.post_id=?;";
	private static final String SEARCH_FOR_POSTS_BY_TITLE_OR_CATEGORY = "select p.post_id,p.title,c.category_name from posts p join categories c on p.category_id=c.category_id where p.title like ? or c.category_name like ?;";
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
		System.out.println("\ncamera model = " + cameraModel);
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
			addPostStatement.setDate(9, Date.valueOf(LocalDate.now()));

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
				boolean nsfw = set.getString("nsfw").equals("T");
				System.out.println("Not safe for work: " + nsfw);
				// int likes = set.getInt("likes");
				int views = set.getInt("views");
				LocalDate dateUploaded = set.getDate("date_uploaded").toLocalDate();

				String dateTaken = set.getString("date_taken");
				String exposureTime = set.getString("exposure_time");
				String fNumber = set.getString("f_number");
				String focalLength = set.getString("focal_length");
				String isoSpeedRatings = set.getString("iso_speed_ratings");
				String cameraModel = set.getString("model");

				Post post = new Post.Builder(imageURL).title(title).category(category).id(id).description(description)
						.location(city, country).nsfw(nsfw).views(views).dateUploaded(dateUploaded).build();

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

	public List<Post> getAllFollowedUserPostsByUserID(int user_id) {
		List<Post> feed = new ArrayList<>();
		UserDAO dao = UserDAO.getInstance();
		List<Integer> followedIds = dao.getUserIdsOfFollowedUsers(user_id);
		for (Integer followedUserID : followedIds) {
			feed.addAll(getUserUploads(followedUserID));
		}
		return Collections.unmodifiableList(feed);
	}

	public List<Post> searchForPosts(String search) {
		List<Post> results = new ArrayList<>();
		try {
			PreparedStatement statement = connection.prepareStatement(SEARCH_FOR_POSTS_BY_TITLE_OR_CATEGORY);
			statement.setString(1, '%'+search+'%');
			statement.setString(2, '%'+search+'%');
			ResultSet set = statement.executeQuery();
			while (set.next()) {
				int postId = set.getInt("post_id");
				
				Post post;
				try {
					post = getPostById(postId);
				} catch (PostException e) {
					System.out.println("Could not get post");
					return new ArrayList<Post>();
				}
				results.add(post);
			}
		} catch (SQLException e) {
			System.out.println("something went wrong while getting search results");
			e.printStackTrace();
		}
		return Collections.unmodifiableList(results);
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

	public Comment getLastAddedCommentByPostID(int postID) throws PostException {
		try {
			PreparedStatement statement = connection.prepareStatement(GET_LAST_ADDED_COMMENT_BY_POST_ID);
			statement.setInt(1, postID);
			ResultSet set = statement.executeQuery();
			Comment comment = null;
			while (set.next()) {
				comment = new Comment(set.getString("comment"), set.getString("username"), set.getInt("likes"),
						set.getInt("comment_id"));
			}
			System.out.println("\n Zaqvkata mina");
			return comment;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException("Something went wrong with the database");
		}

	}

	public List<Comment> getAllComments(int postID) throws PostException {
		try {
			PreparedStatement statement = connection.prepareStatement(GET_ALL_COMMENTS_FROM_DATABASE);
			statement.setInt(1, postID);
			ResultSet set = statement.executeQuery();
			List<Comment> comments = new ArrayList<>();
			while (set.next()) {
				comments.add(new Comment(set.getString("comment"), set.getString("username"), set.getInt("likes"),
						set.getInt("comment_id")));
			}
			System.out.println("\n Zaqvkata mina");
			return comments;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException("Something went wrong with the database");
		}
	}

	public String getPostCreator(int postId) throws PostException {
		String username = null;
		try {
			PreparedStatement statement = connection.prepareStatement(GET_POST_CREATOR);
			statement.setInt(1, postId);
			ResultSet set = statement.executeQuery();
			if (set.next()) {
				username = set.getString(KEY_COLUMN_ID);
			}
			return username;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException("Something went wrong with the database");
		}
	}

	public void increasePostViewsById(int postId) {
		try {
			PreparedStatement st = connection.prepareStatement(INCREMENT_POST_VIEWS_BY_ID);
			st.setInt(1, postId);
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println("--increasePostViewsById-- SQL syntax error");
		}

	}

	public void increaseLikesOfComment(int commentId, int user_id) {
		try {
			PreparedStatement statement = connection.prepareStatement(INCREASE_LIKES_OF_COMMENT_BY_COMMENT_ID);
			statement.setInt(1, commentId);
			statement.setInt(2, user_id);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while updating likes");
		}
	}

	public void decreaseLikesOfComment(int commentId, int user_id) {
		try {
			PreparedStatement statement = connection.prepareStatement(DECREASE_LIKES_OF_COMMENT_BY_COMMENT_ID);
			statement.setInt(1, commentId);
			statement.setInt(2, user_id);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while updating likes");
		}
	}

	public boolean checkIfCommentIsLikedByUser(int commentId, int user_id) {
		try {
			PreparedStatement statement = connection.prepareStatement(GET_LIKES_OF_COMMENT_BY_COMMENT_AND_USER_ID);
			statement.setInt(1, commentId);
			statement.setInt(2, user_id);
			ResultSet set = statement.executeQuery();
			if (set.next()) {
				return set.getInt(KEY_COLUMN_ID) > 0;
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong while updating likes");
		}
		return false;
	}

	public List<Post> getFreshPosts() throws PostException {
		List<Post> posts = new ArrayList<>();
		try {
			PreparedStatement statement = connection.prepareStatement(GET_FRESH_POST_IDS);
			ResultSet set = statement.executeQuery();
			while (set.next()) {
				posts.add(getPostById(set.getInt("post_id")));
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong while updating likes");
		}
		return posts;
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
