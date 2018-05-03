<%@include file="header.jsp"%>

<div class="gallery">
	<img src="${post.imageURL}" width="300" height="200">

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
			<h4>${comment.username}</h4>
			<p>${comment.comment }</p>
			<p>Likes:${comment.likes}</p>
			<a href="/904px/postDetails/${post.id}/${comment.id}">
				<button>Like</button>
			</a>
		</div>
	</c:forEach>
	<form action="/904px/postDetails/${post.id}/addComment" method="post">
		<input name='text' placeholder="Enter comment:" />
		<button>Add comment</button>
	</form>
</div>
<div>
	<h3>Add to album</h3>
	<ul>
		<c:forEach var="album" items="${albums}">
			<a href="/904px/profile/album/${album.id}">
				<li>${album.name}</li>
			</a>
		</c:forEach>
	</ul>
</div>
</body>
</html>