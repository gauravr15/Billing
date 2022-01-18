package com.odin.cashback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.odin.dbManager.DBConnectionAgent;

public class CustomerDetailsPointModifier {
	
	Logger LOG = Logger.getLogger(CustomerDetailsPointModifier.class.getClass());
	
	public boolean updatePoints(UpdatePoints upObj) {
		boolean result = false;
		LOG.debug("customer id is "+upObj.getCustomerID());
		LOG.debug("Points to delete is : "+upObj.getExpiredPoints());
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn =dbObj.connectionAgent();
		String query = "SELECT POINTS FROM CUSTOMER_DETAILS WHERE ID = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, upObj.getCustomerID());
			rs = stmt.executeQuery();
			while(rs.next()) {
				int _totalPoints = Integer.parseInt(rs.getString("POINTS"));
				int _updatedPoints = _totalPoints + upObj.getExpiredPoints();
				query = "UPDATE CUSTOMER_DETAILS SET POINTS = ? WHERE ID = ?";
				LOG.debug("query to fire : "+query);
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Integer.toString(_updatedPoints));
				stmt.setString(2, upObj.getCustomerID());
				int _result = stmt.executeUpdate();
				if(_result != 0) {
					result = true;
				}
				else {
					result = false;
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		return result;
	}
}
