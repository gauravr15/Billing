dateList = document.getElementById('dateList').innerText.split(",");
onlineEarning = document.getElementById('onlineEarning').innerText.split(",");
offlineEarning = document.getElementById('offlineEarning').innerText.split(",");
totalEarning = document.getElementById('totalEarning').innerText.split(",");
expense = document.getElementById('expense').innerText.split(",");
profit = document.getElementById('profit').innerText.split(",");
let pre = "entry"
for (let i = 0; i < dateList.length; i++) {
	parent = document.getElementById('summary');
	child = document.createElement('tr');
	child.setAttribute('id', pre + i);
	parent.appendChild(child);
	parent = document.getElementById(pre + i);
	child = document.createElement('td');
	child.innerText = dateList[i];
	parent.appendChild(child);
	parent = document.getElementById(pre + i);
	child = document.createElement('td');
	child.innerText = onlineEarning[i];
	parent.appendChild(child);
	parent = document.getElementById(pre + i);
	child = document.createElement('td');
	child.innerText = offlineEarning[i];
	parent.appendChild(child);
	parent = document.getElementById(pre + i);
	child = document.createElement('td');
	child.innerText = totalEarning[i];
	parent.appendChild(child);
	parent = document.getElementById(pre + i);
	child = document.createElement('td');
	child.innerText = expense[i];
	parent.appendChild(child);
	parent = document.getElementById(pre + i);
	child = document.createElement('td');
	child.innerText = profit[i];
	parent.appendChild(child);
}