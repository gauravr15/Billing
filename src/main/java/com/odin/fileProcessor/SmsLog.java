package com.odin.fileProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

import com.odin.smsThread.BillingSMS;

public class SmsLog {
	
	Logger LOG = Logger.getLogger(SmsLog.class.getClass());
	
	String response;
	String responseCode;
	String number;
	String transTime;
	String threadName;
	String customer_id;
	
	public boolean writeLog(BillingSMS smsObj) {
		LOG.debug("Writing tlog");
		boolean taskPerformed = false;
		String header = "Time|Thread|Phone_number|Customer_id|Response_code|Response";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime ldt = LocalDateTime.now();
		String _currentTime = dtf.format(ldt);
		File file = new File("../TOMCAT_LOGS/SMSLogs/Smslog_" + _currentTime + ".txt");
		try {
			boolean _fileExists = file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			if (_fileExists == true) {
				fw.write(header + "\n\n");
			}
			LOG.debug("Thread id is: " + smsObj.getThreadName());
			LOG.debug("Transaction time is: " + smsObj.getTransTime());
			LOG.debug("Customer id is: " + smsObj.getCustomer_id());
			LOG.debug("Response code is: " + smsObj.getResponseCode());
			LOG.debug("Response is: " + smsObj.getResponse());
			String transactionLogStr = smsObj.getTransTime() + "|" + smsObj.getThreadName() + "|"
					 + smsObj.getCustomer_mobile()+ "|"+ smsObj.getCustomer_id() + "|"
					+ smsObj.getResponseCode() + "|"+ smsObj.getResponse()+ "\n";
			fw.write(transactionLogStr);
			fw.close();
			taskPerformed = true;
		} catch (IOException e) {
			LOG.error(e);
		}
		return taskPerformed;
	}
}
