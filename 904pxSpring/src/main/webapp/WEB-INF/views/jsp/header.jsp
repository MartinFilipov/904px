<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%//@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" type="text/css" href="/904px/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/904px/css/font-awesome.min.css" />

<!-- 
<link href="/904px/css/blog-post.css" rel="stylesheet">
<link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
 -->
<script type="text/javascript" src="/904px/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/904px/js/bootstrap.min.js"></script>

<style>
div.gallery {
	margin: 5px;
	border: 1px solid #ccc;
	float: left;
	width: 360px;
}
div.gallery:hover {
	border: 1px solid #777;
}

div.gallery img {
	width: 100%;
	height: auto;
}

div.desc {
	padding: 15px;
	text-align: center;
}
</style>

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
			<li><a href="#">Popular</a></li>
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
	</div>