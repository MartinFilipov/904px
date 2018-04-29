package com.project.model.user;

import com.project.model.exceptions.UserException;

public class User {
	public static final int MIN_PASSWORD_LENGTH = 50;
	public static final int MIN_USERNAME_LENGTH = 20;
	public static final int MIN_EMAIL_LENGTH = 30;
	
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String profilePictureURL;
	private String coverPhotoURL;
	private int affection;
	private int photoViews;
	
	public User(String username, String email) throws UserException {
		setUsername(username);
		setEmail(email);
	}	

	public User(String username, String email, String firstName, String lastName, String profilePictureURL,
			String coverPhotoURL, int affection, int photoViews) throws UserException {
		setUsername(username);
		setEmail(email);
		setFirstName(firstName);
		setLastName(lastName);
		setProfilePictureURL(profilePictureURL);
		setCoverPhotoURL(coverPhotoURL);
		setAffection(affection);
		setPhotoViews(photoViews);
	}

	private void setUsername(String username) throws UserException {
		if (username != null && username.trim().length() < MIN_USERNAME_LENGTH) {
			this.username = username;
		} else {
			throw new UserException("Invalid username");
		}
	}
	
	private void setEmail(String email) throws UserException {
		if (email != null && email.trim().length() < MIN_EMAIL_LENGTH) {
			this.email = email;
		} else {
			throw new UserException("Invalid email");
		}
	}
	
	

	private void setFirstName(String firstName){
		if(firstName!=null && firstName.trim().length()>0){
			this.firstName = firstName;
		}else{
			this.firstName="";
		}
	}



	private void setLastName(String lastName){
		if(lastName!=null && lastName.trim().length()>0){
			this.lastName = lastName;
		}else{
			this.lastName="";
		}
	}



	private void setProfilePictureURL(String profilePictureURL){
		if(profilePictureURL!=null){
			this.profilePictureURL = profilePictureURL;
		}else{
			this.profilePictureURL="";
		}
	}



	private void setCoverPhotoURL(String coverPhotoURL){
		if(coverPhotoURL!=null){
			this.coverPhotoURL = coverPhotoURL;
		}else{
			this.coverPhotoURL="";
		}
	}

	private void setAffection(int affection){
		if(affection>=0){
			this.affection = affection;	
		}
	}
	
	private void setPhotoViews(int photoViews){
		if(photoViews>=0){
			this.photoViews=photoViews;
		}
	}
	
	
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getProfilePictureURL() {
		return profilePictureURL;
	}

	public String getCoverPhotoURL() {
		return coverPhotoURL;
	}

	public int getAffection() {
		return affection;
	}

	public int getPhotoViews() {
		return photoViews;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", email=" + email + "]";
	}

}
