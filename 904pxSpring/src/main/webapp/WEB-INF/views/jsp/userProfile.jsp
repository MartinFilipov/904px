<%@include file="header.jsp"%>
<h1>Welcome to ${user.username}'s profile</h1>

<div align="center" style="border: 0px solid black">


	<c:if test="${not empty sessionScope.user_id}">
		<c:if test="${empty followed}">
			<a href="/904px/profile/${user.username}/follow">
				<button>Follow</button>
			</a>
		</c:if>
		<c:if test="${not empty followed}">
			<a href="/904px/profile/${user.username}/unfollow">
				<button>Unfollow</button>
			</a>
		</c:if>
	</c:if>

	<c:if test="${user.firstName != ''}">
		<c:if test="${ user.lastName!= ''}">
			<h1>Name: ${user.firstName} ${user.lastName}</h1>
		</c:if>
	</c:if>

	<c:if test="${user.email != ''}">
		<h1>Email: ${user.email}</h1>
	</c:if>

	<c:if test="${user.profilePictureURL != ''}">
		<img style="display: block;" src="${user.profilePictureURL }" width="350" float:right>
	</c:if>

	<c:if test="${user.coverPhotoURL != ''}">
		<img style="display: block;" src="${user.coverPhotoURL }" width="350">
	</c:if>

</div>
<div class="col-md-4">
<c:forEach var="album" items="${albums}">
	<div class="gallery">
		<a href="/904px/profile/album/${album.id}">
			<h2>${album.name}</h2>
		</a>
	</div>
	
</c:forEach>
</div>
</body>
</html>