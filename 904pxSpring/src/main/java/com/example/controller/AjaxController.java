package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.ajax.AjaxResponseBody;
import com.project.model.ajax.SearchCriteria;

@RestController
public class AjaxController {

	@ResponseBody
	@RequestMapping(value = "/search/api/getSearchResult")
	public AjaxResponseBody getSearchResultViaAjax(@RequestBody SearchCriteria search) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		result.setMsg("success");
		result.setCode("200");
		List<String> resultList=new ArrayList<String>();
		resultList.add(search.getUsername());
		resultList.add(((Integer)(search.getNumber()+1)).toString());
		
		result.setResult(resultList);
		
		return result;

		
	}
	
}
