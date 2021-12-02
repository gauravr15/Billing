<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel ="stylesheet" href = "bootstrap.min.css">
<% String user=(String)session.getAttribute("user"); %>
<% String level= (String)session.getAttribute("level");%>
<script>
var user = '${user}';
var level = '${level}';
sessionStorage.setItem("user",user);
sessionStorage.setItem("level",level);
</script>
</head>
<body style="background-color:#111827;">
    <div class="container col-md-1 mt-5" style="color: white; display: flex; justify-content: center;">
        <img src="assets/loginAssets/rCrown.jpg" style="border-radius: 60px;" height="100px" width="100px"></img>
    </div>
    <div class="container col-md-12 mt-5" style="padding: 10px ;color: aliceblue; display: flex; justify-content: center;">
        <h1 style="font-family: fangsong;">The Radiance Beauty Parlour</h1>
    </div>
    <div class="container col-md-12 mt-5">
        <div class="row" style="justify-content: center;">
            <div class="col-md-2 py-3">
                <button class="w-100 " style="border-radius: 20px; border:lime; background-color:lightseagreen; color:black; height: 80px;">Billing</button>
            </div>
            <div class="col-md-2 py-3">
                <button class="w-100" style="border-radius: 20px; border:lime; background-color:lightseagreen; color:black; height: 80px;">Past Billing</button>
            </div>
            <div class="col-md-2 py-3">
                <button class="w-100" style="border-radius: 20px; border:lime; background-color:lightseagreen; color:black; height: 80px;">Check Points</button>
            </div>
        </div>
        <div class="row" id="employee" style="justify-content: center;">
            <div class="col-md-2 py-3">
                <button class="w-100 " style="border-radius: 20px; border:lime; background-color:mediumslateblue; color:black; height: 80px;">Employee Attendance</button>
            </div>
            <div class="col-md-2 py-3">
                <button class="w-100" style="border-radius: 20px; border:lime; background-color:mediumslateblue; color:black; height: 80px;">Find Employee ID</button>
            </div>
            <div class="col-md-2 py-3">
                <button class="w-100" style="border-radius: 20px; border:lime; background-color:mediumslateblue; color:black; height: 80px;">Employee Payout</button>
            </div>
        </div>
        <div class="row" id = "admin" style="justify-content: center;">
            <div class="col-md-2 py-3">
                <button class="w-100 " style="border-radius: 20px; border:lime; background-color:mediumorchid; color:black; height: 80px;">Send SMS</button>
            </div>
            <div class="col-md-2 py-3">
                <button class="w-100 " style="border-radius: 20px; border:lime; background-color:mediumorchid; color:black; height: 80px;">Create User</button>
            </div>
        </div>
    </div>
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
        <script>
        if(level.localeCompare('3')!=0){
        	document.getElementById('employee').remove()
        	document.getElementById('admin').remove();
        }
        </script>>
</body>
</html>