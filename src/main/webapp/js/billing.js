function reEnter() {
	let reEnterPhone = document.getElementById('reEnterPhone');
	let customerCheck = document.getElementById('customerCheck');
	let mobileCheck = document.getElementById('mobileCheck');
	let newRegister = document.getElementById('newRegister');
	let billList = document.getElementById('billList');
	let checkout = document.getElementById('checkoutDiv');
	reEnterPhone.style.display = "none";
	newRegister.style.display = "none";
	billList.style.display = "none";
	checkout.style.display = "none";
	customerCheck.style.display = "block";
	let number = sessionStorage.getItem('customer_phone');
	mobileCheck.value = number;
}

function checkedItems(checkCount) {
	let count = parseInt(checkCount);
	let itemList = document.getElementById('item').innerText.split(',');
	let quantityList = document.getElementById('quantity').innerText.split(',');
	document.getElementById('itemList').innerHTML = '';
	for (i = 1; i <= count; i++) {
		let divId = 'product' + i
		let inputBox = 'item'
		let quantity = 'quantity'
		let parent = document.getElementById('itemList');
		let child = document.createElement('div');
		child.setAttribute('id', divId);
		child.setAttribute('class', "row col-md-12")
		child.setAttribute('style', 'color: white; justify-content: center;');
		parent.appendChild(child);
		child = document.createElement('input');
		parent = document.getElementById(divId);
		child.setAttribute('name', inputBox + i);
		child.setAttribute('id', inputBox + i);
		child.setAttribute('value', itemList[i - 1]);
		child.setAttribute('class', "col-md-1");
		child.setAttribute('placeholder', 'Product code');
		parent.appendChild(child);
		child = document.createElement('input');
		parent = document.getElementById(divId);
		child.setAttribute('name', quantity + i);
		child.setAttribute('id', quantity + i);
		child.setAttribute('value', quantityList[i - 1]);
		child.setAttribute('class', "col-md-1");
		child.setAttribute('placeholder', 'Quantity');
		parent.appendChild(child);
	}
	document.getElementById('item').innerText = '';
	document.getElementById('quantity').innerText = '';
	/*document.getElementById('checkoutBillState').value = document.getElementById('bill_state').innerText*/
}

function addProductBox() {
	let count = parseInt(document.getElementById('count').innerText) + 1
	let divId = 'product' + count
	let inputBox = 'item'
	let quantity = 'quantity'
	let parent = document.getElementById('itemList');
	let child = document.createElement('div');
	child.setAttribute('id', divId);
	child.setAttribute('class', "row col-md-12")
	child.setAttribute('style', 'color: white; justify-content: center;');
	parent.appendChild(child);
	child = document.createElement('input');
	parent = document.getElementById(divId);
	child.setAttribute('name', inputBox + count);
	child.setAttribute('id', inputBox + count);
	child.setAttribute('class', "col-md-1");
	child.setAttribute('placeholder', 'Product code');
	parent.appendChild(child);
	child = document.createElement('input');
	parent = document.getElementById(divId);
	child.setAttribute('name', quantity + count);
	child.setAttribute('id', quantity + count);
	child.setAttribute('class', "col-md-1");
	child.setAttribute('placeholder', 'Quantity');
	parent.appendChild(child);
	document.getElementById('count').innerText = parseInt(document.getElementById('count').innerText) + 1
}

let bill_status = document.getElementById('bill_state').innerText;
if (document.getElementById('bill_state').innerText == 'BC') {
	document.getElementById('checkTotal').innerText = document.getElementById('totalAmount').innerText
	document.getElementById('availablePoints').innerText = "Available points : "+document.getElementById('points').innerText
	checkedItems(document.getElementById('check_count').innerText);
}
let reEnterPhone = document.getElementById('reEnterPhone');
let customerCheck = document.getElementById('customerCheck');
let newRegister = document.getElementById('newRegister');
let billList = document.getElementById('billList');
let checkoutDiv = document.getElementById('checkoutDiv');
reEnterPhone.style.display = "none";
if (document.getElementById('customer_phone').innerText == 'null') {
	reEnterPhone.style.display = "none";
	newRegister.style.display = "none";
	billList.style.display = "none";
	checkoutDiv.style.display = "none";
	customerCheck.style.display = "block";
}
else if (document.getElementById('customer_phone').innerText == 'INV') {
	reEnterPhone.style.display = "none";
	newRegister.style.display = "none";
	billList.style.display = "none";
	checkoutDiv.style.display = "none";
	customerCheck.style.display = "block";
}
else if (document.getElementById('customer_phone').innerText != null && document.getElementById('customer_name').innerText == 'NA') {
	reEnterPhone.style.display = "block";
	newRegister.style.display = "block";
	billList.style.display = "none";
	customerCheck.style.display = "none";
	checkoutDiv.style.display = "none";
}
else if (document.getElementById('customer_phone').innerText != null && document.getElementById('customer_name').innerText == 'INVB') {
	reEnterPhone.style.display = "block";
	newRegister.style.display = "block";
	billList.style.display = "none";
	customerCheck.style.display = "none";
	checkoutDiv.style.display = "none";
}
else {
	reEnterPhone.style.display = "block";
	billList.style.display = "block";
	checkoutDiv.style.display = "block";
	newRegister.style.display = "none";
	customerCheck.style.display = "none";
}

