package com.project.model.ajax;



import com.project.model.post.Comment;

public class AjaxResponseBody {
	
//	@JsonView(Views.Public.class)
	String msg;
	
//	@JsonView(Views.Public.class)
	String code;
	
//	@JsonView(Views.Public.class)
	Comment result;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Comment getResult() {
		return result;
	}

	public void setResult(Comment result) {
		this.result = result;
	}


	
	
}
