<%@include file="header.jsp"%>

<c:forEach var="post" items="${posts}">
	<div class="gallery" style="height:350px; border:0px;" title="Border" style="border: 1px dotted black;">
		<a href="postDetails/${post.id}"> <img src="${post.imageURL}"
			width="300" height="200">
		</a>
	</div>
</c:forEach>
</body>
</html>