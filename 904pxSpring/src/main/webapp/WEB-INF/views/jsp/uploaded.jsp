<%@include file="header.jsp" %>

<h1> you are in uploaded page</h1>

<c:forEach var="upload" items="${uploads}">
<div class="gallery">
    <img src="${upload.imageURL}" width="300" height="200">
    <h2>Title: "${upload.title}"</h2>
    <h2>Description: "${upload.description}"</h2>
    <h2>Category: "${upload.category}"</h2>
    <h2>Camera Model: "${upload.cameraModel}"</h2>
    <h2>Location</h2>
    <div>
		<h3>City: "${upload.location.city}</h3>
		<h3>Country: "${upload.location.country}</h3>
    </div>
    <div>
	    <h3>Image Characteristics</h3>
	    <h2>Focal Length: "${upload.imageCharacteristics.focalLength}"</h2>
	    <h2>F Number: "${upload.imageCharacteristics.fNumber}"</h2>
	    <h2>Exposure Time: "${upload.imageCharacteristics.exposureTime}"</h2>
	    <h2>Iso Speed Ratings: "${upload.imageCharacteristics.isoSpeedRatings}"</h2>
	    <h2>Date Taken: "${upload.imageCharacteristics.dateTaken}"</h2>
    </div>
    <div border="2px solid red">
    <h3>Comments:</h3>
    </div>
</div>
</c:forEach>

</body>
</html>