<%@include file="header.jsp" %>

<h1> you are in uploaded page</h1>

<c:forEach var="upload" items="${uploads}">
<div class="gallery">
    <a href="postDetails/${upload.id}">
    <img src="${upload.imageURL}" width="300" height="200">
    </a>

</div>
</c:forEach>

</body>
</html>