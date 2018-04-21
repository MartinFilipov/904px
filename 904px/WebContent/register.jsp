<%@include file="header.jsp" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!--
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">

<head>
	<title>Register</title>
	<script type="application/x-javascript">
		addEventListener("load", function () {
			setTimeout(hideURLbar, 0);
		}, false);

		function hideURLbar() {
			window.scrollTo(0, 1);
		}
	</script>

	<link href="css/font-awesome.css" rel="stylesheet">
	<link href="css/style.css" rel='stylesheet' type='text/css' />
	<link href="//fonts.googleapis.com/css?family=Josefin+Sans:100,100i,300,300i,400,400i,600,600i,700,700i" rel="stylesheet">
	<link href="//fonts.googleapis.com/css?family=PT+Sans:400,400i,700,700i" rel="stylesheet">
</head>

<body>
    -->
<div class="whole-body">
	<div class="login-section-agileits">
		<h3 class="form-head">register online today, Its'free!</h3>
		<form action="#" method="post">
			<div class="w3ls-icon">
				<span class="fa fa-user" aria-hidden="true"></span>
				<input type="text" class="lock" name="name" placeholder="username" required="" />
			</div>
			<div class="w3ls-icon">
				<span class="fa fa-envelope" aria-hidden="true"></span>
				<input type="email" class="user" name="email" placeholder="Email" required="" />
			</div>
			<div class="w3ls-icon">
				<span class="fa fa-lock" aria-hidden="true"></span>
				<input type="password" class="lock" id="password1" name="password" placeholder="Password" required="" />
			</div>
			<div class="w3ls-icon">
				<span class="fa fa-lock" aria-hidden="true"></span>
				<input type="password" class="lock" id="password2" name="confirm password" placeholder="Confirm Password" required="" />
			</div>
			<input type="submit" value="register now">
		</form>
	</div>


</div>
	<script type="text/javascript">
		window.onload = function () {
			document.getElementById("password1").onchange = validatePassword;
			document.getElementById("password2").onchange = validatePassword;
		}

		function validatePassword() {
			var pass2 = document.getElementById("password2").value;
			var pass1 = document.getElementById("password1").value;
			if (pass1 != pass2)
				document.getElementById("password2").setCustomValidity("Passwords do not Match");
			else
				document.getElementById("password2").setCustomValidity('');
			//empty string means no validation error
		}
	</script>

</body>

</html>