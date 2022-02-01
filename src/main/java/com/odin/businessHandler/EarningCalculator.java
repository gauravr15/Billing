package com.odin.businessHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.odin.Exceptions.ConfigException;
import com.odin.configManager.ConfigParamMap;
import com.odin.configManager.QueueManager;
import com.odin.constantValues.status;
import com.odin.dbManager.DBConnectionAgent;

public class EarningCalculator implements Runnable {

	Logger LOG = Logger.getLogger(EarningCalculator.class.getClass());

	public void run() {
		LOG.debug("Inside earning calculator");
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn = dbObj.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime ldt = LocalDateTime.now();
		String currentDate = dtf.format(ldt);
		LOG.debug("Going to check earning for " + currentDate);
		int queue_size = 0;
		if (QueueManager.queue.containsKey("EARNING")) {
			queue_size = QueueManager.queue.get("EARNING");
		} else {
			try {
				throw new ConfigException(
						"PARAM_NAME EARNING IS NOT CONFIGURED IN QUEUE_CONTROL TABLE, using 200 as default");
			} catch (Exception e) {
				queue_size = 200;
				LOG.fatal(e);
			}
		}
		long earningCheckDuration = ConfigParamMap.params.get("RECHECK_EARNING_DURATION") == null
				? earningCheckDuration = 60000
				: (long) Double.parseDouble(ConfigParamMap.params.get("RECHECK_EARNING_DURATION"));
		try {
			String query = "SELECT COUNT(*) FROM CUSTOMER_BILL WHERE TRANSACTION_DATE LIKE ? AND RECONCILE = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, currentDate + "%");
			stmt.setString(2, status.Earning.NOT_DONE.values);
			rs = stmt.executeQuery();
			int _fetchSize = 0;
			while (rs.next()) {
				_fetchSize = Integer.parseInt(rs.getString(1));
			}
			if (_fetchSize == 0) {
				rs.close();
				stmt.close();
				conn.close();
				LOG.debug("Going to sleep for " + earningCheckDuration + " milliseconds");
				try {
					Thread.sleep(earningCheckDuration);
					run();
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
			query = "SELECT * FROM EARNING WHERE DATE LIKE ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, currentDate + "%");
			LOG.debug("Query to fire " + query);
			int _count = 0;
			rs = stmt.executeQuery();
			while (rs.next()) {
				_count = _count + 1;
			}
			double offlineEarning = 0;
			double onlineEarning = 0;
			double totalEarning = 0;
			double profit = 0;
			boolean earningEntryExists = false;
			if (_count != 0) {
				query = "SELECT * FROM EARNING WHERE DATE LIKE ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, currentDate + "%");
				rs = stmt.executeQuery();
				while (rs.next()) {
					offlineEarning = Double.parseDouble(rs.getString("OFFLINE_EARNING"));
					onlineEarning = Double.parseDouble(rs.getString("ONLINE_EARNING"));
					totalEarning = Double.parseDouble(rs.getString("TOTAL_EARNING"));
					profit = Double.parseDouble(rs.getString("PROFIT"));
				}
				earningEntryExists = true;
			} else {
				offlineEarning = 0;
				onlineEarning = 0;
				totalEarning = 0;
				profit = 0;
				earningEntryExists = false;
			}
			LOG.debug("offline earning is " + offlineEarning);
			LOG.debug("online earning is " + onlineEarning);
			LOG.debug("total earning is " + totalEarning);
			LOG.debug("profit earning is " + profit);
			double currentOfflineEarning = 0;
			double currentOnlineEarning = 0;
			double currentTotalEarning = 0;
			double currentProfit = 0;
			List<String> transactionList = new ArrayList<String>();
			query = "SELECT PAY_AMOUNT, PAYMENT_MODE, TRANSACTION_ID FROM CUSTOMER_BILL WHERE TRANSACTION_DATE LIKE ? AND RECONCILE = ? LIMIT ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, currentDate + "%");
			stmt.setString(2, status.Earning.NOT_DONE.values);
			stmt.setInt(3, queue_size);
			LOG.debug("Query to fire " + query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				transactionList.add(rs.getString("TRANSACTION_ID"));
				String _paymentMode = rs.getString("PAYMENT_MODE");
				if (_paymentMode.equalsIgnoreCase(status.PaymentMode.ONLINE.value)) {
					currentOnlineEarning = currentOnlineEarning + Double.parseDouble(rs.getString("PAY_AMOUNT"));
				} else if (_paymentMode.equalsIgnoreCase(status.PaymentMode.OFFLINE.value)) {
					currentOfflineEarning = currentOfflineEarning + Double.parseDouble(rs.getString("PAY_AMOUNT"));
				}
			}
			currentProfit = currentOnlineEarning + currentOfflineEarning;
			onlineEarning = onlineEarning + currentOnlineEarning;
			offlineEarning = offlineEarning + currentOfflineEarning;
			totalEarning = totalEarning + currentTotalEarning;
			profit = profit + currentProfit;
			LOG.debug("updated offline earning is " + offlineEarning);
			LOG.debug("updated online earning is " + onlineEarning);
			LOG.debug("updated total earning is " + totalEarning);
			LOG.debug("updated profit earning is " + profit);
			stmt = null;
			if (earningEntryExists == true) {
				query = "UPDATE EARNING SET ONLINE_EARNING = ?, OFFLINE_EARNING = ?, TOTAL_EARNING = ?, PROFIT = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Double.toString(onlineEarning));
				stmt.setString(2, Double.toString(offlineEarning));
				stmt.setString(3, Double.toString(totalEarning));
				stmt.setString(4, Double.toString(profit));
				LOG.debug("Query to fire " + query);
				int result = stmt.executeUpdate();
				if (result != 0) {
					LOG.debug("Earning updated successfully");
				} else {
					LOG.debug("Earning update failed");
				}
			} else if (earningEntryExists == false) {
				query = "INSERT INTO EARNING (DATE, ONLINE_EARNING, OFFLINE_EARNING, TOTAL_EARNING, PROFIT) VALUES (?,?,?,?,?)";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, currentDate);
				stmt.setString(2, Double.toString(onlineEarning));
				stmt.setString(3, Double.toString(offlineEarning));
				stmt.setString(4, Double.toString(totalEarning));
				stmt.setString(5, Double.toString(profit));
				LOG.debug("Query to fire " + query);
				int result = stmt.executeUpdate();
				if (result != 0) {
					LOG.debug("Earning inserted successfully");
				} else {
					LOG.debug("Earning insertion failed");
				}
			}
			LOG.debug("Number of entries to update after reconcilation is "+transactionList.size());
			if (transactionList.size() != 0) {
				for (int i = 0; i < transactionList.size(); i++) {
					LOG.debug("Setting "+transactionList.get(i)+" as reconciled");
					query = "UPDATE CUSTOMER_BILL SET RECONCILE = ? WHERE TRANSACTION_ID = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, status.Earning.DONE.values);
					stmt.setString(2, transactionList.get(i));
					if(stmt.executeUpdate() != 0)
						LOG.debug("Successfully reconciled transaction "+transactionList.get(i));
					else
						LOG.error("Error while reconciling transaction "+transactionList.get(i));
				}
			}
			rs.close();
			stmt.close();
			conn.close();
			run();
		} catch (SQLException e) {
			LOG.error(e);
		}
	}

}
