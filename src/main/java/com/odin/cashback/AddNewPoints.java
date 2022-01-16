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

public class AddNewPoints extends UpdatePoints implements Runnable{
	
	Logger LOG = Logger.getLogger(AddNewPoints.class.getClass());
	
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
		LOG.debug("Inside add new points class");
		int cooldownPeriod= ConfigParamMap.params.containsKey("COOLDOWN_PERIOD") == true?Integer.parseInt(ConfigParamMap.params.get("COOLDOWN_PERIOD")):0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.now().plusDays(-cooldownPeriod);
		String cooldownDate = dtf.format(ld);
		LOG.debug("Making points state from Not cool down to valid"+cooldownDate);
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
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn = dbObj.connectionAgent();
		String query = "select COUNT(*) from customer_bill where transaction_date like ? and cashback_validity = ?";
		LOG.debug("query to fire "+query);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int _fetchSize = 0;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, cooldownDate+"%");
			stmt.setString(2, status.CashbackValidity.NOT_COOLDOWN.values);
			rs = stmt.executeQuery();
			while(rs.next()) {
				_fetchSize = Integer.parseInt(rs.getString(1));
			}
			LOG.debug("fetch size received is : "+_fetchSize);
			if(_fetchSize == 0 ) {
				LOG.debug("going to sleep for a day");
				try {
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
				query = "SELECT customer_bill.transaction_date, customer_bill.transaction_id , customer_bill.cashback_received, customer_details.id FROM customer_bill, customer_details WHERE customer_bill.customer_id = customer_details.id and transaction_date like ? AND CASHBACK_VALIDITY = ? ORDER BY TRANSACTION_DATE LIMIT ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, cooldownDate+"%");
				stmt.setString(2, status.CashbackValidity.NOT_COOLDOWN.values);
				stmt.setInt(3, queue_size);
				LOG.debug("Query to fire : "+query);
				rs = stmt.executeQuery();
				while(rs.next()) {
					setCustomerID(rs.getString("id"));
					if(rs.getString("cashback_received")==null)
						setExpiredPoints(0);
					else
						setExpiredPoints(Integer.parseInt(rs.getString("cashback_received")));
					setTransactionID(rs.getString("transaction_id"));
					setTransactionType(status.addCooldownPoints);
					LOG.debug("Adding "+getExpiredPoints()+" cooled down points for customer_id:"+getCustomerID());
					if(addPoints()) {
						LOG.debug("added points successfully");
						String addPointsQuery = "UPDATE CUSTOMER_BILL SET CASHBACK_VALIDITY = ? WHERE TRANSACTION_ID = ?";
						PreparedStatement expireStmt = conn.prepareStatement(addPointsQuery);
						expireStmt.setString(1, status.CashbackValidity.VALID.values);
						expireStmt.setString(2, getTransactionID());
						int _addPointsResult = expireStmt.executeUpdate();
						if(_addPointsResult != 0) {
							LOG.debug("Transaction points added successful");
							expireStmt.close();
						}
						else {
							LOG.error("Failed to set the transaction as valid");
						}
					}
					else {
						try {
							throw new PointRemovalException("Failed to add new points for user "+getCustomerID());
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
		} catch (SQLException e) {
			LOG.error(e);
		}
	}
	
	boolean removePoints() {
		return false;
	}
	
	boolean addPoints() {
		boolean result = false;
		CustomerDetailsPointModifier obj = new CustomerDetailsPointModifier();
		result = obj.updatePoints(this);
		return result;
	}

}
