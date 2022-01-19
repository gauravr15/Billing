package com.odin.inventory;

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
import com.odin.constantValues.status;
import com.odin.dbManager.DBConnectionAgent;

public class AddNewProduct extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3307012642487278422L;
	Logger LOG = Logger.getLogger(AddNewProduct.class.getClass());
	
	private String prodId;
	private String prodName;
	private String prodPrice;
	private String prodCode;
	
	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(String prodPrice) {
		this.prodPrice = prodPrice;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}

	private void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.debug("Going to insert new product");
		if(!req.getParameter("prodId").isEmpty() && !req.getParameter("prodName").isEmpty() && !req.getParameter("prodPrice").isEmpty()) {
			setProdId(req.getParameter("prodId"));
			setProdName(req.getParameter("prodName"));
			setProdPrice(req.getParameter("prodPrice"));
			HttpSession session = req.getSession();
			if(req.getParameter("prodCode").isEmpty()) {
				setProdCode(getProdName());
			}
			else {
				setProdCode(req.getParameter("prodCode"));
			}
			DBConnectionAgent dbObj = new DBConnectionAgent();
			Connection conn = dbObj.connectionAgent();
			PreparedStatement stmt = null;
			String query = "INSERT INTO SERVICES VALUES (?,?,?,?)";
			try {
				stmt = conn.prepareStatement(query);
				stmt.setString(1, getProdId());
				stmt.setString(2, getProdName());
				stmt.setString(3, getProdPrice());
				stmt.setString(4, getProdCode());
				if(stmt.executeUpdate() !=0) {
					session.setAttribute("pageStatus", status.ProductEntry.SUCCESS.values.toString());
					LOG.debug("Record inserted successfully");
				}
				else {
					session.setAttribute("pageStatus", status.ProductEntry.FAIL.values.toString());
					LOG.error("Record insertion failed");
				}
			} catch (SQLException e) {
				LOG.error(e);
			}
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e1) {
				LOG.error(e1);
			}
			try {
				res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":"
						+ ConfigParamMap.params.get("HOST_PORT") + "/subscription/addNewItem");
			} catch (IOException e) {
				LOG.error(e);
			}
		}
	}
}
