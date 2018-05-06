<%@include file="header.jsp" %>

<c:if test="${empty filename}">
	<form action="/904px/uploadImage" method="post" enctype="multipart/form-data">
		<input type="file" name='filename'/><br/>
		<input type="submit" value="Next"/><br/>
	</form>
</c:if>

<c:if test="${not empty filename}"> 
<img src="/904px/download/${filename}" height="300px">
<form action="/904px/upload/${filename}" method="post">
	<input name='title' placeholder="Title:"/> <br/>
	<input name='description' placeholder="Description"/><br/>
	
	<div>
	<h4>Category</h4>
		<select name="category">
			<c:forEach items="${categories}" var="category">
				<option> ${category} </option>
			</c:forEach>	
		</select>
	</div>

	<div>
		<h4>Location</h4>
		<input name='city' placeholder="City:"/> <br/>
		<input name='country' placeholder="Country:"/> <br/>
	</div>
	<input type="checkbox" name="nsfw" value="nsfw"/> NSFW <br/>

	<button> Upload </button>
</form>

</c:if>
</body>
</html>