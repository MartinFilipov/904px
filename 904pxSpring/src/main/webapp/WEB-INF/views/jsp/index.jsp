
<%@include file="header.jsp" %>
<body>
<c:if test="${empty sessionScope.user_id}">
<h1>nqma lognat potrebitel</h1>
</c:if>
<c:if test="${!empty sessionScope.user_id}">
<h1>ima lognat potrebitel</h1>
</c:if>
<c:if test="${!empty registered}">
<h1>Successfully registered!</h1>
</c:if>

<!--  
<h1>${user_id}  user_id</h1>
<h1>${sessionScope} session scope</h1>
<h1>${session.user_id} session</h1>
-->

<!--
 button><a href="https://78.media.tumblr.com/da495430333543385b6301b2e1883a46/tumblr_mpe54zOcNi1qdlh1io1_250.gif">Log in</a></button>
<button><a href="https://78.media.tumblr.com/da495430333543385b6301b2e1883a46/tumblr_mpe54zOcNi1qdlh1io1_250.gif">Register</a></button>
-->
</body>
</html>