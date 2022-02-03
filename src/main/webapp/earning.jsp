<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="bootstrap.min.css">
<%
String dateList = (String) session.getAttribute("dateList");
String onlineEarning = (String) session.getAttribute("onlineEarning");
String offlineEarning = (String) session.getAttribute("offlineEarning");
String totalEarning = (String) session.getAttribute("totalEarning");
String expense = (String) session.getAttribute("expense");
String profit = (String) session.getAttribute("profit");
%>
<style>
table, td, th {
  border: 1px solid black;
  padding-top: 10px;
  padding-bottom: 10px;
  padding-left: 25px;
  padding-right: 25px;
  text-align: center;
}

table {
  width: 100%;
  border-collapse: collapse;
}

</style>
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
	<div class="container-fluid col-md-5 mt-4"
		style="display: flex; justify-content: center; background-color: white;">
		<div class="row" id="displayEarning">
			<table id="summary">
				<tr>
					<th>Date</th>
					<th>Online Earning</th>
					<th>Offline Earning</th>
					<th>Total Earning</th>
					<th>Expense</th>
					<th>Profit</th>
				</tr>
			</table>
		</div>
	</div>
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
	<p id="dateList" style="display: none;"></p>
	<p id="onlineEarning" style="display: none;"></p>
	<p id="offlineEarning" style="display: none;"></p>
	<p id="totalEarning" style="display: none;"></p>
	<p id="expense" style="display: none;"></p>
	<p id="profit" style="display: none;"></p>
	<script>
	document.getElementById('dateList').innerText = '<%= dateList%>';
	document.getElementById('onlineEarning').innerText = '<%= onlineEarning%>';
	document.getElementById('offlineEarning').innerText = '<%= offlineEarning%>';
	document.getElementById('totalEarning').innerText = '<%= totalEarning%>';
	document.getElementById('expense').innerText = '<%= expense%>';
	document.getElementById('profit').innerText = '<%= profit%>';
	</script>
	<script src="js/earning.js"></script>
</body>
</html>