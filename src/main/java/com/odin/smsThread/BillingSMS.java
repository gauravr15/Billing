package com.odin.smsThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.odin.billingHandler.CommitBill;
import com.odin.configManager.ConfigParamMap;
import com.odin.configManager.ServiceMap;
import com.odin.dbManager.DBConnectionAgent;
import com.odin.fileProcessor.SmsLog;

public class BillingSMS implements SmsInterface{
	
	Logger LOG = Logger.getLogger(BillingSMS.class.getClass());
	
	public String getCustomer_mobile() {
		return customer_mobile;
	}

	public void setCustomer_mobile(String customer_mobile) {
		this.customer_mobile = customer_mobile;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	
	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	
	String customer_mobile;
	String message;
	String items;
	String customer_name;
	String total;
	String response;
	String responseCode;
	String threadName;
	String transTime;
	String customer_id;
	String smsType;
	

	@Override
	public boolean sendSMS(CommitBill customerObj) {
		boolean taskPerformed = false;
		customer_name = customerObj.getCustomerName().toLowerCase();
		setCustomer_name(customer_name.substring(0,1).toUpperCase() + customer_name.substring(1, customer_name.length()).toLowerCase()); 
		setCustomer_mobile(customerObj.getCustomerPhone());
		setThreadName(customerObj.getThreadId());
		setTransTime(customerObj.getTransTime());
		setCustomer_id(customerObj.getCheckoutUser());
		setItems(customerObj.getItemList());
		setMessage(customerObj.getBillingMessage());
		setTotal(customerObj.getPayAmount());
		setSmsType(customerObj.getSmsType());
		LOG.debug("Customer name is "+getCustomer_name());
		LOG.debug("customer phone number is "+getCustomer_mobile());
		LOG.debug("Items list is "+ getItems());
		String[] itemName = items.split(",");
		items = "";
		for(int i = 0;i< itemName.length ;i++) {
			items = items+ServiceMap.service_name.get(itemName[i])+",";
		}
		items = items.substring(0, items.length()-1);
		setItems(items.replace("_", " "));
		message = message.replace("$1", getCustomer_name());
		message = message.replace("$2", getItems());
		message = message.replace("$3", getTotal());
		LOG.debug("Message to be sent is "+ getMessage());
		try {
			taskPerformed = sendMessage();
		} catch (IOException e) {
			LOG.error(e);
		}
		if(taskPerformed == true)
			LOG.debug("SMS sent successfully");
		else
			LOG.error("SMS sending failed");
		if(insertSMSLog()) {
			taskPerformed = true;
			LOG.debug("SMS has been inserted in the send_sms table");
		}
		else {
			taskPerformed = false;
			LOG.error("Failed to insert data in send_sms table");
		}
		return taskPerformed;
	}

	@Override
	public boolean insertSMSLog() {
		LOG.debug("Inserting sms log in send sms table");
		boolean task_performed = false;
		String query = "INSERT INTO SEND_SMS VALUES (?,?,?,?,?)";
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn = dbObj.connectionAgent();
		String _message = getMessage();
		_message = _message.replace("+", " ").replace("%2C", ",");
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, getCustomer_id());
			stmt.setString(2, getTransTime());
			stmt.setString(3, getCustomer_mobile());
			stmt.setString(4, getSmsType());
			stmt.setString(5, _message);
			task_performed = stmt.executeUpdate() != 0? true:false;
		} catch (SQLException e) {
			LOG.error(e);
		}
		return task_performed;
	}
	
	public boolean sendMessage() throws IOException
	{
		boolean taskPerformed = false;
		String apiKey= ConfigParamMap.params.get("SMS_API_KEY");
		//String sendId="FSTSMS";
		//important step...
		message=URLEncoder.encode(message, "UTF-8");
		String language="english";
		
		String route="q";
		
		String myUrl="https://www.fast2sms.com/dev/bulkV2?authorization="+apiKey+"&route="+route+"&message="+getMessage()+"&language="+language+"&flash=0&numbers="+getCustomer_mobile();
		
		//sending get request using java..
		
		URL url=new URL(myUrl);
		
		HttpsURLConnection con=(HttpsURLConnection)url.openConnection();
		
		
		con.setRequestMethod("GET");
		
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("cache-control", "no-cache");
		LOG.debug("Wait..............");
		
		int code=con.getResponseCode();
		this.responseCode = Integer.toString(code);
		LOG.debug("Response code : "+code);
		setResponseCode(Integer.toString(code));
		taskPerformed = code == 200 ? true:false;
		StringBuffer response=new StringBuffer();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		while(true)
		{
			String line=br.readLine();
			if(line==null)
			{
				break;
			}
			response.append(line);
		}
		setResponse(response.toString());
		LOG.debug(response);
		SmsLog smsLogObj = new SmsLog();
		if(!smsLogObj.writeLog(this)) {
			taskPerformed = false;
		}
		LOG.debug("task performed is : "+taskPerformed);
		return taskPerformed;
		
	}

}
