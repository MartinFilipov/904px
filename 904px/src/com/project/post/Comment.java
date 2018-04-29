package com.project.post;


public class Comment {
	private String comment;
//	private User user;
	private String username;
	
	public Comment(String comment,String username){
		setComment(comment);
//		setUser(user);
		setUsername(username);
	}

	private void setComment(String comment) {
		if(comment!=null && comment.trim().length()>0){
			this.comment = comment;
		}else{
			this.comment="";
		}
	}
	
	private void setUsername(String username){
		if(username!=null && username.trim().length()>0){
			this.username = username;
		}else{
			this.username="";
		}
	}

//	private void setUser(User user) throws UserException {
//		if(user!=null){
//			this.user = user;
//		}else{
//			throw new UserException("Invalid user");
//		}
//	}

//	public User getUser() {
//		return user;
//	}

	public String getUsername(){
		return username;
	}
	public String getComment() {
		return comment;
	}
	
}
