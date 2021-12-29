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
String item = (String) session.getAttribute("item");
String quantity = (String) session.getAttribute("quantity");
String count = (String) session.getAttribute("count");
String billState = (String) session.getAttribute("billState");
String checkTotal = (String) session.getAttribute("checkTotal");
%>
<p id="customer_name" style="display: none;"></p>
<p id="customer_phone" style="display: none;"></p>
<p id="customer_id" style="display: none;"></p>
<p id="quantity" style="display: none;"></p>
<p id="check_count" style="display: none;"></p>
<p id="bill_state" style="display: none;"></p>
<p id="item" style="display: none;"></p>
<p id="totalAmount" style="display: none;"></p>
<script>
	document.getElementById('totalAmount').innerText = '<%=checkTotal%>';
	document.getElementById('check_count').innerText = '<%=count%>';
	document.getElementById('bill_state').innerText = '<%=billState%>';
	document.getElementById('item').innerText = '<%=item%>';
	document.getElementById('quantity').innerText = '<%=quantity%>';
	document.getElementById('customer_name').innerText = '<%=customer_name%>';
	document.getElementById('customer_phone').innerText = '<%=customer_phone%>';
	document.getElementById('customer_id').innerText = '<%=customer_id%>';
	let customer_phone = document.getElementById('customer_phone').innerText;
	let customer_name = document.getElementById('customer_name').innerText;
	if (customer_phone.localeCompare('INV') == 0) {
		alert("Mobile number does not contains 10 digits");
	}
	if (customer_name.localeCompare('INVB') == 0) {
		alert("Please check the birthday");
	}
