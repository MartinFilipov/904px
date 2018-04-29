package com.project.model.post;

import java.io.File;
import java.io.IOException;


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

	private final String imageURL;
	private boolean nsfw;
	private String title;
	private String description;
	private String category;
	private String cameraModel;
	private Location location;
	private ImageCharacteristics imageCharacteristics;
//	private List<Comment> comments;

	private Post(Builder builder) {
		this.imageURL = builder.imageURL;
		this.nsfw = builder.nsfw;
		this.title = builder.title;
		this.description = builder.description;
		this.category = builder.category;
		this.cameraModel = builder.cameraModel;
		this.location = builder.location;
		this.imageCharacteristics = builder.imageCharacteristics;
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

		private String title = "";
		private String cameraModel = "";
		private String category = "";
		private boolean nsfw = false;
		private String description = "";
		private Location location = null;
		private ImageCharacteristics imageCharacteristics = null;

		public Builder(String imageURL) {
			this.imageURL = imageURL == null ? "" : imageURL;
			try {
				this.imageCharacteristics = initializeImageCharacteristics(imageURL);
			} catch (ImageProcessingException | IOException e) {
				System.out.println("Error extracting data from image");
				this.imageCharacteristics = new ImageCharacteristics("", "", "", "", "");
			}
		}

		public Builder category(PostCategory category) {
			if (category != null) {
				this.category = category.toString();
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

	public String getURL() {
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

	public Location getLocation() {
		return location;
	}

	public String getCameraModel() {
		return cameraModel;
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
