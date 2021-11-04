package com.odin.customerHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.util.Throwables;

import com.odin.constantValues.SMSConstants;
import com.odin.dbManager.DBConnectionAgent;

public class CustomerBirthday implements Runnable{
	
	static Logger LOG = Logger.getLogger(CustomerBirthday.class.getClass());
	
	
	public void run() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.now();
		String query = "SELECT * FROM SMS_LOG WHERE TYPE = ?";
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(Throwables.toStringList(e));
		}
		try {
			stmt.setString(1, SMSConstants.BIRTHDAY.values().toString());
		} catch (SQLException e) {
			LOG.error(Throwables.toStringList(e));
		}
		try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			LOG.error(Throwables.toStringList(e));
		}
		int count = 0;
		try {
			while(rs.next()) {
				count = count+1;
			}
		} catch (SQLException e) {
			LOG.error(Throwables.toStringList(e));
		}
		if(count == 0) {
			query = "INSERT INTO SMS_LOG VALUES (?,?)";
			try {
				stmt = conn.prepareStatement(query);
			} catch (SQLException e) {
				LOG.error(Throwables.toStringList(e));
			}
			try {
				stmt.setString(1, dtf.format(ldt).toString());
				stmt.setString(2, SMSConstants.BIRTHDAY.values().toString());
			} catch (SQLException e) {
				LOG.error(Throwables.toStringList(e));
			}
		}
	}
}
