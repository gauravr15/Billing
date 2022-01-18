package com.odin.smsThread;

import com.odin.billingHandler.CommitBill;

public interface SmsInterface {
	
	public boolean sendSMS(CommitBill customerObj);
	public boolean insertSMSLog();
}