</script>
<%
session.setAttribute("customer_name", null);
session.setAttribute("customer_phone", null);
session.setAttribute("customer_id", null);
session.setAttribute("item", null);
session.setAttribute("quantity", null);
session.setAttribute("count", null);
session.setAttribute("billState", null);
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
	<script>
		function newCustomer() {
			document.getElementById('new_number').value = document
					.getElementById('customer_phone').innerText;
		}
	</script>
	<form onsubmit="newCustomer()" method="get"
		action="/subscription/createCustomer">
		<div id="newRegister" class="container-fluid col-md-12 mt-5">
			<div class="row"
				style="color: white; display: flex; justify-content: center;">
				<input id="new_number" name="new_number" style="display: none;"></input>
				<input id="customerName" name="customerName" class="col-md-2"
					placeholder="Customer Name" /> <input id="birthDay"
					name="birthDay" class="col-md-1"
					style="margin-left: 10px; width: 80px;" placeholder="DD" /> <input
					id="birthMonth" name="birthMonth" class="col-md-1"
					style="margin-left: 10px; width: 80px;" placeholder="MM" />
				<button class="col-md-1" type="submit" style="margin-left: 10px;">Register</button>
			</div>
		</div>
	</form>
	<div id="billList">
		<div id="itemList" class="container-fluid col-md-12">
			<p id="count" style="display: none;">1</p>
			<div id="product1" class="row col-md-12"
				style="color: white; justify-content: center;">
				<input id="item1" name="item1" class="col-md-1"
					placeholder="Product code"> <input id="quantity1"
					name="quantity1" class="col-md-1" placeholder="Quantity">
			</div>
		</div>
		<div class="container-fluid col-md-12 mt-5">
			<div class="row"
				style="display: flex; justify-content: center; color: white;">
				<button id="newItem" class="col-md-1" onclick="addProductBox()">Add
					item</button>
			</div>
		</div>
		<script>
			function checkItem() {
				let count = parseInt(document.getElementById('count').innerText)
				let prefix = 'item'
				let quantity = 'quantity'
				let checkoutItem = ''
				for (let i = 1; i <= count; i++) {
					let inputId = prefix + i;
					console.log(document.getElementById(inputId).value);
					let quantityID = quantity + i;
					console.log(document.getElementById(quantityID).value);
					if (document.getElementById(inputId).value != '' || document.getElementById(quantityID).value != '') {
						checkoutItem = checkoutItem + document.getElementById(inputId).value + ' ' + document.getElementById(quantityID).value + ',';
					}
				}
				document.getElementById('checkItemList').value = checkoutItem.slice(0, -1)
				document.getElementById('checkItemCount').value = document.getElementById('count').innerText;
				document.getElementById('checkItemCustomerName').value = document.getElementById('customer_name').innerText;
				document.getElementById('checkItemCustomerPhone').value = document.getElementById('customer_phone').innerText;
				document.getElementById('checkItemCustomerId').value = document.getElementById('customer_id').innerText;
			}
		</script>
		<form onsubmit="checkItem()" action="/subscription/checkItem"
			method="get">
			<div class="container-fluid col-md-12 mt-5">
				<div class="row"
					style="display: flex; justify-content: center; color: white;">
					<input id="checkItemList" name="checkItemList" class="col-md-12"
						style="margin-left: 10px; display: none;" /> <input
						id="checkItemCount" name="checkItemCount" class="col-md-12"
						style="margin-left: 10px; display: none;" /> <input
						id="checkItemCustomerName" name="checkItemCustomerName"
						class="col-md-12" style="margin-left: 10px; display: none;" /> <input
						id="checkItemCustomerPhone" name="checkItemCustomerPhone"
						class="col-md-12" style="margin-left: 10px; display: none;" /> <input
						id="checkItemCustomerId" name="checkItemCustomerId"
						class="col-md-12" style="margin-left: 10px; display: none;" />
					<h1 id="checkTotal" class="col-md-12"
						style="color: red; display: flex; justify-content: center;"></h1>
					<button id="checkoutItem" class="col-md-1" type="submit"
						style="margin-left: 10px;">Check</button>
				</div>
			</div>
		</form>
	</div>
	<script>
		function checkList() {
			let bill_state = document.getElementById('bill_state').innerText
			let counter = 0;
			alert("bill_state is " + bill_state);
			if (bill_state == 'null') {
				counter = document.getElementById('count').innerText
				alert("if block count " + counter);
			} else if (bill_state != 'null') {
				counter = document.getElementById('check_count').innerText
				alert("else block count " + counter);
			}
			let prefix = 'item'
			let quantity = 'quantity'
			let checkoutItem = ''
			for (let i = 1; i <= parseInt(counter); i++) {
				alert("inside loop "+i);
				let inputId = prefix + i;
				console.log(document.getElementById(inputId).value);
				let quantityID = quantity + i;
				console.log(document.getElementById(quantityID).value);
				if (document.getElementById(inputId).value != ''
						|| document.getElementById(quantityID).value != '') {
					checkoutItem = checkoutItem
							+ document.getElementById(inputId).value + ' '
							+ document.getElementById(quantityID).value + ',';
				}
			}
			alert("checkoutList value is " + checkoutItem)
			document.getElementById('checkoutList').value = checkoutItem.slice(0, -1)
			document.getElementById('checkoutBillState').value = bill_state;
		}
	</script>
	<form onsubmit="checkList()" action="/subscription/finalizeBill"
		method="get">
		<div id="checkoutDiv" class="container-fluid col-md-12 mt-1"
			style="color: white;">
			<div class="row" style="display: flex; justify-content: center;">
				<input id="checkoutList" name="checkoutList" class="col-md-12"
					style="margin-left: 10px; display: none;" /> <input
					id="checkoutBillState" name="checkoutBillState" class="col-md-12"
					style="margin-left: 10px; display: none;" />
			</div>
			<div class="row" style="display: flex; justify-content: center;">
				<input id="checkoutDiscount" name="checkoutDiscount"
					class="col-md-1" placeholder="Discount" style="margin-left: 10px;" />
				<button id="checkout" type="submit" name="checkout" value="checkout"
					class="col-md-1" style="margin-left: 10px;">Checkout</button>
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
</body>
<script src="js/billing.js"></script>
</html>