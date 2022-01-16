package com.odin.cashback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

import com.odin.Exceptions.ConfigException;
import com.odin.Exceptions.PointRemovalException;
import com.odin.configManager.ConfigParamMap;
import com.odin.configManager.QueueManager;
import com.odin.constantValues.status;
import com.odin.dbManager.DBConnectionAgent;

public class RemoveExpiredPoints extends UpdatePoints implements Runnable {

	Logger LOG = Logger.getLogger(RemoveExpiredPoints.class.getClass());
	
	private String customerID;
	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public int getExpiredPoints() {
		return expiredPoints;
	}

	public void setExpiredPoints(int expiredPoints) {
		this.expiredPoints = expiredPoints;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	
	private String transactionID;
	private int expiredPoints;
	private String transactionType;
	
	public void run() {
		int cooldownPeriod=0;
		int pointsValidity=0;
		LOG.debug("Going to check valid points for every user and add the points in customer_details table");
		if (!ConfigParamMap.params.containsKey("COOLDOWN_PERIOD")) {
			cooldownPeriod = 0;
			LOG.debug("COOLDOWN_PERIOD is not configured, taking 0 days as default");
		}
		else if (ConfigParamMap.params.containsKey("COOLDOWN_PERIOD")) {
			cooldownPeriod = Integer.parseInt(ConfigParamMap.params.get("COOLDOWN_PERIOD"));
			LOG.debug("Cooldown period is set as "+cooldownPeriod);
		}
		if (!ConfigParamMap.params.containsKey("POINTS_VALIDITY")) {
			pointsValidity = 365;
			LOG.debug("POINTS_VALIDITY is not configured, taking 365 days as default");
		} 
		else if (ConfigParamMap.params.containsKey("POINTS_VALIDITY")) {
			pointsValidity = Integer.parseInt(ConfigParamMap.params.get("POINTS_VALIDITY"));
			LOG.debug("Points validity is set as "+pointsValidity);
		}
		int queue_size = 0;
		if(QueueManager.queue.containsKey("POINTS")) {
			queue_size = QueueManager.queue.get("POINTS");
		}
		else {
			try {
				throw new ConfigException("PARAM_NAME POINTS IS NOT CONFIGURED IN CONFIG_PARAMS TABLE, using 200 as default");
			}
			catch(Exception e) {
				queue_size = 200;
				LOG.fatal(e);
			}
		}
		LOG.debug("Fetch size set as : "+queue_size);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.now();
		LocalDate _expiryDay = ld.plusDays(-pointsValidity-cooldownPeriod+1);
		String expiryDate = dtf.format(_expiryDay);
		LOG.debug("Going to expire entries before "+expiryDate);
		DBConnectionAgent dbObject = new DBConnectionAgent();
		Connection conn = dbObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int _fetchSize = 0;
		try {
			String query = "SELECT COUNT(*) FROM CUSTOMER_BILL WHERE TRANSACTION_DATE NOT BETWEEN ? AND NOW() AND CASHBACK_VALIDITY != ? ORDER BY TRANSACTION_DATE LIMIT ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, expiryDate);
			stmt.setString(2, status.CashbackValidity.EXPIRED.values);
			stmt.setInt(3, queue_size);
			LOG.debug("Query to fire : "+query);
			rs = stmt.executeQuery();
			if(rs.next()) {
				_fetchSize = Integer.parseInt(rs.getString(1));
			}
			LOG.debug("fetch size received is : "+_fetchSize);
			if(_fetchSize == 0) {
				try {
					LOG.debug("Going to sleep for an hour");
					Thread.sleep(3600000);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
				rs.close();
				stmt.close();
				conn.close();
				run();
			}
			else {
				query = "SELECT customer_bill.transaction_date, customer_bill.transaction_id , customer_bill.cashback_available, customer_details.id FROM customer_bill, customer_details WHERE customer_bill.customer_id = customer_details.id and TRANSACTION_DATE NOT BETWEEN ? AND NOW() AND CASHBACK_VALIDITY != ? ORDER BY TRANSACTION_DATE LIMIT ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, expiryDate);
				stmt.setString(2, status.CashbackValidity.EXPIRED.values);
				stmt.setInt(3, queue_size);
				LOG.debug("Query to fire : "+query);
				rs = stmt.executeQuery();
				while(rs.next()) {
					setCustomerID(rs.getString("id"));
					if(rs.getString("cashback_available")==null)
						setExpiredPoints(0);
					else
						setExpiredPoints(-Integer.parseInt(rs.getString("cashback_available")));
					setTransactionID(rs.getString("transaction_id"));
					setTransactionType(status.removePoints);
					LOG.debug("Expiring "+getExpiredPoints()+" points for customer_id:"+getCustomerID());
					if(removePoints()) {
						LOG.debug("Removed points successfully");
						String expireQuery = "UPDATE CUSTOMER_BILL SET CASHBACK_VALIDITY = ? WHERE TRANSACTION_ID = ?";
						PreparedStatement expireStmt = conn.prepareStatement(expireQuery);
						expireStmt.setString(1, status.CashbackValidity.EXPIRED.values);
						expireStmt.setString(2, getTransactionID());
						int _expireResult = expireStmt.executeUpdate();
						if(_expireResult != 0) {
							LOG.debug("Transaction expiry successful");
							expireStmt.close();
						}
						else {
							LOG.error("Failed to set the transaction as expired");
						}
					}
					else {
						try {
							throw new PointRemovalException("Failed to remove points for user "+getCustomerID());
						}
						catch(Exception e) {
							LOG.error(e);
						}
					}
				}
			}
			rs.close();
			stmt.close();
			conn.close();
			try {
				Thread.sleep(3600000);
			} catch (InterruptedException e) {
				LOG.error(e);
			}
			run();
		}
		catch (SQLException e) {
			LOG.error(e);
		}
	}
	
	boolean removePoints() {
		boolean result = false;
		CustomerDetailsPointModifier obj = new CustomerDetailsPointModifier();
		result = obj.updatePoints(this);
		return result;
	}
	
	boolean addPoints() {
		return false;
	}
}
