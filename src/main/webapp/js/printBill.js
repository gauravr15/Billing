/*
*
*
*
*/

if (document.getElementById('checkoutUser') != null) {
	if(document.getElementById('checkoutDiscount').innerText==''){
		document.getElementById('discountDiv').style.display = "none";
	}
	else{
		document.getElementById('discount').innerText = document.getElementById('checkoutDiscount').innerText
	}
	document.getElementById('billTotal').innerText = document.getElementById('payAmount').innerText;
	frameBill();
}

if(document.getElementById('billState').innerText != 'null'){
	document.getElementById('finalPrint').style.display = "visible";
	document.getElementById('HomeBtn').style.display = "visible"
	document.getElementById('generateBill').style.display = "none";
	document.getElementById('dateTime').innerText = "Date-"+document.getElementById('transTime').innerText
	document.getElementById('dateTime').style.display = "visible"
}
else if(document.getElementById('billState').innerText == 'null'){
	document.getElementById('finalPrint').style.display = "none";
	document.getElementById('HomeBtn').style.display = "none"
	document.getElementById('generateBill').style.display = "visible";
	document.getElementById('dateTime').style.display = "none"
}


function frameBill() {
	let count = document.getElementById('itemList').innerText.split(',').length;
	let itemList = document.getElementById('itemList').innerText.split(',');
	let qtyList = document.getElementById('qtyList').innerText.split(',');
	let rateList = document.getElementById('rateList').innerText.split(',');
	let priceList = document.getElementById('priceList').innerText.split(',');
	let pre = 'itemList';
	for (i = 0; i < count; i++) {
		let parent = document.getElementById('billList')
		let child = document.createElement('div')
		child.setAttribute('class', 'row')
		child.setAttribute('id', pre+(i+1))
		parent.appendChild(child)
		child = document.createElement('p')
		child.setAttribute('class', 'col')
		parent = document.getElementById(pre+(i+1))
		parent.appendChild(child)
		child.innerText = itemList[i]
		child = document.createElement('p')
		child.setAttribute('class', 'col')
		parent.appendChild(child)
		child.innerText = qtyList[i]
		child = document.createElement('p')
		child.setAttribute('class', 'col')
		parent.appendChild(child)
		child.innerText = rateList[i]
		child = document.createElement('p')
		child.setAttribute('class', 'col')
		parent.appendChild(child)
		child.innerText = priceList[i]
	}
}