<%@include file="header.jsp"%>
<div class="row" style="padding-left: 40px;">

	<!-- Post Content Column -->
	<div class="col-lg-8">

		<!-- Title -->
		<h1 class="mt-4">Post Title: ${post.title}</h1>
		<p class="lead">
			by <a href="/904px/profile/${username}">${username}</a>
		</p>
		<hr>

		<p>Posted on ${post.dateUploaded}</p>

		<hr>

		<img class="img-fluid rounded" src="/904px/download/${post.imageName}"
			width="500" alt="">

		<hr>

		<c:if test="${not empty sessionScope.user_id}">
			<div class="card my-4">
				<h5 class="card-header">Leave a Comment:</h5>
				<div class="card-body">
					<div class="form-group">
						<input id="text" name='text' style="height: 50px; width: 400px;"
							placeholder="Enter comment:" />
					</div>
					<button id="addComment" class="btn btn-primary">Submit</button>
				</div>
			</div>
		</c:if>


		<div id="newComments"></div>
		<c:forEach var="comment" items="${comments}">
			<div class="media mb-4">
				<div class="media-body">
					<h4>
						<a href="/904px/profile/${comment.username}">${comment.username}</a>
					</h4>

					<p>${comment.comment}</p>

					Likes:
					<div id="likes${comment.id}">${comment.likes}</div>
					<c:if test="${not empty sessionScope.user_id}">
						<p hidden id="commentID${comment.id}">${comment.id }</p>
						<c:choose>
							<c:when test="${not comment.isLikedByCurrentUser}">
								<button id="likeButton${comment.id}"
									onclick="like(${comment.id})">Like</button>
								<button hidden id="dislikeButton${comment.id}"
									onclick="dislike(${comment.id})">Dislike</button>
							</c:when>
							<c:otherwise>
								<button hidden id="likeButton${comment.id}"
									onclick="like(${comment.id})">Like</button>
								<button id="dislikeButton${comment.id}"
									onclick="dislike(${comment.id})">Dislike</button>
							</c:otherwise>
						</c:choose>
					</c:if>
				</div>

			</div>
		</c:forEach>
		<p hidden id="postID">${post.id }</p>
	</div>
	<c:if test="${not empty sessionScope.user_id}">
		<div hidden id="post_id">${post.id}</div>
		<div hidden id="user_id">${sessionScope.user_id}</div>
	</c:if>

	<div class="col-md-4">
		<div class="card my-4">
			<p>
				<b><i>Likes:
						<div id="post_likes">${post.likes}</div> Views: ${post.views}
						Rating: ${post.rating}
				</i></b>
			</p>
			<c:if test="${not empty sessionScope.user_id}">
				<c:if test="${liked}">
					<button id="dislike_button" onclick="decreaseLikes()">
						Dislike</button>
					<button hidden id="like_button" onclick="increaseLikes()">
						Like</button>
				</c:if>
				<c:if test="${not liked}">
					<button hidden id="dislike_button" onclick="decreaseLikes()">
						Dislike</button>
					<button id="like_button" onclick="increaseLikes()">Like</button>
				</c:if>
			</c:if>
			<p class="lead">
				<c:if test='${post.description != "" }'>
					<p>
						<b>Description: ${post.description}</b>
					</p>
				</c:if>
				<c:if test='${post.category != "" }'>
					<p>
						<b>Category: ${post.category}</b>
					</p>
				</c:if>
				<c:if test='${post.cameraModel != "" }'>
					<p>
						<b>Camera Model: ${post.cameraModel}</b>
					</p>
				</c:if>
				<c:if test="${post.nsfw}">
					<b>Not Safe For Work</b>
				</c:if>
			<p>
				<c:if test='${post.location.city != "" }'>
				Location: ${post.location.city}, ${post.location.country}
				</c:if>
			</p>


			<blockquote class="blockquote">
				<c:if
					test='${(post.imageCharacteristics.focalLength != "") 
				|| (post.imageCharacteristics.fNumber != "")
				|| (post.imageCharacteristics.exposureTime != "")
				|| (post.imageCharacteristics.isoSpeedRatings != "")
				|| (post.imageCharacteristics.dateTaken != "")}'>
					<p class="mb-0">Image Characteristics:</p>
				</c:if>
				<c:if test='${post.imageCharacteristics.focalLength != "" }'>
					<p>
						<b>Focal Length: ${post.imageCharacteristics.focalLength}</b>
					</p>
				</c:if>
				<c:if test='${post.imageCharacteristics.fNumber != "" }'>
					<p>
						<b>F Number: ${post.imageCharacteristics.fNumber}</b>
					</p>
				</c:if>
				<c:if test='${post.imageCharacteristics.exposureTime != "" }'>
					<p>
						<b>Exposure Time: ${post.imageCharacteristics.exposureTime}</b>
					</p>
				</c:if>
				<c:if test='${post.imageCharacteristics.isoSpeedRatings != "" }'>
					<p>
						<b> Iso Speed Ratings:
							${post.imageCharacteristics.isoSpeedRatings}</b>
					</p>
				</c:if>
				<c:if test='${post.imageCharacteristics.dateTaken != "" }'>
					<b>Date Taken: ${post.imageCharacteristics.dateTaken}</b>
				</c:if>
			</blockquote>
			<c:if test="${not empty sessionScope.user_id}">
				<blockquote class="blockquote">
					<h4>Add to album</h4>
					<ul>
						<c:forEach var="album" items="${albums}">
							<a href="/904px/profile/album/${album.id}/add/${post.id}">
								<li>${album.name}</li>
							</a>
						</c:forEach>
					</ul>
				</blockquote>
			</c:if>
		</div>

	</div>
</div>

<script src="/904px/jquery/jquery.min.js"></script>
<script src="/904px/js/bootstrap.bundle.min.js"></script>
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
	$("#text").val('');
	if(comment == ""){
		alert("Cannot add empty comments");
		return;
	}
	console.log(comment);
	$.ajax({
		url : '/904px/postDetails/' + postID + '/addComment/' + comment,
		type : 'POST',
		data : null,
		success : function(data) {
			console.log(data.result.isLikedByCurrentUser);
			display(data);
		},
		error: function(data){
			alert(data.msg);
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
<script src="/904px/js/like_dislike.js"></script>

</body>
</html>