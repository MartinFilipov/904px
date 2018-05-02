<%@include file="header.jsp" %>

<form action=./upload method="post">
	<input name='imageURL' placeholder="Enter image URL:"/><br/>
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

	<button>Upload</button>
</form>
</body>
</html>