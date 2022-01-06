package com.odin.billingHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;
import com.odin.constantValues.status.BillState;
import com.odin.dbManager.DBConnectionAgent;
import com.odin.fileProcessor.Tlog;

public class CommitBill extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8800204910303315485L;
	
	Logger LOG = Logger.getLogger(CommitBill.class.getClass());
	
	String checkoutCustomer;
	public Logger getLOG() {
		return LOG;
	}

	public void setLOG(Logger lOG) {
		LOG = lOG;
	}

	public String getCheckoutCustomer() {
		return checkoutCustomer;
	}

	public void setCheckoutCustomer(String checkoutCustomer) {
		this.checkoutCustomer = checkoutCustomer;
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

	
	String checkoutUser;
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
	String paymentMode;
	


	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.debug("going to commit bill");
		HttpSession session = req.getSession();
		setCheckoutUser(req.getParameter("printCheckoutUser"));
		setItemList(req.getParameter("printItemList"));
		setQtyList(req.getParameter("printQtyList"));
		setRateList(req.getParameter("printRateList"));
		setPriceList(req.getParameter("printPriceList"));
		setTotal(req.getParameter("printTotal"));
		setCheckoutDiscount(req.getParameter("printCheckoutDiscount"));
		setPayAmount(req.getParameter("printPayAmount"));
		setCashierId((String)session.getAttribute("user"));
		setPaymentMode(req.getParameter("payMode"));
		String discount;
		if(getCheckoutDiscount() == null || getCheckoutDiscount() == "0") {
			discount = "0";
		}
		else
			discount = req.getParameter("printCheckoutDiscount");
		setCheckoutDiscount(discount);
		int count = getItemList().split(",").length;
		String purchases = "";
		for(int i=0;i<count;i++) {
			purchases = purchases+ getItemList().split(",")[i]+"-"+getQtyList().split(",")[i]+"-"+getRateList().split(",")[i]+",";
		}
		purchases = purchases.substring(0, purchases.length()-1);
		setPurchases(purchases);
		setTransId(UUID.randomUUID().toString());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		LocalDateTime ldt = LocalDateTime.now();
		setTransTime(dtf.format(ldt));
		DBConnectionAgent dbObject = new DBConnectionAgent();
		Connection conn = dbObject.connectionAgent();
		String query = "INSERT INTO CUSTOMER_BILL (customer_id,transaction_id,transaction_date,purchase_info,bill_total,discount,pay_amount,payment_mode,bill_state,cashier_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
		LOG.debug("query to fire : "+query);
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, getCheckoutUser());
			stmt.setString(2, getTransId());
			stmt.setString(3, getTransTime());
			stmt.setString(4, getPurchases());
			stmt.setString(5, getTotal());
			stmt.setString(6, getCheckoutDiscount());
			stmt.setString(7, getPayAmount());
			stmt.setString(8, getPaymentMode());
			stmt.setString(9, getBillState());
			stmt.setString(10, getCashierId());
			stmt.executeUpdate();
		} catch (SQLException e1) {
			LOG.error(e1);
		}
		setThreadId(Thread.currentThread().getName());
		Tlog tLogObj = new Tlog();
		boolean _tlogResp = tLogObj.write(this);
		LOG.debug("Tlog written is : "+_tlogResp);
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
			res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
			+ "/subscription/printBill.jsp");
		} catch (IOException e) {
			LOG.error(e);
		}
	}

}
