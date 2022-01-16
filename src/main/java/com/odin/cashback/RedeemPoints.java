package com.odin.cashback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.odin.billingHandler.CommitBill;
import com.odin.constantValues.status;
import com.odin.dbManager.DBConnectionAgent;

public class RedeemPoints extends UpdatePoints{
	
	private String transactionId;
	private String cashbackPoints;
	private String customerID;
	private int expiredPoints;
	
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
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCashbackPoints() {
		return cashbackPoints;
	}

	public void setCashbackPoints(String cashbackPoints) {
		this.cashbackPoints = cashbackPoints;
	}

	Logger LOG = Logger.getLogger(RedeemPoints.class.getClass());
	
	public boolean cashbackDiscount(CommitBill customerPointsObj) {
		boolean taskPerformed = false;
		LOG.debug("Inside cashback discount");
		setCustomerID(customerPointsObj.getCheckoutUser());
		setExpiredPoints(-Integer.parseInt(customerPointsObj.getCheckoutDiscount()));
		String query = "SELECT POINTS FROM CUSTOMER_DETAILS WHERE ID = ?";
		int redeemPoints = Integer.parseInt(customerPointsObj.getCheckoutDiscount());
		int availablePoints = 0;
		HashMap<String,Integer> customerTransaction = new HashMap<String,Integer>();
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn = dbObj.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			LOG.debug("Checking available points for customer id : "+customerPointsObj.getCheckoutUser());
			stmt = conn.prepareStatement(query);
			stmt.setString(1, customerPointsObj.getCheckoutUser());
			rs = stmt.executeQuery();
			while(rs.next()) {
				availablePoints = Integer.parseInt(rs.getString("POINTS"));
			}
			if(availablePoints >= redeemPoints) {
				LOG.debug("Going to redeem points");
				query = "SELECT transaction_id, cashback_available FROM CUSTOMER_BILL WHERE CUSTOMER_ID = ? AND CASHBACK_VALIDITY = ? ORDER BY TRANSACTION_ID ASC";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, customerPointsObj.getCheckoutUser());
				stmt.setString(2, status.CashbackValidity.VALID.values);
				rs = stmt.executeQuery();
				while(rs.next()) {
					if(redeemPoints > Integer.parseInt(rs.getString("cashback_available")) && rs.getString("cashback_available") != null && Integer.parseInt(rs.getString("cashback_available")) > 0 && redeemPoints != 0) {
						redeemPoints = redeemPoints - Integer.parseInt(rs.getString("cashback_available"));
						customerTransaction.put(rs.getString("transaction_id"), 0);
					}
					else if(redeemPoints < Integer.parseInt(rs.getString("cashback_available")) && rs.getString("cashback_available") != null && Integer.parseInt(rs.getString("cashback_available")) > 0 && redeemPoints != 0) {
						redeemPoints = Integer.parseInt(rs.getString("cashback_available")) - redeemPoints;
						customerTransaction.put(rs.getString("transaction_id"), redeemPoints);
					}
					else if(redeemPoints == 0) {
						break;
					}
				}
				Iterator<?> it = customerTransaction.entrySet().iterator();
			    while (it.hasNext()) {
			        @SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry)it.next();
			        setTransactionId(pair.getKey().toString());
			        setCashbackPoints(pair.getValue().toString());
			        if(updateCustomerBill() == true) {
			        	LOG.debug("Successfully used points from transaction id : "+pair.getKey().toString());
			        	taskPerformed  = true;
			        }
			        else {
			        	LOG.error("Failed to use points from transaction id : "+pair.getKey().toString());
			        	taskPerformed = false;
			        	break;
			        }
			        it.remove();
			    }
			}
			else {
				LOG.debug("Unable to redeem points since redeem points is greater than available points");
				taskPerformed = false;
			}
			if(taskPerformed == true) {
				if(removePoints()== true)
					LOG.debug("Successfully updated customer_details table");
				else
					LOG.error("Failed to update customer_details table");
			}
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
		return taskPerformed;
	}
	
	boolean updateCustomerBill() {
		boolean updatePerformed = false;
		String query = "UPDATE CUSTOMER_BILL SET CASHBACK_AVAILABLE = ? WHERE TRANSACTION_ID = ?";
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn = dbObj.connectionAgent();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, getCashbackPoints());
			stmt.setString(2, getTransactionId());
			updatePerformed = stmt.executeUpdate() != 0 ? true:false;
		} catch (SQLException e) {
			LOG.error(e);
		}
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		return updatePerformed;
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
