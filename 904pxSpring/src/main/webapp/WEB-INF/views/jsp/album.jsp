<%@include file="header.jsp"%>
<h1>Album name: ${album.name}</h1>
<c:forEach var="post" items="${posts}">
	<div class="gallery">
		<a href="/904px/postDetails/${post.id}"> <img
			src="/904px/download/${post.imageName}" width="300" height="200">
		</a>
		<c:if test="${not empty sessionScope.user_id}">
			<c:if test="${sessionScope.user_id==albumCreatorID}">
				<a href="/904px/profile/album/${album.id}/remove/${post.id}">
					<button class="btn btn-primary btn-lg">remove</button>
				</a>
			</c:if>
		</c:if>
	</div>
</c:forEach>
</body>
</html>