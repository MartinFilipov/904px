package com.project.user;

import com.project.exceptions.UserException;

public class User {
	public static final int MIN_PASSWORD_LENGTH = 50;
	public static final int MIN_USERNAME_LENGTH = 20;
	public static final int MIN_EMAIL_LENGTH = 30;
	
	private String username;
	private String email;
	
	public User(String username, String email) throws UserException {
		setUsername(username);
		setEmail(email);
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

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
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
