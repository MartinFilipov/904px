<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile Editor</title>
</head>
<body>
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