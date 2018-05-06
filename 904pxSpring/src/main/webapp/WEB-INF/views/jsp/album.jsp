<%@include file="header.jsp"%>
<h1>Album name: ${album.name}</h1>
<c:forEach var="post" items="${posts}">
	<div class="gallery">
		<a href="/904px/postDetails/${post.id}"> 
		<img src="/904px/download/${post.imageName}" width="300" height="200">
		</a>
	</div>
</c:forEach>
</body>
</html>