<%@include file="header.jsp"%>
<form action="editor" method="post" enctype="multipart/form-data">
	<input name='firstName' placeholder="Enter first name:"/> <br/>
	<input name='lastName' placeholder="Enter last name:"/><br/>
	<h2>Choose profile picture</h2>
	<input type="file" name='profilePicture'/><br/>
	<h2>Choose cover photo</h2>
	<input type="file" name='coverPhoto'/><br/>
	<input type="submit" value="Save changes"/><br/>
</form>
</body>
</html>