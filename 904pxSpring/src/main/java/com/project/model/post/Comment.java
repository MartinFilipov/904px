package com.project.model.post;


public class Comment {
	private int id;
	private String comment;
//	private User user;
	private String username;
	private int likes;
	
	public Comment(String comment,String username,int likes,int id){
		setComment(comment);
		setLikes(likes);
//		setUser(user);
		setUsername(username);
		setId(id);
	}

	private void setId(int id) {
		this.id=id;		
	}
	
	private void setLikes(int likes) {
		this.likes=likes;
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
	public int getLikes(){
		return likes;
	}
	public int getId(){
		return id;
	}
}
