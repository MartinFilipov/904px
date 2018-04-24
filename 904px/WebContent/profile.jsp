<%@include file="header.jsp" %>
<div align="center" style="border:0px solid black">
<%if(request.getAttribute("firstName")!=""){ %>
<h1>Welcome <%=request.getAttribute("firstName")%></h1>
<%}
if(request.getAttribute("firstName")!="" && request.getAttribute("lastName")!=""){ %>
<h1>Name: <%=request.getAttribute("firstName")%> <%=request.getAttribute("lastName") %> </h1>
<%}
if(request.getAttribute("email")!=""){ %>
<h1>Email: <%=request.getAttribute("email")%></h1>
<%}
if(request.getAttribute("profilePictureURL")!=""){ %>
<h6>Profile picture url: <%=request.getAttribute("profilePictureURL")%></h6>
<img src="<%=request.getAttribute("profilePictureURL") %>" width="500" height="250" float:right >
<%}
if(request.getAttribute("coverPhotoURL")!=""){ %>
<h6>Cover photo url:  <%=request.getAttribute("coverPhotoURL")%></h6>
<img src="<%=request.getAttribute("coverPhotoURL")%>" width="500" height="250" >
<%} %>
<h1>Affection: <%=request.getAttribute("affection")%></h1>
<h1>Photo views: <%=request.getAttribute("photoViews")%></h1>
<a href="https://78.media.tumblr.com/da495430333543385b6301b2e1883a46/tumblr_mpe54zOcNi1qdlh1io1_250.gif">
<button>Gratz</button>
</a>
<a href="./editProfile.jsp"><button>Edit profile</button></a>
</div>
</body>
</html>