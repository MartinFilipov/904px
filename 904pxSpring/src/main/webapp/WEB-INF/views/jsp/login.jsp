<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
<head>

<title>Login</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link rel="stylesheet" type="text/css"
	href="/904px/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="/904px/css/font-awesome.min.css" />
<script type="text/javascript" src="/904px/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/904px/js/bootstrap.min.js"></script>

<link rel="icon" type="image/png" href="icons/favicon.ico" />
<link rel="stylesheet" type="text/css"
	href="/904px/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/904px/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css"
	href="/904px/fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
<link rel="stylesheet" type="text/css"
	href="/904px/vendor/animate/animate.css">
<link rel="stylesheet" type="text/css"
	href="/904px/vendor/css-hamburgers/hamburgers.min.css">
<link rel="stylesheet" type="text/css"
	href="/904px/vendor/animsition/css/animsition.min.css">
<link rel="stylesheet" type="text/css"
	href="/904px/vendor/select2/select2.min.css">
<link rel="stylesheet" type="text/css"
	href="/904px/vendor/daterangepicker/daterangepicker.css">
<link rel="stylesheet" type="text/css" href="/904px/css/util.css">
<link rel="stylesheet" type="text/css" href="/904px/css/main.css">
</head>
<body>

	<div class="container"></div>

	<nav class="navbar navbar-default" role="navigation">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target="#bs-example-navbar-collapse-1">
			<span class="icon-bar"></span> <span class="icon-bar"></span> <span
				class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="/904px/"><b>904<sup>px</sup></b></a>
	</div>
	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		<ul class="nav navbar-nav">
			<!-- <li class="active"><a href="#">Popular</a></li>-->
			<li><a href="/904px/popular">Popular</a></li>
			<li><a href="/904px/fresh">Fresh</a></li>
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown">More <b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="#">About</a></li>
					<li class="divider"></li>
					<li><a href="#">Contacts</a></li>
				</ul></li>
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
		<form action="/904px/search" method="post" class="navbar-form"
			role="search">
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
	</div>
	<div class="limiter">
		<form action="/904px/login" method="post">
			<div class="container-login100"
				style="background-image: url('images/bg-01.jpg');">
				<div class="wrap-login100 p-l-110 p-r-110 p-t-62 p-b-33">
					<form class="login100-form validate-form flex-sb flex-w">
						<span class="login100-form-title p-b-53"> Sign In</span>
						<!--   <a
							href="#" class="btn-face m-b-20"> Facebook </a> <a href="#"
							class="btn-google m-b-20">
							Google
						</a>
						-->
						<div class="p-t-31 p-b-9">
							<span class="txt1"> Username </span>
						</div>
						<div class="wrap-input100 validate-input"
							data-validate="Username is required">
							<input class="input100" type="text" name="username"> <span
								class="focus-input100"></span>
						</div>

						<div class="p-t-13 p-b-9">
							<span class="txt1"> Password </span> <a href="#"
								class="txt2 bo1 m-l-5"> Forgot? </a>
						</div>
						<div class="wrap-input100 validate-input"
							data-validate="Password is required">
							<input class="input100" type="password" name="pass"> <span
								class="focus-input100"></span>
						</div>

						<div class="container-login100-form-btn m-t-17">
							<button class="login100-form-btn">Sign In</button>
						</div>
					</form>
					<div class="w-full text-center p-t-55">
						<span class="txt2"> Not a member? </span> <a
							href="/904px/register" class="txt2 bo1"> Sign up now </a>
					</div>
		</form>
	</div>

	<div id="dropDownSelect1"></div>

	<script src="/904px/vendor/jquery/jquery-3.2.1.min.js"></script>
	<script src="/904px/vendor/animsition/js/animsition.min.js"></script>
	<script src="/904px/vendor/bootstrap/js/popper.js"></script>
	<script src="/904px/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script src="/904px/vendor/select2/select2.min.js"></script>
	<script src="/904px/vendor/daterangepicker/moment.min.js"></script>
	<script src="/904px/vendor/daterangepicker/daterangepicker.js"></script>
	<script src="/904px/vendor/countdowntime/countdowntime.js"></script>
	<script src="/904px/js/main.js"></script>
	<!--  FACEBOOK LOGIN ATTEMPT -->
	
</body>
</html>