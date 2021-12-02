<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel ="stylesheet" href = "bootstrap.min.css">
<link rel ="stylesheet" href = "style.css">
<script>
function phoneSubmit(){
    document.getElementById("phone").style.display = "none";
}
function phoneReenter(){
    document.getElementById("phone").style.display = "block";
}
</script>
</head>
<body style="background-color:#111827;">
    <div class="container col-md-1 mt-5" style="color: white; display: flex; justify-content: center;">
        <img src="assets/loginAssets/rCrown.jpg" style="border-radius: 60px;" height="100px" width="100px"></img>
    </div>
    <div class="container col-md-12 mt-5" style="padding: 10px ;color: aliceblue; display: flex; justify-content: center;">
        <h1 style="font-family: fangsong;">The Radiance Beauty Parlour</h1>
    </div>
    <div class="container col-md-12 mt-5" style="color: white; display: flex; justify-content: center;">
        <div class="row col-md-8" style="display: flex; justify-content: center;">
        <div class="col-md-3">
            <input id="phone" name="phone" placeholder="Mobile Number" style="height:38px; border-radius: 10px; border: none;"></input>
        </div>
        <div class="col-md-2" >
            <button id="phoneBtn" onclick="phoneSubmit()" class ="btn btn-primary" style="border-radius: 10px;">Proceed</button>
        </div>
        <div class="col-md-2" >
            <button id="phoneBtn" onclick="phoneReenter()" class ="btn btn-primary" style="border-radius: 10px;width: 80px;">Back</button>
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
</body>
</html>