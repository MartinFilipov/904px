package com.project.model.post;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.project.model.imageCharacteristics.ImageCharacteristics;
import com.project.model.location.Location;

public class Post {
	private static final int CAMERA_MODEL_DESCRIPTION_ID = 272;
	private static final int CAMERA_FOCAL_LENTH_DESCRIPTION_ID = 37386;
	private static final int CAMERA_F_NUMBER_DESCRIPTION_ID = 33437;
	private static final int CAMERA_EXPOSURE_TIME_DESCRIPTION_ID = 33434;
	private static final int CAMERA_ISO_SPEED_RATINGS_DESCRIPTION_ID = 34855;
	private static final int CAMERA_DATE_TAKEN_DESCRIPTION_ID = 36867;

	private int id;
	private final String imageURL;
	private boolean nsfw;
	private String title;
	private String description;
	private String category;
	private String cameraModel;
	private Location location;
	private ImageCharacteristics imageCharacteristics;
	private int likes;
	private LocalDate dateUploaded;
	private int views;
	private String imageName;
//	private List<Comment> comments;

	private Post(Builder builder) {
		this.imageURL = builder.imageURL;
		this.imageName = builder.imageName;
		this.nsfw = builder.nsfw;
		this.title = builder.title;
		this.description = builder.description;
		this.category = builder.category;
		this.cameraModel = builder.cameraModel;
		this.location = builder.location;
		this.imageCharacteristics = builder.imageCharacteristics;
		this.id = builder.id;
		this.likes = builder.likes;
		this.dateUploaded = builder.dateUploaded;
		this.views = builder.views;
//		this.comments = new ArrayList<Comment>();
	}

//	public boolean addComment(User commenter, String comment) throws UserException {
//		if (commenter == null || comment == null || !(comment.trim().length() > 0)) {
//			throw new UserException("Invalid user or comment");
//		}
//		synchronized (comments) {
//			this.comments.add(new Comment(commenter, comment));
//			return true;
//		}
//	}
//
//	public Collection<Comment> getComments() {
//		return Collections.unmodifiableCollection(comments);
//	}

	public static class Builder {
		private final String imageURL;
		private String imageName = "";
		
		private int id = 0;
		private String title = "";
		private String cameraModel = "";
		private String category = "";
		private boolean nsfw = false;
		private String description = "";
		private Location location = null;
		private ImageCharacteristics imageCharacteristics = null;
		private int likes = 0;
		private LocalDate dateUploaded = LocalDate.now();
		private int views = 0;

		public Builder(String imageURL) {
			this.imageURL = imageURL == null ? "" : imageURL;
			try {
				this.imageCharacteristics = initializeImageCharacteristics(imageURL);
			} catch (ImageProcessingException | IOException e) {
				System.out.println("Error extracting data from image");
				this.imageCharacteristics = new ImageCharacteristics("", "", "", "", "");
			}
		}
		
		public Builder views(int views) {
			if (views > 0) {
				this.views = views;
			}
			return this;
		}
		
		public Builder dateUploaded(LocalDate dateUploaded) {
			if (dateUploaded != null) {
				this.dateUploaded = dateUploaded;
			}
			return this;
		}
		
		public Builder likes(int likes) {
			if (likes > 0) {
				this.likes = likes;
			}
			return this;
		}
		
		public Builder id(int id) {
			if (id > 0) {
				this.id = id;
			}
			return this;
		}

		public Builder category(String category) {
			if (category != null) {
				this.category = category;
			}
			return this;
		}

		public Builder title(String title) {
			if (title != null) {
				this.title = title;
			}
			return this;
		}

		public Builder nsfw(boolean nsfw) {
			this.nsfw = nsfw;
			return this;
		}

		public Builder description(String description) {
			if (description != null) {
				this.description = description;
			}
			return this;
		}

		public Builder location(String city, String country) {
			if (city != null && country != null) {
				this.location = new Location(city, country);
			}
			return this;
		}

		public Post build() {
			return new Post(this);
		}

		private ImageCharacteristics initializeImageCharacteristics(String imageURL)
				throws ImageProcessingException, IOException {
			File file = new File(imageURL);
			if (file.exists()) {
				this.imageName = file.getName();
			}

			Metadata metadata = ImageMetadataReader.readMetadata(file);

			ExifIFD0Directory directoryIFDO = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			ExifSubIFDDirectory directorySubIFD = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

			String model = "";
			String focalLength = "";
			String fNumber = "";
			String exposureTime = "";
			String isoSpeedRatings = "";
			String dateTaken = "";

			if (directoryIFDO != null) {
				model = directoryIFDO.getDescription(CAMERA_MODEL_DESCRIPTION_ID);
				this.cameraModel = model;
			}
			if (directorySubIFD != null) {
				focalLength = directorySubIFD.getDescription(CAMERA_FOCAL_LENTH_DESCRIPTION_ID);
				fNumber = directorySubIFD.getDescription(CAMERA_F_NUMBER_DESCRIPTION_ID);
				exposureTime = directorySubIFD.getDescription(CAMERA_EXPOSURE_TIME_DESCRIPTION_ID);
				isoSpeedRatings = directorySubIFD.getDescription(CAMERA_ISO_SPEED_RATINGS_DESCRIPTION_ID);
				// returns only date
				dateTaken = directorySubIFD.getDescription(CAMERA_DATE_TAKEN_DESCRIPTION_ID).split(" ")[0];
			}

			return new ImageCharacteristics(focalLength, fNumber, exposureTime, isoSpeedRatings, dateTaken);
		}
	}
	
	

	public String getImageName() {
		return imageName;
	}

	public int getLikes() {
		return likes;
	}

	public LocalDate getDateUploaded() {
		return dateUploaded;
	}

	public int getViews() {
		return views;
	}

	public int getId() {
		return id;
	}

	public String getImageURL() {
		return imageURL;
	}

	public boolean isNsfw() {
		return nsfw;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public String getCameraModel() {
		return cameraModel;
	}

	public Location getLocation() {
		return location;
	}

	public ImageCharacteristics getImageCharacteristics() {
		return imageCharacteristics;
	}

	@Override
	public String toString() {
		return "Post [url=" + imageURL + ", nsfw=" + nsfw + ", title=" + title + ", description=" + description
				+ ", location=" + location + ", camera model=" + cameraModel + ", category=" + category + "\n"
				+ "image characteristics=\n" + this.imageCharacteristics.toString();
	}

}
