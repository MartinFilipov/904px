<%@include file="header.jsp" %>

<h1> you are in uploaded page</h1>

<c:forEach var="upload" items="${uploads}">
    <a href="/904px/postDetails/${upload.id}">
    <img src="/904px/download/${upload.imageName}" width="350" style="margin: 20px; padding-left: 50px">
    </a>
</c:forEach>

</body>
</html>