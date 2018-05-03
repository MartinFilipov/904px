<%@include file="header.jsp"%>
<h1>Welcome to ${user.username}'s profile</h1>

<div align="center" style="border: 0px solid black">
	<a href="/904px/profile/${user.username}/follow">
	<button>Follow</button>
	</a>
	<c:if test="${user.firstName != ''}">
		<c:if test="${ user.lastName!= ''}">
			<h1>Name: ${user.firstName} ${user.lastName}</h1>
		</c:if>
	</c:if>

	<c:if test="${user.email != ''}">
		<h1>Email: ${user.email}</h1>
	</c:if>

	<c:if test="${user.profilePictureURL != ''}">
		<h6>Profile picture url:${user.profilePictureURL }</h6>
		<img src="${user.profilePictureURL }" width="350" float:right>
	</c:if>

	<c:if test="${user.coverPhotoURL != ''}">
		<h6>Cover photo url: ${user.coverPhotoURL }</h6>
		<img src="${user.coverPhotoURL }" width="350">
	</c:if>
	
</div>
	<c:forEach var="album" items="${albums}">
		<div class="gallery" title="Border" style="border: 1px dotted black;">
			<h2>${album.name}</h2>
				<a href="/904px/profile/album/${album.id}"> 
				<h2>${album.name} link</h2>
				</a>
		</div>
	</c:forEach>
</body>
</html>