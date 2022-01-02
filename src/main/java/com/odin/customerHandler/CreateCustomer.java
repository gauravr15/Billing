package com.odin.customerHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;
import com.odin.constantValues.status.CustomerConstants;
import com.odin.dbManager.DBConnectionAgent;

public class CreateCustomer extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4172549981195429326L;
	
	Logger LOG = Logger.getLogger(CreateCustomer.class.getClass());
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	void process(HttpServletRequest req, HttpServletResponse res) {
		String mobile = req.getParameter("new_number");
		String name = req.getParameter("customerName");
		int birthDay = Integer.parseInt(req.getParameter("birthDay"));
		int birthMonth = Integer.parseInt(req.getParameter("birthMonth"));
		HttpSession session = req.getSession();
		if(birthDay < 1 || birthDay > 31 || birthMonth < 1 || birthMonth >12) {
			session.setAttribute("customer_name", CustomerConstants.INVALID_BIRTHDAY.values);
			session.setAttribute("customer_phone", mobile);
			try {
				redirectSelf(res);
			} catch (IOException e) {
				LOG.error(e);
			}
		}
		else {
			DBConnectionAgent DBObject = new DBConnectionAgent();
			Connection conn = DBObject.connectionAgent();
			PreparedStatement stmt = null;
			String query = "INSERT INTO CUSTOMER_DETAILS (NAME, BIRTH_DAY, PHONE) VALUES (?,?,?)";
			try {
				stmt = conn.prepareStatement(query);
				stmt.setString(1, name);
				stmt.setString(2, Integer.toString(birthDay)+"/"+Integer.toString(birthMonth));
				stmt.setString(3, mobile);
				LOG.debug("Query to fire "+query);
				int count = stmt.executeUpdate();
				LOG.debug("insertion status "+count);
			} catch (SQLException e) {
				LOG.error(e);
			}
			finally {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					LOG.error(e);
				}
			}
			try {
				res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
				+ "/subscription/checkCustomer?mobileCheck="+mobile);
			} catch (IOException e) {
				LOG.error(e);
			}
		}
	}
	
	void redirectSelf(HttpServletResponse res) throws IOException {
		res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
				+ "/subscription/billing.jsp");
	}
}
