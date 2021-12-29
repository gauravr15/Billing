<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="bootstrap.min.css">
<link rel="stylesheet" href="style.css">
<script>

</script>
</head>
<body id="pageBody">
    <div class="container-fluid col-md-12" style="display: flex; justify-content: center;">
        <div class="row" style="text-align: center;"><img src="assets/loginAssets/banner.png" style="max-width:80px; max-height: 80px;"></div>
    </div>
    <div class="brandAddress">
        <div class="container-fluid col-md-12 mt-2" style=" display: flex; justify-content: center;">
            <div class="row">
                <p>The Radiance Beauty Parlour</p>
            </div>
        </div>
        <hr class="mt-0" style="border-top: solid 1px #000 !important;">
        <div class="container-fluid col-md-12" style=" display: flex; flex-direction: column; align-items: center;">
            <div class="row">
                <p>D.N.Singh Road</p>
            </div>
            <div class="row" style="margin-top: -15px;">
                <p>Kharmanchak, Bhagalpur</p>
            </div>
            <div class="row" style="margin-top: -15px;">
                <p>Pin code - 812001</p>
            </div>
            <div class="row" style="margin-top: -15px;">
                <p>Mobile No. - 9546573640</p>
            </div>
        </div>
        </div>
        <hr class="mt-0" style="border-top: solid 1px #000 !important;">
        <div class="container-fluid col-md-6">
            <table style="width:100%; text-align: center;" id="billItem">
                <tr>
                  <th>Item</th>
                  <th>Qty</th>
                  <th>Price</th>
                </tr>
                <tr> 
                    <td colspan="3" > <hr style="border: 1px solid #000 !important;" /> </td>      
                  </tr>
                  <div id="billList">

                </div>
                </table>
        </div>
        <hr class="mt-0" style="border-top: solid 1px #000 !important;">
        <div class="container-fluid col-md-6">
            <table style="width:100%; text-align: center;" id="billItem">
                <tr>
                  <th>Total</th>
                  <div id="total">
                  
                  </div>
                </tr>
                </table>
        </div>
        <div class="container-fluid col-md-6">
            <table style="width:100%; text-align: center;margin-left: -10px;" id="billItem">
                <tr>
                  <th>Discount</th>
                  <div id="discount">
                  
                  </div>
                </tr>
                </table>
        </div>
        <div class="container-fluid col-md-6">
            <table style="width:100%; text-align: center;margin-left: -20px;" id="billItem">
                <tr>
                  <th>Payable amount</th>
                  <div id="payAmount">
                  
                  </div>
                </tr>
                </table>
        </div>
        <hr class="mt-0" style="border-top: solid 1px #000 !important;">
    </body>
<script src="js/billing.js"></script>
</html>