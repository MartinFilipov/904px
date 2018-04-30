<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="css/font-awesome.min.css" />
    <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">

</div>

<nav class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="./"><b>904<sup>px</sup></b></a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
            <!-- <li class="active"><a href="#">Popular</a></li>-->
            <li><a href="#">Popular</a></li>
            <li><a href="#">Fresh</a></li>
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
        	<%if(request.getSession(false)==null || request.getSession(false).getAttribute("user_id")==null){ %>
            <li><a href="login">Log in</a></li>
            <li><a href="register">Register</a></li>
            <%}else{ %>
            <li><a href="profile">Profile</a></li>
            <li><a href="logout">Log out</a></li>
            <%} %>
      	</ul>
            </div>
            <form class="navbar-form" role="search">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="Search" name="q">
                    <div class="input-group-btn">
                        <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                    </div>
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                </div>
            </form>
    </div>
</nav>
</div>