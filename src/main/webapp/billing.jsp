<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="bootstrap.min.css">
<link rel="stylesheet" href="style.css">
<%
String customer_name = (String) session.getAttribute("customer_name");
String customer_phone = (String) session.getAttribute("customer_phone");
String customer_id = (String) session.getAttribute("customer_id");
%>
<p id="customer_name" style="display: none;"></p>
<p id="customer_phone" style="display: none;"></p>
<p id="customer_id" style="display: none;"></p>
<script>
	document.getElementById('customer_name').innerText = '<%=customer_name%>';
	document.getElementById('customer_phone').innerText = '<%=customer_phone%>';
	document.getElementById('customer_id').innerText = '<%=customer_id%>';
	let customer_phone = document.getElementById('customer_phone').innerText;
	if(customer_phone.localeCompare('INV')== 0 ){
		alert("Mobile number does not contains 10 digits");
	}
</script>
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
	<form method="post" action="/subscription/checkCustomer">
		<div id="customerCheck" class="container-fluid col-md-12 mt-5"
			style="color: white;">
			<div class="row" style="display: flex; justify-content: center;">
				<input id="mobileCheck" name="mobileCheck" class="col-md-2"
					placeholder="Mobile Number"></input>
				<button id="mobileCheckButton" name="mobileCheckButton"
					class="col-md-1" style="margin-left: 10px;">Check Customer</button>
			</div>
		</div>
	</form>
	<div id="reEnterPhone" class="container-fluid col-md-12 mt-5"
		style="color: white;">
		<div class="row" style="display: flex; justify-content: center;">
			<button id="reMobile" class="col-md-2" onclick="reEnter()"
				style="margin-left: 10px;">Re-Enter Mobile Number</button>
		</div>
	</div>
	<form method="post" action="/subscription/addCustomer">
	<div id="newRegister" class="container-fluid col-md-12 mt-5">
		<div class="row"
			style="color: white; display: flex; justify-content: center;">
			<input id="customerName" class="col-md-2"
				placeholder="Customer Name" /> 
				<input id="birthDate"
				class="col-md-1" style="margin-left: 10px; width: 80px;"
				placeholder="DD" /> 
				<input id="birthDate" class="col-md-1"
				style="margin-left: 10px; width: 80px;" placeholder="MM" />
			<button class="col-md-1" style="margin-left: 10px;">Register</button>
		</div>
	</div>
	</form>
	<div id="billList">
		<div id="itemList" class="container-fluid col-md-12">
			<p id="count" style="display: none;">1</p>
			<div id="product1" class="row col-md-12" style="color: white; justify-content: center;">
				<input id="item1" name="item1" class="col-md-1" placeholder="Product code">
			</div>
		</div>
		<div class="container-fluid col-md-12 mt-5">
			<div class="row" style="display:flex; justify-content: center; color: white;">
				<button id="newItem" class="col-md-1" onclick="addProductBox()">Add item</button>
				<button id="checkout" class="col-md-1" style="margin-left: 10px;">Check</button>
			</div>
		</div>
		</div>
		<div id="checkoutDiv" class="container-fluid col-md-12 mt-1" style="color: white;">
		<div class="row" style="display: flex; justify-content: center;">
			<input id="checkoutList" class="col-md-12" style="margin-left: 10px; display:none;"/>
		</div>
		<div class="row" style="display: flex; justify-content: center;">
			<button id="checkout" class="col-md-1" style="margin-left: 10px;" onclick="checkoutList()">Checkout</button>
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
</body>
<script src="js/billing.js"></script>
</html>