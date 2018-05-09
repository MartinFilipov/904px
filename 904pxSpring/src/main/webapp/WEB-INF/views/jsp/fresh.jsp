<%@include file="header.jsp"%>

<c:forEach var="post" items="${posts}">

	<a href="/904px/postDetails/${post.id}"> <img
		src="/904px/download/${post.imageName}" width="350"
		style="margin: 20px; padding-left: 50px">
	</a>

</c:forEach>
</body>
</html>