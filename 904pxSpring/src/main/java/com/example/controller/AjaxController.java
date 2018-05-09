package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.ajax.AjaxResponseBody;
import com.project.model.ajax.CommentText;
import com.project.model.post.Comment;
import com.project.model.post.PostDAO;
import com.project.model.post.PostException;

@RestController
public class AjaxController {

	// @ResponseBody
	// @RequestMapping(value = "/search/api/getSearchResult")
	// public AjaxResponseBody getSearchResultViaAjax(@RequestBody CommentText
	// search) {
	//
	// AjaxResponseBody result = new AjaxResponseBody();
	//
	// result.setMsg("success");
	// result.setCode("200");
	// List<String> resultList=new ArrayList<String>();
	// resultList.add(search.getUsername());
	// resultList.add(((Integer)(search.getNumber()+1)).toString());
	//
	// result.setResult(resultList);
	//
	// return result;
	// }

	@ResponseBody
	@RequestMapping(value = "/postDetails/{postId}/{commentId}/like", method = RequestMethod.POST)
	public void likeComment(HttpServletRequest request, @PathVariable(value = "postId") Integer postId,
			@PathVariable(value = "commentId") Integer commentId) {

		int userID = (int) request.getSession(false).getAttribute("user_id");
		PostDAO.getInstance().increaseLikesOfComment(commentId, userID);

	}

	@ResponseBody
	@RequestMapping(value = "/postDetails/{postId}/{commentId}/dislike", method = RequestMethod.POST)
	public void dislikeComment(HttpServletRequest request, @PathVariable(value = "postId") Integer postId,
			@PathVariable(value = "commentId") Integer commentId) {

		int userID = (int) request.getSession(false).getAttribute("user_id");
		PostDAO.getInstance().decreaseLikesOfComment(commentId, userID);

	}

	@ResponseBody
	@RequestMapping(value = "/postDetails/{postId}/addComment/{comment}", method = RequestMethod.POST)
	public AjaxResponseBody addComment(HttpServletRequest request, @PathVariable(value = "postId") Integer postId,
			@PathVariable(value = "comment") String comment) {

		AjaxResponseBody result = new AjaxResponseBody();
		if (comment.trim().length() == 0) {
			result.setMsg("Cannot post empty comments");
			result.setCode("400");
			result.setResult(null);
		} else {

			System.out.println("\n\n\n Ajax controller \n\n");
			int user_id = (int) request.getSession(false).getAttribute("user_id");
			PostDAO dao = PostDAO.getInstance();
			dao.addComment(user_id, postId, comment);
			try {
				Comment lastComment = dao.getLastAddedCommentByPostID(postId);
				result.setResult(lastComment);
				result.setMsg("");
				result.setCode("200");
			} catch (PostException e) {
				e.printStackTrace();
				result.setCode("400");
				result.setMsg("Couldn't add comment");
			}
		}
		return result;
	}
}
