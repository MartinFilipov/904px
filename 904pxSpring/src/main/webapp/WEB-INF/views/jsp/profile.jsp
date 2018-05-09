<%@include file="header.jsp"%>
<a href="/904px/upload">
	<button id="upload" type="button" class="btn btn-secondary">Upload</button>
</a>

<a href="/904px/uploaded">
	<button id="uploaded" type="button" class="btn btn-secondary">Uploaded</button>
</a>

<script src="./js/upload.js"></script>
<div align="center" style="border: 0px solid black">
	<c:if test="${firstName != ''}">
		<h1>Welcome ${firstName }</h1>
	</c:if>

	<c:if test="${firstName != ''}">
		<c:if test="${ lastName!= ''}">
			<h1>Name: ${firstName} ${lastName}</h1>
		</c:if>
	</c:if>

	<c:if test="${email != ''}">
		<h1>Email: ${email}</h1>
	</c:if>

	<c:if test="${profilePictureName != ''}">
		<img src="/904px/download/${profilePictureName}" width="350" float:right>
	</c:if>

	<c:if test="${coverPhotoName != ''}">
		<img src="/904px/download/${coverPhotoName}" width="350">
	</c:if>

	<h1>Affection: ${affection }</h1>
	<h1>Photo views: ${photoViews }</h1>
	<div class="gallery" style="border: 0.5px dotted black">
		<c:forEach var="album" items="${albums}">
			<div class="gallery" title="Border" style="border: 1px dotted black;">
				<h2>${album.name}</h2>
				<a href="/904px/profile/album/${album.id}">
					<h2>${album.name}</h2>
				</a>
			</div>
		</c:forEach>
	</div>
	<div style="float: left;">
		<form action="/904px/addAlbum" method="post">
			<input name='albumName' placeholder="Enter album name:" />
			<button>Add new album</button>
		</form>
	</div>
	<a
		href="https://78.media.tumblr.com/da495430333543385b6301b2e1883a46/tumblr_mpe54zOcNi1qdlh1io1_250.gif">
		<button>Gratz</button>
	</a> <a href="/904px/editProfile"><button>Edit profile</button></a>

	<div class="gallery" title="Border" style="border: 1px dotted black;">
		<c:forEach var="followedName" items="${followed}">
			<a href="/904px/profile/${followedName}">
			<h3>${followedName }</h3>
			</a>
		</c:forEach>
	</div>

</div>
</body>
</html>