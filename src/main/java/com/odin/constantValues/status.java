package com.odin.constantValues;

public class status {
	
	public enum ConfigParamName {
		BIRTHDAY_CHECK("BC"),
		SMS_SEND_TIME("SST");
		String values;
		ConfigParamName(String str){
			values = str;
		}
	}
	
	public enum CustomerConstants {
		NOT_AVAILABLE("NA"),
		INVALID_PHONE("INV"),
		CREATION_ERROR("CE"),
		INVALID_BIRTHDAY("INVB");
		public String values ;
		CustomerConstants(String string) {
			values = string;
		}
	}
	
	public enum BillState{
		BILL_CHECK("BC"),
		BILL_PRINT("BP");
		public String values;
		BillState(String string){
			values = string;
		}
	}
	
	public enum SMSConstants {
		BIRTHDAY("BD"),
		PROMOTIONAL("P"),
		SALARY("S"),
		BILLING("B");
		public String values;
		SMSConstants(String type) {
			values = type;
		}
	}
}
