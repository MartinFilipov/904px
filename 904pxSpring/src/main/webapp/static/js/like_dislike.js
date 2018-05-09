function increaseLikes() {
		var postId = $("#post_id").html();
		
		$.ajax({
	        url: '/904px/postDetails/'+postId+'/increaseLikes',
	        type: 'POST',
	        data: null,
	        success: function() { 
	        	var likes = $("#post_likes");
	        	likes.html(+likes.html()+1);
	        	$("#like_button").hide();
	        	$("#dislike_button").show();
	        }
	    });
		
	};
	
function decreaseLikes() {
	var postId = $("#post_id").html();
	
	$.ajax({
        url: '/904px/postDetails/'+postId+'/decreaseLikes',
        type: 'POST',
        data: null,
        success: function() { 
        	var likes = $("#post_likes");
        	likes.html(+likes.html()-1);
        	$("#dislike_button").hide();
        	$("#like_button").show();
        }
    });
	
};