package com.odin.inventory;

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
import com.odin.constantValues.status;
import com.odin.dbManager.DBConnectionAgent;

public class FetchAvailableServiceId extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1291664267513339067L;
	Logger LOG = Logger.getLogger(AddNewProduct.class.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	private void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.debug("Fetching available service id");
		if(ConfigParamMap.params.get("PRODUCT_ID") == null) {
			LOG.debug("No configuration available for PRODUCT_ID in config_params table under SYSTEM module, so using default as MANUAL");
			ConfigParamMap.params.put("PRODUCT_ID",status.ProductEntry.MANUAL.values);
		}
		LOG.debug("product id insertion mode is set as : "+ConfigParamMap.params.get("PRODUCT_ID"));
		if (ConfigParamMap.params.get("PRODUCT_ID").equals(status.ProductEntry.AUTOMATIC.values.toString())) {
			LOG.debug("Service id entry is set to automatic");
			DBConnectionAgent dbObj = new DBConnectionAgent();
			Connection conn = dbObj.connectionAgent();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String query = "select MAX(SERVICE_ID) FROM SERVICES";
			String availableServiceId = "";
			try {
				stmt = conn.prepareStatement(query);
				rs = stmt.executeQuery();
				while (rs.next()) {
					availableServiceId = Integer.toString(Integer.parseInt(rs.getString(1)) + 1);
				}
				LOG.debug("Available service id is : " + availableServiceId);
			} catch (SQLException e) {
				LOG.error(e);
			}
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e1) {
				LOG.error(e1);
			}
			HttpSession session = req.getSession();
			session.setAttribute("availableServiceId", availableServiceId);
			if(session.getAttribute("pageStatus") !=null) {
				session.setAttribute("pageStatus", session.getAttribute("pageStatus"));
			}
			else {
				session.setAttribute("pageStatus", "");
			}
		} else if(ConfigParamMap.params.get("PRODUCT_ID").equals(status.ProductEntry.MANUAL.values.toString())){
			LOG.debug("Service id insertion mode is set to manual");
		}
		try {
			res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":"
					+ ConfigParamMap.params.get("HOST_PORT") + "/subscription/addNewItem.jsp");
		} catch (IOException e) {
			LOG.error(e);
		}
	}
}
