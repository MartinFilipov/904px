package com.project.model.ajax;

import java.util.List;

public class AjaxResponseBody {
	
//	@JsonView(Views.Public.class)
	String msg;
	
//	@JsonView(Views.Public.class)
	String code;
	
//	@JsonView(Views.Public.class)
	List<String> result;

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

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}
	
	
}
