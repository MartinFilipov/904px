
<%@include file="header.jsp"%>
<body>
	<c:if test="${!empty registered}">
		<h1>Successfully registered!</h1>
	</c:if>

	<c:if test="${empty searchRequest}">
		<c:forEach var="post" items="${posts}">
			<a href="/904px/postDetails/${post.id}"> <img
				src="/904px/download/${post.imageName}" width="400"
				style="margin: 20px; padding-left: 50px">
			</a>
		</c:forEach>
	</c:if>
	<c:if test="${not empty searchRequest}">
		<c:if test="${empty searchResults}">
			<h1>Nothing came up on your search.</h1>
		</c:if>
		<c:forEach var="post" items="${searchResults}">
				<a href="/904px/postDetails/${post.id}"> <img
					src="/904px/download/${post.imageName}" width="350" style="margin: 20px; padding-left: 50px">
				</a>
		</c:forEach>
	</c:if>
	<!-- 		
	<div id="feedback1">
		<h4 id="number">5</h4>
	</div>
<h4 id="number">5</h4>  
		<input type=text class="form-control" id="username">
			<c:if test="${!empty liked}">
	</c:if>
	<c:if test="${empty liked}">
	</c:if>


		<form class="form-horizontal" id="search-form">
			<button type="submit" id="bth-search" class="btn btn-primary btn-lg">dislike</button>
		</form>
		<form class="form-horizontal" id="search-form">
			<button type="submit" id="bth-search" class="btn btn-primary btn-lg">like</button>
		</form>
	<script>
		jQuery(document).ready(function($) {

			$("#search-form").submit(function(event) {

				// Disble the search button
				enableSearchButton(false);

				// Prevent the form from submitting via the browser.
				event.preventDefault();

				searchViaAjax();

			});

		});

		function searchViaAjax() {
			console.log($("#number").html())
			var search = {}
			search["username"] = $("#username").val();
			search["number"] = $("#number").html();
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${home}search/api/getSearchResult",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					display(data);
				},
				error : function(e) {
					console.log("ERROR: ", e);
					display(e);
				},
				done : function(e) {
					console.log("DONE");
					enableSearchButton(true);
				}
			});

		}

		function enableSearchButton(flag) {
			$("#btn-search").prop("disabled", flag);
		}

		function display(data) {
			//	console.log(data.result[1])
			//		var json = "<h4>Ajax Response</h4>"
			//			+ "<h4>"+JSON.stringify(data)+"</h4>";
			//		console.log(json)

			var json = "<h4 id='number'>" + data.result[1] + "</h4>";
			$('#feedback1').html(json);
		}
	</script>

<h1>${user_id}  user_id</h1>
<h1>${sessionScope} session scope</h1>
<h1>${session.user_id} session</h1>



 button><a href="https://78.media.tumblr.com/da495430333543385b6301b2e1883a46/tumblr_mpe54zOcNi1qdlh1io1_250.gif">Log in</a></button>
<button><a href="https://78.media.tumblr.com/da495430333543385b6301b2e1883a46/tumblr_mpe54zOcNi1qdlh1io1_250.gif">Register</a></button>
-->
</body>
</html>