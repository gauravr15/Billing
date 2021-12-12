function reEnter() {
	let reEnterPhone = document.getElementById('reEnterPhone');
	let customerCheck = document.getElementById('customerCheck');
	let mobileCheck = document.getElementById('mobileCheck');
	let newRegister = document.getElementById('newRegister');
	let billList = document.getElementById('billList');
	reEnterPhone.style.display = "none";
	newRegister.style.display = "none";
	billList.style.display = "none";
	customerCheck.style.display = "block";
	let number = sessionStorage.getItem('customer_phone');
	mobileCheck.value = number;
}

function checkoutList() {
	let count = parseInt(document.getElementById('count').innerText)
	let prefix = 'item'
	let checkoutItem = ''
	for (let i = 1; i <= count; i++) {
		let inputId = prefix + i;
		console.log(document.getElementById(inputId).value);
		checkoutItem = checkoutItem + document.getElementById(inputId).value + ',';
	}
	document.getElementById('checkoutList').value = checkoutItem.slice(0, -1)
}

function addProductBox() {
	let count = parseInt(document.getElementById('count').innerText) + 1
	let divId = 'product' + count
	let inputBox = 'item'
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
	document.getElementById('count').innerText = parseInt(document.getElementById('count').innerText) + 1
}

let reEnterPhone = document.getElementById('reEnterPhone');
let customerCheck = document.getElementById('customerCheck');
let newRegister = document.getElementById('newRegister');
let billList = document.getElementById('billList');
reEnterPhone.style.display = "none";
if (document.getElementById('customer_phone').innerText == 'null') {
	reEnterPhone.style.display = "none";
	newRegister.style.display = "none";
	billList.style.display = "none";
	customerCheck.style.display = "block";
}
else if (document.getElementById('customer_phone').innerText == 'INV') {
	reEnterPhone.style.display = "none";
	newRegister.style.display = "none";
	billList.style.display = "none";
	customerCheck.style.display = "block";
}
else if (document.getElementById('customer_phone').innerText == 'NA') {
	reEnterPhone.style.display = "block";
	newRegister.style.display = "block";
	billList.style.display = "none";
	customerCheck.style.display = "none";
}
else {
	reEnterPhone.style.display = "block";
	billList.style.display = "block";
	newRegister.style.display = "none";
	customerCheck.style.display = "none";
}