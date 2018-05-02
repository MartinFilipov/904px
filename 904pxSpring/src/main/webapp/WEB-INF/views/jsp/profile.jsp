<%@include file="header.jsp" %>
<a href="./upload">
<button id="upload" type="button" class="btn btn-secondary">Upload</button>
</a>

<a href="./uploaded">
<button id="uploaded" type="button" class="btn btn-secondary">Uploaded</button>
</a>

<script src="./js/upload.js"></script>
<div align="center" style="border:0px solid black">
<c:if test = "${firstName != ''}">
<h1>Welcome ${firstName }</h1>
</c:if>

<c:if test = "${firstName != ''}">
<c:if test = "${ lastName!= ''}">
<h1>Name: ${firstName} ${lastName}</h1>
</c:if>
</c:if>

<c:if test = "${email != ''}">
<h1>Email: ${email}</h1>
</c:if>

<c:if test = "${profilePictureURL != ''}">
<h6>Profile picture url:${profilePictureURL } </h6>
<img src="${profilePictureURL }" width="350" float:right >
</c:if>

<c:if test = "${coverPhotoURL != ''}">
<h6>Cover photo url: ${coverPhotoURL }</h6>
<img src="${coverPhotoURL }" width="350">
</c:if>

<h1>Affection: ${affection }</h1>
<h1>Photo views: ${photoViews }</h1>
<a href="https://78.media.tumblr.com/da495430333543385b6301b2e1883a46/tumblr_mpe54zOcNi1qdlh1io1_250.gif">
<button>Gratz</button>
</a>
<a href="editProfile"><button>Edit profile</button></a>
</div>
</body>
</html>