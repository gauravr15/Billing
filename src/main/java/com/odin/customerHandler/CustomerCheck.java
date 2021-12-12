package com.odin.customerHandler;

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
import com.odin.constantValues.CustomerConstants;
import com.odin.dbManager.DBConnectionAgent;

public class CustomerCheck extends HttpServlet {

	Logger LOG = Logger.getLogger(CustomerCheck.class.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.trace("Inside customer check");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String name = null;
		String phone = null;
		String customerId = null;
		HttpSession session = req.getSession();
		String mobile = req.getParameter("mobileCheck");
		if (mobile.length() != 10) {
			session.setAttribute("customer_phone", CustomerConstants.INVALID_PHONE.values);
			LOG.debug("customer mobile is " + session.getAttribute("customer_phone"));
			try {
				redirectSelf(res);
			} catch (IOException e) {
				LOG.error(e);
			}
		} else {
			DBConnectionAgent DBObject = new DBConnectionAgent();
			conn = DBObject.connectionAgent();
			String query = "SELECT NAME, ID FROM CUSTOMER_DETAILS WHERE PHONE = ? ";
			LOG.debug("query to fire " + query);
			try {
				stmt = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				stmt.setString(1, mobile);
				rs = stmt.executeQuery();
				int count = 0;
				while (rs.next()) {
					count = count + 1;
				}
				rs.beforeFirst();
				if (count == 1) {
					while(rs.next()) {
						name = rs.getString("NAME");
						phone = mobile;
						customerId = rs.getString("ID");
					}
					session.setAttribute("customer_name", name);
					session.setAttribute("customer_phone", phone);
					session.setAttribute("customer_id", customerId);
				}
				else {
					session.setAttribute("customer_name", CustomerConstants.NOT_AVAILABLE.values);
					session.setAttribute("customer_phone", CustomerConstants.NOT_AVAILABLE.values);
					session.setAttribute("customer_id", CustomerConstants.NOT_AVAILABLE.values);
				}
				try {
					redirectSelf(res);
				} catch (IOException e) {
					LOG.error(e);
				}
			} 
			catch (SQLException e) {
				LOG.error(e);
			}
		}
	}

	void redirectSelf(HttpServletResponse res) throws IOException {
		res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
				+ "/subscription/billing.jsp");
	}
}