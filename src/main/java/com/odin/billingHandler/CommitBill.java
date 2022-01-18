package com.odin.billingHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.cashback.CalculateCashback;
import com.odin.cashback.RedeemPoints;
import com.odin.configManager.ConfigParamMap;
import com.odin.constantValues.status;
import com.odin.constantValues.status.BillState;
import com.odin.dbManager.DBConnectionAgent;
import com.odin.fileProcessor.Tlog;
import com.odin.smsThread.BillingSMS;

public class CommitBill extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8800204910303315485L;

	Logger LOG = Logger.getLogger(CommitBill.class.getClass());


	public Logger getLOG() {
		return LOG;
	}

	public void setLOG(Logger lOG) {
		LOG = lOG;
	}

	public String getItemList() {
		return itemList;
	}

	public void setItemList(String itemList) {
		this.itemList = itemList;
	}

	public String getQtyList() {
		return qtyList;
	}

	public void setQtyList(String qtyList) {
		this.qtyList = qtyList;
	}

	public String getRateList() {
		return rateList;
	}

	public void setRateList(String rateList) {
		this.rateList = rateList;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCheckoutDiscount() {
		return checkoutDiscount;
	}

	public void setCheckoutDiscount(String checkoutDiscount) {
		this.checkoutDiscount = checkoutDiscount;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}

	public String getCheckoutUser() {
		return checkoutUser;
	}

	public void setCheckoutUser(String checkoutUser) {
		this.checkoutUser = checkoutUser;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public String getPurchases() {
		return purchases;
	}

	public void setPurchases(String purchases) {
		this.purchases = purchases;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCashback() {
		return cashback;
	}

	public void setCashback(String cashback) {
		this.cashback = cashback;
	}
	
	public String getCashBackValidityStatus() {
		return cashBackValidityStatus;
	}

	public void setCashBackValidityStatus(String cashBackValidityStatus) {
		this.cashBackValidityStatus = cashBackValidityStatus;
	}
	
	public String getDiscountMode() {
		return discountMode;
	}

	public void setDiscountMode(String discountMode) {
		this.discountMode = discountMode;
	}
	
	public String getAvailableCashBack() {
		return availableCashBack;
	}

	public void setAvailableCashBack(String availableCashBack) {
		this.availableCashBack = availableCashBack;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	
	public String getBillingMessage() {
		return billingMessage;
	}

	public void setBillingMessage(String billingMessage) {
		this.billingMessage = billingMessage;
	}

	public String getSmsTask() {
		return smsTask;
	}

	public void setSmsTask(String smsTask) {
		this.smsTask = smsTask;
	}
	
	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	String checkoutUser;
	String customerName;
	String customerPhone;
	String billingMessage;
	String itemList;
	String qtyList;
	String rateList;
	String priceList;
	String total;
	String checkoutDiscount;
	String payAmount;
	String billState;
	String purchases;
	String cashierId;
	String transId;
	String transTime;
	String threadId;
	String cashback;
	String paymentMode;
	String cashBackValidityStatus;
	String discountMode;
	String availableCashBack;
	String smsTask;
	String smsType;



	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	private void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.debug("going to commit");
		HttpSession session = req.getSession();
		setCheckoutUser(req.getParameter("printCheckoutUser"));
		setItemList(req.getParameter("printItemList"));
		setQtyList(req.getParameter("printQtyList"));
		setRateList(req.getParameter("printRateList"));
		setPriceList(req.getParameter("printPriceList"));
		setTotal(req.getParameter("printTotal"));
		setCheckoutDiscount(req.getParameter("printCheckoutDiscount"));
		setPayAmount(req.getParameter("printPayAmount"));
		setCashierId((String) session.getAttribute("user"));
		setPaymentMode(req.getParameter("payMode"));
		setDiscountMode(req.getParameter("disMode"));
		String discount = "0";
		boolean cashbackOnDiscount = Boolean.parseBoolean(ConfigParamMap.params.get("CASHBACK_ON_DISCOUNT"));
		LOG.debug("Cashback on discount is set as : "+cashbackOnDiscount);
		if (cashbackOnDiscount == true) {
			LOG.debug("Proceeding with cashbackOnDiscount");
			CalculateCashback cashbackObj = new CalculateCashback();
			setCashback(Integer.toString(cashbackObj.cashBackCalculator((int) Double.parseDouble(getPayAmount()))));
			if (getCheckoutDiscount().isEmpty()) {
				discount = "0";
			} else
				discount = req.getParameter("printCheckoutDiscount");
			setAvailableCashBack(Integer.toString(cashbackObj.cashBackCalculator((int) Double.parseDouble(getPayAmount()))));
		} else if (cashbackOnDiscount == false) {
			LOG.debug("Proceeding without cashbackOnDiscount");
			if (getCheckoutDiscount().isEmpty()) {
				discount = "0";
				CalculateCashback cashbackObj = new CalculateCashback();
				setCashback(Integer.toString(cashbackObj.cashBackCalculator((int) Double.parseDouble(getPayAmount()))));
				setAvailableCashBack(Integer.toString(cashbackObj.cashBackCalculator((int) Double.parseDouble(getPayAmount()))));
			} else {
				discount = req.getParameter("printCheckoutDiscount");
				setAvailableCashBack("0");
			}
		}
		setCheckoutDiscount(discount);
		if(getDiscountMode().equals("RedeemPoints") && !getCheckoutDiscount().equals("0")) {
			RedeemPoints pointsDiscountObj = new RedeemPoints();
			boolean pointRedeemed = pointsDiscountObj.cashbackDiscount(this);
			if(pointRedeemed)
				LOG.debug("CashBack points redeemed successfully");
			else {
				LOG.error("Failed to redeem points setting discount to 0");
				discount = "0";
			}
		}
		int count = getItemList().split(",").length;
		String purchases = "";
		for (int i = 0; i < count; i++) {
			purchases = purchases + getItemList().split(",")[i] + "-" + getQtyList().split(",")[i] + "-"
					+ getRateList().split(",")[i] + ",";
		}
		purchases = purchases.substring(0, purchases.length() - 1);
		setPurchases(purchases);
		setTransId(UUID.randomUUID().toString());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
		LocalDateTime ldt = LocalDateTime.now();
		setTransTime(dtf.format(ldt));
		DBConnectionAgent dbObject = new DBConnectionAgent();
		Connection conn = dbObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "INSERT INTO CUSTOMER_BILL (customer_id,transaction_id,transaction_date,purchase_info,bill_total,discount,cashback_received, cashback_available, cashback_validity,pay_amount,payment_mode,bill_state,cashier_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		LOG.debug("query to fire : " + query);
		try {
			if(Boolean.parseBoolean(ConfigParamMap.params.get("CASHBACK")) == false) 
				setCashBackValidityStatus(status.CashbackValidity.NOT_APPLICABLE.values);
			else if(Integer.parseInt(ConfigParamMap.params.get("COOLDOWN_PERIOD"))== 0)
				setCashBackValidityStatus(status.CashbackValidity.VALID.values);
			else
				setCashBackValidityStatus(Boolean.parseBoolean(ConfigParamMap.params.get("COOLDOWN_POINTS"))== true?status.CashbackValidity.NOT_COOLDOWN.values:status.CashbackValidity.VALID.values);
			LOG.debug("Cashback feature is set as "+Boolean.parseBoolean(ConfigParamMap.params.get("CASHBACK")));
			stmt = conn.prepareStatement(query);
			stmt.setString(1, getCheckoutUser());
			stmt.setString(2, getTransId());
			stmt.setString(3, getTransTime());
			stmt.setString(4, getPurchases());
			stmt.setString(5, getTotal());
			stmt.setString(6, getCheckoutDiscount());
			stmt.setString(7, getCashback());
			stmt.setString(8, getAvailableCashBack());
			stmt.setString(9, getCashBackValidityStatus());
			stmt.setString(10, getPayAmount());
			stmt.setString(11, getPaymentMode());
			stmt.setString(12, getBillState());
			stmt.setString(13, getCashierId());
			stmt.executeUpdate();
			if(getCashBackValidityStatus().equals(status.CashbackValidity.VALID.values)) {
				LOG.debug("Updating points in customer details table");
				query = "SELECT POINTS FROM CUSTOMER_DETAILS WHERE ID = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, getCheckoutUser());
				rs = stmt.executeQuery();
				int _updatedPoints = 0;
				while(rs.next()) {
					_updatedPoints = Integer.parseInt(rs.getString("POINTS")) + Integer.parseInt(getAvailableCashBack());
				}
				query = "UPDATE CUSTOMER_DETAILS SET POINTS = ? WHERE ID = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1,Integer.toString(_updatedPoints));
				stmt.setString(2, getCheckoutUser());
				if(stmt.executeUpdate() != 0 ) {
					LOG.debug("User points updated successfully");
				}
				else
					LOG.error("Failed to update user points");
			}
			query = "SELECT NAME,PHONE FROM CUSTOMER_DETAILS WHERE ID = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, getCheckoutUser());
			rs = stmt.executeQuery();
			while(rs.next()) {
				setCustomerName(rs.getString("NAME"));
				setCustomerPhone(rs.getString("PHONE"));
			}
			query = "SELECT MESSAGE FROM NOTIFICATION WHERE SMS_TYPE = 'BILLING'";
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next()) {
				setBillingMessage(rs.getString("MESSAGE"));
			}
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e1) {
				LOG.error(e1);
			}
		} catch (SQLException e1) {
			LOG.error(e1);
		}
		setThreadId(Thread.currentThread().getName());
		if(Boolean.parseBoolean(ConfigParamMap.params.get("BILLING_SMS")) == true) {
			setSmsType("BILLING");
			LOG.debug("Sending billing sms");
			BillingSMS smsObj = new BillingSMS();
			boolean _response = smsObj.sendSMS(this);
			setSmsTask(_response == true? status.smsSuccess:status.smsFailure);
			if(_response == true)
				LOG.debug("SMS sent successfully");
			else
				LOG.error("Failed to send sms");
		}
		else {
			setSmsTask(status.smsDisabled);
			LOG.debug("Billing sms is disabled.");
		}
		Tlog tLogObj = new Tlog();
		boolean _tlogResp = tLogObj.write(this);
		LOG.debug("Tlog written is : " + _tlogResp);
		session.setAttribute("checkoutUser", req.getParameter("printCheckoutUser"));
		session.setAttribute("itemList", req.getParameter("printItemList"));
		session.setAttribute("qtyList", req.getParameter("printQtyList"));
		session.setAttribute("rateList", req.getParameter("printRateList"));
		session.setAttribute("priceList", req.getParameter("printPriceList"));
		session.setAttribute("total", req.getParameter("printTotal"));
		session.setAttribute("checkoutDiscount", req.getParameter("printCheckoutDiscount"));
		session.setAttribute("payAmount", req.getParameter("printPayAmount"));
		session.setAttribute("transTime", getTransTime());
		session.setAttribute("billState", BillState.BILL_PRINT.values);
		try {
			res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":"
					+ ConfigParamMap.params.get("HOST_PORT") + "/subscription/printBill.jsp");
		} catch (IOException e) {
			LOG.error(e);
		}
	}

}
