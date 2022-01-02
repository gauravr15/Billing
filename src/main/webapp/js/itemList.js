function checkoutList() {
	alert('hello');
	let count = parseInt(document.getElementById('count').innerText)
	let prefix = 'item'
	let quantity = 'quantity'
	let checkoutItem = ''
	for (let i = 1; i <= count; i++) {
		let inputId = prefix + i;
		console.log(document.getElementById(inputId).value);
		let quantityID = quantity + i;
		console.log(document.getElementById(quantityID).value);
		checkoutItem = checkoutItem + document.getElementById(inputId).value + '*' + document.getElementById(quantityID).value + ',';
	}
	document.getElementById('checkoutList').value = checkoutItem.slice(0, -1)
}