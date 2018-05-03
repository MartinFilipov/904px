<%@include file="header.jsp"%>

<div class="gallery">
	<img src="${post.imageURL}" width="300" height="200">

	<h2>Title: ${post.title}</h2>
	<h2>Description: ${post.description}</h2>
	<h2>Category: ${post.category}</h2>
	<h2>Camera Model: ${post.cameraModel}</h2>
	<h2>Location</h2>
	<div>
		<h3>City: ${post.location.city}</h3>
		<h3>Country: ${post.location.country}</h3>
	</div>
	<div>
		<h3>Image Characteristics</h3>
		<h2>Focal Length: ${post.imageCharacteristics.focalLength}</h2>
		<h2>F Number: ${post.imageCharacteristics.fNumber}</h2>
		<h2>Exposure Time: ${post.imageCharacteristics.exposureTime}</h2>
		<h2>Iso Speed Ratings: ${post.imageCharacteristics.isoSpeedRatings}</h2>
		<h2>Date Taken: ${post.imageCharacteristics.dateTaken}</h2>
	</div>
</div>

</body>
</html>