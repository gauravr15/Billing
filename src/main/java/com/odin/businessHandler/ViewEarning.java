package com.odin.businessHandler;

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
import com.odin.dbManager.DBConnectionAgent;

public class ViewEarning extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 53053759750276978L;
	Logger LOG = Logger.getLogger(ViewEarning.class.getClass());
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOnlineEarning() {
		return onlineEarning;
	}

	public void setOnlineEarning(String onlineEarning) {
		this.onlineEarning = onlineEarning;
	}

	public String getOfflineEarning() {
		return offlineEarning;
	}

	public void setOfflineEarning(String offlineEarning) {
		this.offlineEarning = offlineEarning;
	}

	public String getTotalEarning() {
		return totalEarning;
	}

	public void setTotalEarning(String totalEarning) {
		this.totalEarning = totalEarning;
	}

	public String getExpense() {
		return expense;
	}

	public void setExpense(String expense) {
		this.expense = expense;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	String date;
	String onlineEarning;
	String offlineEarning;
	String totalEarning;
	String expense;
	String profit;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.trace("Inside View Earning class");
		setDate("");
		setOnlineEarning("");
		setOfflineEarning("");
		setTotalEarning("");
		setExpense("");
		setProfit("");
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn = dbObj.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM EARNING ORDER BY DATE DESC limit 30";
		try {
			stmt = conn.prepareStatement(query);
			LOG.debug("Query to fire "+query);
			rs = stmt.executeQuery();
			while(rs.next()) {
				setDate(getDate()+rs.getString("DATE").split(" ")[0]+",");
				setOnlineEarning(getOnlineEarning()+rs.getString("ONLINE_EARNING")+",");
				setOfflineEarning(getOfflineEarning()+rs.getString("OFFLINE_EARNING")+",");
				setTotalEarning(getTotalEarning()+rs.getString("TOTAL_EARNING")+",");
				setExpense(getExpense()+rs.getString("EXPENSE")+",");
				setProfit(getProfit()+rs.getString("PROFIT")+",");
			}
			setDate(getDate().substring(0, getDate().length() - 1));
			setOnlineEarning(getOnlineEarning().substring(0,getOnlineEarning().length() - 1));
			setOfflineEarning(getOfflineEarning().substring(0,getOfflineEarning().length() - 1));
			setTotalEarning(getTotalEarning().substring(0,getTotalEarning().length() - 1));
			setExpense(getExpense().substring(0,getExpense().length() - 1));
			setProfit(getProfit().substring(0,getProfit().length() - 1));
			LOG.debug("date "+getDate());
			LOG.debug("online earning "+getOnlineEarning());
			LOG.debug("offline earning "+getOfflineEarning());
			LOG.debug("total earning "+getTotalEarning());
			LOG.debug("expense "+getExpense());
			LOG.debug("profit "+getProfit());
			HttpSession session = req.getSession();
			session.setAttribute("dateList", getDate());
			session.setAttribute("onlineEarning", getOnlineEarning());
			session.setAttribute("offlineEarning", getOfflineEarning());
			session.setAttribute("totalEarning", getTotalEarning());
			session.setAttribute("expense", getExpense());
			session.setAttribute("profit", getProfit());
		} catch (SQLException e) {
			LOG.error(e);
		}
		try {
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		try {
			res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":"
					+ ConfigParamMap.params.get("HOST_PORT") + "/subscription/earning.jsp");
		} catch (IOException e) {
			LOG.error(e);
		}
	}
}
