package com.project.model.user;

public class Album {
	private String name;
	private int id;

	
	public Album(String name, int id) {
		setName(name);
		setId(id);
	}
	public String getName() {
		return name;
	}
	private void setName(String name) {
		if(name!=null && name.trim().length()>0){
			this.name = name;
		}else{
			this.name="Invalid name";
		}
	}
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}
	
}
