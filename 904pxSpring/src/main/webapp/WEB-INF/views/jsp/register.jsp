<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page session="false" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
	<link rel="stylesheet" type="text/css" href="/904px/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/904px/css/font-awesome.min.css" />
    <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    
	<link href="/904px/css/font-awesome.css" rel="stylesheet">
	<link href="/904px/css/style.css" rel='stylesheet' type='text/css' />
	<link href="//fonts.googleapis.com/css?family=Josefin+Sans:100,100i,300,300i,400,400i,600,600i,700,700i" rel="stylesheet">
	<link href="//fonts.googleapis.com/css?family=PT+Sans:400,400i,700,700i" rel="stylesheet">
</head>

<body>

<nav class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="/904px/"><b>904<sup>px</sup></b></a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
            <!-- <li class="active"><a href="#">Popular</a></li>-->
            <li><a href="#">Popular</a></li>
            <li><a href="/904px/fresh">Fresh</a></li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">More <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="#">About</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Contacts</a></li>
                </ul>
            </li>
        </ul>

        <div class="col-sm-3 col-md-3 pull-right">
        <ul class="nav navbar-nav">
        	<c:if test="${empty sessionScope.user_id}">
            <li><a href="/904px/login">Log in</a></li>
            <li><a href="/904px/register">Register</a></li>
            </c:if>
            <c:if test="${!empty sessionScope.user_id}">
            <li><a href="/904px/profile">Profile</a></li>
            <li><a href="/904px/logout">Log out</a></li>
            </c:if>
      	</ul>
            </div>
            <form action="/904px/search" method="post" class="navbar-form" role="search">
			<div class="input-group">
				<input type="text" class="form-control" placeholder="Search"
					name="q">
				<div class="input-group-btn">
					<button class="btn btn-default" type="submit">
						<i class="glyphicon glyphicon-search"></i>
					</button>
				</div>
				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1"></div>
		</form>
    </div>
</nav>

<div class="whole-body">
	<div class="login-section-agileits">
		<h3 class="form-head">register online today, Its'free!</h3>
		<form action="./register" method="post">
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