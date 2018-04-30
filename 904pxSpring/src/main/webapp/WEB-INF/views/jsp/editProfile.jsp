<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile Editor</title>
</head>
<body>
<form action="editor" method="post">
<input name='firstName' placeholder="Enter first name:"/> <br/>
<input name='lastName' placeholder="Enter last name:"/><br/>
<input name='profilePictureURL' placeholder="Enter profile picture URL:"/> <br/>
<input name='coverPhotoURL' placeholder="Enter cover photo URL:"/><br/>
 <!-- 
Select a profile picture: <br />
<input type = "file" name = "profilePicture" size = "50" /><br/>
Select a cover photo: <br />
<input type = "file" name = "coverPhoto" size = "50" /><br/><br/><br/>
-->
<button>Save changes</button>
</form>
</body>
</html>