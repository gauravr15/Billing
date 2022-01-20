<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel ="stylesheet" href = "bootstrap.min.css">
<%String pageStatus = (String) session.getAttribute("pageStatus");%>
<p id="pageStatus" style="display: none;"></p>
<script>
document.getElementById("pageStatus").innerText = '<%=pageStatus%>';
if(document.getElementById("pageStatus").innerText == 'INS'){
	alert("User Created successfully");
}
else if(document.getElementById("pageStatus").innerText == 'INV'){
	alert("All the parameters are mandatory except Joining date, salary and phone number");
}
else if(document.getElementById("pageStatus").innerText == 'ICP'){
	alert("Both the password do not same");
}
else if(document.getElementById("pageStatus").innerText == 'IE'){
	alert("Oops, something went wrong. Please try again");
}
</script>
<%
session.setAttribute("pageStatus","");
%>
</head>
<body style="background-color:#111827;">
    <div class="container col-md-1 mt-5" style="color: white; display: flex; justify-content: center;">
        <img src="assets/loginAssets/rCrown.jpg" style="border-radius: 60px;" height="100px" width="100px"></img>
    </div>
    <div class="container col-md-12 mt-5" style="padding: 10px ;color: aliceblue; display: flex; justify-content: center;">
        <h1 style="font-family: fangsong;">The Radiance Beauty Parlour</h1>
    </div>
    <form method="get" action="/subscription/createUser">
    <div class="container-fluid col-md-12">
        <div class="row" style="display: flex; justify-content: center;">
            <input id="name" name="name" class="col-md-2" style="margin-right: 10px;" placeholder="Employee Name">
            <input id="joinDate" name="joinDate" class="col-md-2" style="margin-right: 10px;" placeholder="Joining Date (yyyy-mm-dd)">
            <input id="salary" name="salary" class="col-md-1" style="margin-right: 10px;" placeholder="Salary">
            <input id="phone" name="phone" class="col-md-1" style="margin-right: 10px;" placeholder="Mobile Number">
            <input id="level" name="level" class="col-md-1" placeholder="Access Level">
        </div>
        <div class="row mt-2" style="display: flex; justify-content: center;">
            <input id="username" name="username" class="col-md-2" style="margin-right: 10px;" placeholder="Username">
            <input id="password" name="password" type="password" class="col-md-2" style="margin-right: 10px;" placeholder="Password">
            <input id="rePassword" name="rePassword" type="password" class="col-md-2" style="margin-right: 10px;" placeholder="Re-enter Password">
        </div>
            <div class="row mt-3" style="display:flex; justify-content: center;">
                <button class="col-md-1 btn-danger">Create User</button>
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
        <img src="assets/loginAssets/odin.png" style="height:70px; width: 83px; margin-left: 30px;"></img>
        </div>
        <div class="col-md-3" >
        <p style="display: inline; font-size:50px;">&#124;</p>
        <p style="display: inline; color: white;">Odin Tech Solutions &#9400; 2021</p>
        </div>
        </div>
        </div>
        </footer>
</body>
</html>