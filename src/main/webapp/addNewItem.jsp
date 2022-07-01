<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert New Product</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="bootstrap.min.css">
<% String serviceId = (String) session.getAttribute("availableServiceId");
String pageStatus = (String) session.getAttribute("pageStatus");%>
<p id="availableServiceId" style="display: none;"></p>
<p id="pageStatus" style="display: none;"></p>
<script>
document.getElementById('availableServiceId').innerText = '<%=serviceId%>'
document.getElementById('pageStatus').innerText = '<%=pageStatus%>'
if(document.getElementById('pageStatus').innerText == "SUCCESS"){
	alert("Data inserted successfully");
}
else if(document.getElementById('pageStatus').innerText == "FAIL"){
	alert("Failed to insert data");
}
</script>
<%
session.setAttribute("availableServiceId","");
session.setAttribute("pageStatus","");
%>
</head>
<body style="background-color: #111827;">
	<div class="container col-md-1 mt-5"
		style="color: white; display: flex; justify-content: center;">
		<img src="assets/loginAssets/rCrown.jpg" style="border-radius: 60px;"
			height="100px" width="100px"></img>
	</div>
	<div class="container col-md-12 mt-5"
		style="padding: 10px; color: aliceblue; display: flex; justify-content: center;">
		<h1 style="font-family: fangsong;">The Radiance Beauty Parlour</h1>
	</div>
	<form method="get" action="/subscription/addNewProduct">
	<div class="container-fluid col-md-12">
		<div class="row" style="display: flex; justify-content: center;">
			<input class="col-md-1" id="prodId" name ="prodId" style="margin-right: 10px;"
				placeholder="Product ID"> <input class="col-md-2"
				id="prodName" name="prodName" style="margin-right: 10px;" placeholder="Product Name"> <input
				class="col-md-1" id="price" name="prodPrice" style="margin-right: 10px;" placeholder="Price">
			<input class="col-md-1" id="prodCode" name="prodCode" placeholder="Product short code">
		</div>
		<div class="row mt-3" style="display: flex; justify-content: center;">
			<button class="col-md-1 btn-primary">Insert Item</button>
		</div>
	</div>
	</form>
	<div class="container-fluid col-md-12">
			<div class="row mt-5" style="display: flex; justify-content: center;">
			<a href="/subscription/home.jsp" style="text-align: center;">
				<button class="col-md-1 btn-warning">Back</button>
				</a>
			</div>
		</div>
	<form method="post" action="/subscription/logout">
		<div class="container-fluid col-md-12">
			<div class="row mt-5" style="display: flex; justify-content: center;">
				<button class="col-md-1 btn-danger">Logout</button>
			</div>
		</div>
	</form>
	<footer>
		<div class="mt-5">
			<div class="row">
				<div class="col-md-1"></div>
				<div class="col-md-1" style="color: white;">
					<img src="assets/loginAssets/odin.png"
						style="height: 70px; width: 83px; margin-left: 30px;"></img>
				</div>
				<div class="col-md-3">
					<p style="display: inline; font-size: 50px;">&#124;</p>
					<p style="display: inline; color: white;">Odin Tech Solutions
						&#9400; 2021</p>
				</div>
			</div>
		</div>
	</footer>
	<script src="js/createService.js"></script>
</body>
</html>