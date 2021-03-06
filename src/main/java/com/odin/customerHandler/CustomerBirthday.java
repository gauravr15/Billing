package com.odin.customerHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.apache.log4j.Logger;

import com.odin.billingHandler.CommitBill;
import com.odin.configManager.ConfigParamMap;
import com.odin.constantValues.status.ConfigParamName;
import com.odin.constantValues.status.SMSConstants;
import com.odin.dbManager.DBConnectionAgent;
import com.odin.smsThread.SmsInterface;

public class CustomerBirthday implements Runnable,SmsInterface {

	Logger LOG = Logger.getLogger(CustomerBirthday.class.getClass());
	
	String messageBody = null;
	String phoneNumber = null;

	public void run() {
		LOG.trace("Inside customer birthday.");
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = null;
		int smsTime = 0;
		if (ConfigParamMap.params.get(ConfigParamName.SMS_SEND_TIME.toString()) == null) {
			LOG.fatal("SMS_SEND_TIME is not configured in CONFIG_PARAMS table, so setting value as 10 hours");
			smsTime = 10;
		} else if (Integer.parseInt(ConfigParamMap.params.get(ConfigParamName.SMS_SEND_TIME.toString())) > 23) {
			LOG.fatal("Improper configuration present for SMS_SEND_TIME, setting it to 10 hours");
		} else {
			smsTime = Integer.parseInt(ConfigParamMap.params.get(ConfigParamName.SMS_SEND_TIME.toString()));
			LOG.debug("Birthday SMS will be sent at " + smsTime);
		}
		LOG.debug("Birthday SMS will be sent daily at " + smsTime + " hours");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("kk");
		int sysTime = Integer.parseInt(sdf.format(date));
		if (sysTime >= smsTime) {
			LOG.debug("System time is grater, so sending birthday SMS");
			try {
				String[] smsDate = new String[0];
				String lastSmsDate = null;
				query = "SELECT * FROM SMS_LOG WHERE TYPE = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, SMSConstants.BIRTHDAY.values);
				rs = stmt.executeQuery();
				int count = 0;
				while (rs.next()) {
					lastSmsDate = rs.getString("DATE");
					count = count+1;
				}
				if(count > 0) {
				smsDate = lastSmsDate.split(" ");
				lastSmsDate = smsDate[0];
				LOG.debug("Last SMS for type " + SMSConstants.BIRTHDAY.values + " was sent on : " + lastSmsDate);
				LocalDate ld = LocalDate.parse(lastSmsDate);
				LocalDate currentDate = LocalDate.now();
				LOG.debug("Current system date is : " + currentDate);
				if (currentDate.compareTo(ld) > 0) {
					LOG.debug("Fetching user data from customer data table");
					LOG.debug("This module is under development and will be deployed soon");
					/* Call the method that will fetch customer data from customer_info table */
				} else {
					LOG.debug("Birthday sms already sent on : " + lastSmsDate);
					LOG.debug("Going to sleep for 1 hour");
					try {
						Thread.sleep(3600000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						LOG.error(e);
					}
					try {
						rs.close();
						stmt.close();
						conn.close();
					} catch (SQLException e) {
						LOG.error(e);
					}
					run();
				}
				}
				else {
					LOG.fatal("No entry found in SMS_LOG table for type "+SMSConstants.BIRTHDAY.values);
					LOG.debug("Inserting entry for type "+SMSConstants.BIRTHDAY.values);
				}
			} catch (SQLException e) {
				LOG.error(e);
			}
		} else {
			LOG.debug("Going to sleep for 1 hour");
			try {
				Thread.sleep(3600000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				LOG.error(e);
			}
			try {
				conn.close();
			} catch (SQLException e) {
				LOG.error(e);
			}
			run();
		}
	}

	public void sendSMS() {

	}

	@Override
	public boolean sendSMS(CommitBill customerObj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertSMSLog() {
		// TODO Auto-generated method stub
		return false;
	}
}
