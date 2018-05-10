<%@include file="header.jsp"%>

<div class="gallery">
	<img src="/904px/download/${post.imageName}" width="300" height="200">

	<h2>Title: ${post.title}</h2>
	<h2>Description: ${post.description}</h2>
	<h2>Category: ${post.category}</h2>
	<h2>Camera Model: ${post.cameraModel}</h2>
	<c:if test="${post.nsfw}">
		<h2>Not Safe For Work</h2>
	</c:if>
	<h2>Location</h2>
	<div>
		<h3>City: ${post.location.city}</h3>
		<h3>Country: ${post.location.country}</h3>
	</div>
	<div>
		<h2>Likes: ${post.likes} Views: ${post.views}</h2>
		<h2>Date uploaded: ${post.dateUploaded}</h2>
	</div>
	<div>
		<h2>Image Characteristics</h2>
		<h3>Focal Length: ${post.imageCharacteristics.focalLength}</h3>
		<h3>F Number: ${post.imageCharacteristics.fNumber}</h3>
		<h3>Exposure Time: ${post.imageCharacteristics.exposureTime}</h3>
		<h3>Iso Speed Ratings:
			${post.imageCharacteristics.isoSpeedRatings}</h3>
		<h3>Date Taken: ${post.imageCharacteristics.dateTaken}</h3>
	</div>
	<h3>Comments:</h3>
	<c:forEach var="comment" items="${comments}">
		<div title="Border" style="border: 1px dotted black;">
			<a href="/904px/profile/${comment.username}">
				<h4>${comment.username}</h4>
			</a>
			<p>${comment.comment }</p>
			<h6>
				Likes:
				<p id="likes${comment.id}">${comment.likes}</p>
			</h6>
			<c:if test="${not empty sessionScope.user_id}">
				<p hidden id="commentID${comment.id}">${comment.id }</p>
				<c:choose>
					<c:when test="${not comment.isLikedByCurrentUser}">
						<button id="likeButton${comment.id}" onclick="like(${comment.id})">Like</button>
						<button hidden id="dislikeButton${comment.id}" onclick="dislike(${comment.id})">Dislike</button>
					</c:when>
					<c:otherwise>
						<button hidden id="likeButton${comment.id}" onclick="like(${comment.id})">Like</button>
						<button id="dislikeButton${comment.id}" onclick="dislike(${comment.id})">Dislike</button>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</c:forEach>
	<div id="newComments"></div>
	
	<p hidden id="postID">${post.id }</p>

	<c:if test="${not empty sessionScope.user_id}">

		<input id="text" name='text' placeholder="Enter comment:" />
		<button id="addComment">Add comment</button>
	</c:if>
</div>
<c:if test="${not empty sessionScope.user_id}">
	<div>
		<h3>Add to album</h3>
		<ul>
			<c:forEach var="album" items="${albums}">
				<a href="/904px/profile/album/${album.id}/add/${post.id}">
					<li>${album.name}</li>
				</a>
			</c:forEach>
		</ul>
	</div>
</c:if>
</body>


<script src="/904px/js/likeDislike.js"></script>

<script>

	function like(varId){
		console.log(varId);
		var postID = $("#postID").html();
		
		var commentID = $("#commentID"+varId).html();
		var likes = $("#likes"+varId).html();
		$("#likes"+varId).html(+likes + 1);
		$("#likeButton"+varId).hide();
		$("#dislikeButton"+varId).show();
		$.ajax({
			url : '/904px/postDetails/' + postID + '/' + varId + '/like',
			type : 'POST',
			data : null
		});
	}
	function dislike(varId){
		console.log(varId);
		var postID = $("#postID").html();
		var commentID = $("#commentID"+varId).html();
		var likes = $("#likes"+varId).html();
		$("#likes"+varId).html(+likes - 1);
		$("#likeButton"+varId).show();
		$("#dislikeButton"+varId).hide();
		$.ajax({
			url : '/904px/postDetails/' + postID + '/' + varId + '/dislike',
			type : 'POST',
			data : null
		});
	}

	$("#addComment").click(function() {
		var postID = $("#postID").html();
		var comment = $("#text").val();
		console.log(comment);
		$.ajax({
			url : '/904px/postDetails/' + postID + '/addComment/' + comment,
			type : 'POST',
			data : null,
			success : function(data) {
				console.log(data.result.isLikedByCurrentUser);
				display(data);
			}
		});
	})
	
	function display(data) {
		var id=data.result.id;
		var comment=data.result.comment;
		var isLikedByCurrentUser=data.result.isLikedByCurrentUser;
		var likes= data.result.likes;
		var username= data.result.username;
		var json = "<h4>Ajax Response</h4><pre>"
				+"<h3>"+username+"</h3>"
				+ JSON.stringify(data, null, 4) + "</pre>";
		
		var json1='<div title="Border" style="border: 1px dotted black;">'+
			'<a href="/904px/profile/'+username+'">'+
			'<h4>'+username+'</h4></a>'+'<p>'+comment+'</p><h6>Likes:'+
			'<p id="likes'+id+'">'+likes+'</p></h6>'+
			'<c:if test="${not empty sessionScope.user_id}">'+
			'<p hidden id="commentID'+id+'">'+id+'</p>'+
			'</c:if><button id="likeButton'+id+'" onclick="like('+id+')">Like</button>'+
			'<button hidden id="dislikeButton'+id+'" onclick="dislike('+id+')">Dislike</button></div>';
		var newComments=$('#newComments').html();
		$('#newComments').html(newComments+json1);
	}
	
</script>

</html>