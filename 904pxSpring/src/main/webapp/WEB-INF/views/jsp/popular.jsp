<%@include file="header.jsp"%>

<c:forEach var="post" items="${popular}">
<div class="gallery">
    <a href="/904px/postDetails/${post.id}">
    <img src="/904px/download/${post.imageName}" width="300" height="200">
    </a>
</div>
</c:forEach>

</body>
</html>