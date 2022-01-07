package com.odin.pageController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;
import com.odin.configManager.ServiceMap;
import com.odin.constantValues.status.BillState;
import com.odin.dbManager.DBConnectionAgent;

public class ItemCheck extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3840225684411902795L;
	
	Logger LOG = Logger.getLogger(ItemCheck.class.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	private void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.debug("Inside Item Check class");
		String billList = req.getParameter("checkItemList");
		String customer_name = req.getParameter("checkItemCustomerName");
		String customer_phone = req.getParameter("checkItemCustomerPhone");
		String customer_id = req.getParameter("checkItemCustomerId");
		String[] itemList = billList.split(",");
		String item = "";
		String quantity ="";
		double totalAmount = 0;
		HttpSession session = req.getSession();
		for(int i=0;i<itemList.length;i++) {
			if(itemList[i]!=null && ServiceMap.service_code.get(ServiceMap.services.get(Integer.valueOf(itemList[i].split(" ")[0])))!=null) {
			item = item+ServiceMap.services.get(Integer.parseInt(itemList[i].split(" ")[0]))+",";
			quantity = quantity+itemList[i].split(" ")[1]+",";
			totalAmount = totalAmount + ((int) ServiceMap.service_charges.get(ServiceMap.services.get(Integer.parseInt(itemList[i].split(" ")[0]))))*Integer.parseInt(itemList[i].split(" ")[1]);
			}
		}
		DBConnectionAgent dbObject = new DBConnectionAgent();
		String query = "SELECT POINTS FROM CUSTOMER_DETAILS WHERE PHONE = ?";
		Connection conn = dbObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String _points = null;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, customer_phone);
			rs = stmt.executeQuery();
			while(rs.next()) {
				_points = rs.getString("POINTS");
			}
			LOG.debug("Available points is : "+_points);
		} catch (SQLException e1) {
			LOG.debug(e1);
		}
		item = item.substring(0, item.length()-1);
		quantity = quantity.substring(0, quantity.length()-1);
		session.setAttribute("customer_name", customer_name);
		session.setAttribute("customer_phone", customer_phone);
		session.setAttribute("customer_id", customer_id);
		session.setAttribute("item", item);
		session.setAttribute("quantity", quantity);
		session.setAttribute("count", Integer.toString(itemList.length));
		session.setAttribute("checkTotal", Double.toString(totalAmount));
		session.setAttribute("billState", BillState.BILL_CHECK.values);
		session.setAttribute("points", _points);
		try {
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e1) {
			LOG.error(e1);
		}
		try {
			redirectSelf(res);
		} catch (IOException e) {
			LOG.debug(e);
		}
	}
	void redirectSelf(HttpServletResponse res) throws IOException {
		res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
				+ "/subscription/billing.jsp");
	}
}
