<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="bootstrap.min.css">
<link rel="stylesheet" href="printBill.css">
<%
String checkoutUser = (String) session.getAttribute("checkoutUser");
String itemList = (String) session.getAttribute("itemList");
String qtyList = (String) session.getAttribute("qtyList");
String rateList = (String) session.getAttribute("rateList");
String priceList = (String) session.getAttribute("priceList");
String total = (String) session.getAttribute("total");
String checkoutDiscount = (String) session.getAttribute("checkoutDiscount");
String payAmount = (String) session.getAttribute("payAmount");
String billState = (String) session.getAttribute("billState");
String transTime = (String) session.getAttribute("transTime");
String discountMode = (String) session.getAttribute("discountMode");
%>
<p id="checkoutUser" style="display: none;"></p>
<p id="itemList" style="display: none;"></p>
<p id="qtyList" style="display: none;"></p>
<p id="rateList" style="display: none;"></p>
<p id="priceList" style="display: none;"></p>
<p id="total" style="display: none;"></p>
<p id="checkoutDiscount" style="display: none;"></p>
<p id="payAmount" style="display: none;"></p>
<p id="billState" style="display: none;"></p>
<p id="transTime" style="display: none;"></p>
<p id="discountMode" style="display: none;"></p>
<script>
document.getElementById('checkoutUser').innerText = '<%=checkoutUser%>';
document.getElementById('itemList').innerText = '<%=itemList%>';
document.getElementById('qtyList').innerText = '<%=qtyList%>';
document.getElementById('rateList').innerText = '<%=rateList%>';
document.getElementById('priceList').innerText = '<%=priceList%>';
document.getElementById('total').innerText = '<%=total%>';
document.getElementById('checkoutDiscount').innerText = '<%=checkoutDiscount%>';
document.getElementById('payAmount').innerText = '<%=payAmount%>';
document.getElementById('billState').innerText = '<%=billState%>';
document.getElementById('transTime').innerText = '<%=transTime%>';
if(document.getElementById('billState').innerText.localeCompare('BI') == 0){
	alert("Please click on CHECK before proceeding or keep the points within limit.");
	history.go(-1);
}
document.getElementById('discountMode').innerText = '<%=discountMode%>';
</script>
<%
session.setAttribute("checkoutUser",null);
session.setAttribute("itemList",null);
session.setAttribute("qtyList",null);
session.setAttribute("rateList",null);
session.setAttribute("priceList",null);
session.setAttribute("total",null);
session.setAttribute("checkoutDiscount",null);
session.setAttribute("payAmount",null);
session.setAttribute("billState",null);
session.setAttribute("transTime",null);
session.setAttribute("discountMode",null);
%>
</head>
<body id="pageBody">
	<div class="container-fluid col-md-12"
		style="display: flex; justify-content: center;">
		<div class="row" style="text-align: center;">
			<img src="assets/loginAssets/banner.png"
				style="max-width: 80px; max-height: 80px;">
		</div>
	</div>
	<div class="container-fluid col-md-12" id="addHeader"
		style="font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif; display: flex; justify-content: center;">
		<div style="text-align: center;">
			<div class="col-md-12"
				style="display: flex; flex-direction: column; text-align: center; font-weight: 1000; text-decoration: underline;">
				The Radiance Beauty Parlour</div>
			<div class="col-md-12"
				style="display: flex; flex-direction: column; text-align: center;">Kharmanchak,
				D.N.Singh Road</div>
			<div class="col-md-12"
				style="display: flex; flex-direction: column; text-align: center;">Bhagalpur,
				Pin no. - 812001</div>
			<div class="col-md-12" style="display: flex; flex-direction: column;">Mob
				No.- 9546573640</div>
		</div>
	</div>
	<div class="container-fluid col-md-12"
		style="display: flex; justify-content: center;">
		<div class="row" style="text-align: center;">
			<p id="dateTime" style="font-weight: 500"></p>
		</div>
	</div>
	<hr id="firstLine" class="mt-0"
		style="border-top: solid 4px #000 !important;">
	<div class="container-fluid col-md-4" id="header"
		style="font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;">
		<div class="row">
			<p class="col">Item</p>
			<p class="col">Qty</p>
			<p class="col">Rate</p>
			<p class="col">Amount</p>
		</div>
	</div>
	<hr id="secondLine" class="mt-0"
		style="border-top: solid 4px #000 !important;">
	<div class="container-fluid col-md-4" id="billList"
		style="font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;">

	</div>
	<hr class="mt-0" style="border-top: solid 4px #000 !important;">
	<div class="container-fluid col-md-4"
		style="font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;">
		<div class="row" id="discountDiv"
			style="text-align: right; font-weight: 1000;">
			<p class="col">Discount</p>
			<p class="col" id="discount"></p>
		</div>
		<div class="row" style="text-align: right; font-weight: 1000;">
			<p class="col">Payable Amount</p>
			<p class="col" id="billTotal"></p>
		</div>
	</div>
	<hr class="mt-0" style="border-top: solid 1px #000 !important;">
	<div class="container-fluid col-md-4"
		style="font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;">
		<div class="row" style="text-align: center;">
			<p>Thank you. Please visit again.</p>
		</div>
	</div>
	<script>
    function saveBill(){
    	document.getElementById('generateBill').style.display = "none"
    	document.getElementById('printCheckoutUser').value = document.getElementById('checkoutUser').innerText
    	document.getElementById('printItemList').value = document.getElementById('itemList').innerText
    	document.getElementById('printQtyList').value = document.getElementById('qtyList').innerText
    	document.getElementById('printRateList').value = document.getElementById('rateList').innerText
    	document.getElementById('printPriceList').value = document.getElementById('priceList').innerText
    	document.getElementById('printTotal').value = document.getElementById('total').innerText
    	document.getElementById('printCheckoutDiscount').value = document.getElementById('checkoutDiscount').innerText
    	document.getElementById('printPayAmount').value = document.getElementById('payAmount').innerText
    	document.getElementById('disMode').value = document.getElementById('discountMode').innerText
    	var select = document.getElementById('paymentMode');
    	var value = select.options[select.selectedIndex].value;
    	document.getElementById('payMode').value = value;
    }
    </script>
	<form onsubmit="saveBill()" method="get"
		action="/subscription/saveBill">
		<div class="container-fluid col-md-4"
			style="font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;">
			<input id="printCheckoutUser" name="printCheckoutUser"
				style="display: none;" /> <input id="printItemList"
				name="printItemList" style="display: none;" /> <input
				id="printQtyList" name="printQtyList" style="display: none;" /> <input
				id="printRateList" name="printRateList" style="display: none;" /> <input
				id="printPriceList" name="printPriceList" style="display: none;" />
			<input id="printTotal" name="printTotal" style="display: none;" /> <input
				id="printCheckoutDiscount" name="printCheckoutDiscount"
				style="display: none;" /> <input id="printPayAmount"
				name="printPayAmount" style="display: none;" />
				<input id="payMode"
				name="payMode" style="display: none;" />
				<input id="disMode"
				name="disMode" style="display: none;" />
				<div id = "payment" class="row" style="text-align: center;">
				<label for="pay">Choose payment method : </label>
		<select id="paymentMode" name="PaymentMode" id="cars">
			<option value="cash">Cash</option>
			<option value="online">Online</option>
		</select>
		</div>
			<div class="row mt-2" style="text-align: center;">
				<button id="generateBill" type="submit">Generate Bill</button>
			</div>
		</div>
	</form>
	<div class="container-fluid col-md-4"
		style="font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;">
		<div class="row" style="text-align: center;">
			<button id="finalPrint" onclick="window.print()">PrintBill</button>
		</div>
	</div>
	<div class="container col-md-12 mt-5">
		<div class="row" style="justify-content: center;">
			<div class="col-md-2 py-3">
				<a href="/subscription/home.jsp">
					<button class="w-100 " id="HomeBtn">Back to Home page</button>
				</a>
			</div>
		</div>
	</div>
	<script src="js/printBill.js"></script>
</body>
</html>