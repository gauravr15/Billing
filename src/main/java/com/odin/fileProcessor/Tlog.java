package com.odin.fileProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

import com.odin.billingHandler.CommitBill;
import com.odin.constantValues.status.TLOGConstants;

public class Tlog {

	Logger LOG = Logger.getLogger(Tlog.class.getClass());

	public boolean write(CommitBill transactionRecord) {
		LOG.debug("Writing tlog");
		boolean flag = false;
		String header = "Time|Thread|Customer_ID|Purchase_Info|Total|Discount|Payment_Amount|Sms_Task|Cashier_ID";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime ldt = LocalDateTime.now();
		String _currentTime = dtf.format(ldt);
		File file = new File("../TOMCAT_LOGS/TLOG/tlog_" + _currentTime + ".txt");
		try {
			boolean _fileExists = file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			if (_fileExists == true) {
				fw.write(header + "\n\n");
			}
			LOG.debug("Thread id is: " + transactionRecord.getThreadId());
			LOG.debug("Transaction time is: " + transactionRecord.getTransTime());
			LOG.debug("Customer id is: " + transactionRecord.getCheckoutUser());
			LOG.debug("Purchases is: " + transactionRecord.getPurchases());
			LOG.debug("Total amount is: " + transactionRecord.getTotal());
			LOG.debug("Total amount is: " + transactionRecord.getCheckoutDiscount());
			LOG.debug("Payable amount is: " + transactionRecord.getPayAmount());
			LOG.debug("Cashier name is: " + transactionRecord.getCashierId());
			String transactionLogStr = transactionRecord.getTransTime() + "|" + transactionRecord.getThreadId() + "|"
					+ TLOGConstants.CUSTOMERID.values + "=" + transactionRecord.getCheckoutUser() + "|"
					+ TLOGConstants.PRODUCTS.values + "=" + transactionRecord.getPurchases() + "|"
					+ TLOGConstants.TOTAL.values + "=" + transactionRecord.getTotal() + "|"
					+ TLOGConstants.DISCOUNT.values + "=" + transactionRecord.getCheckoutDiscount() + "|"
					+ TLOGConstants.PAYMENT.values + "=" + transactionRecord.getPayAmount() + "|"
					+ TLOGConstants.SMS_SENT.values+"="+transactionRecord.getSmsTask()+"|"
					+ TLOGConstants.CASHIER.values+"="+transactionRecord.getCashierId() + "\n";
			fw.write(transactionLogStr);
			fw.close();
			flag = true;
		} catch (IOException e) {
			LOG.error(e);
		}
		return flag;
	}
}