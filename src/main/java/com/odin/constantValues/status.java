package com.odin.constantValues;

public class status {
	
	public static final String removePoints = "REMP";
	public static final String addCooldownPoints = "ADDCP";
	
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
		BILL_PRINT("BP"),
		BILL_INVALID("BI");
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
	
	public enum TLOGConstants{
		CUSTOMERID("CST"),
		PRODUCTS("PRD"),
		TOTAL("TOT"),
		DISCOUNT("DIS"),
		PAYMENT("PAY"),
		CASHIER("CR");
		public String values;
		TLOGConstants(String string){
			values = string;
		}
	}
	
	public enum CashBackConstants{
		NOTUSED("NU"),
		PARTIALLYUSED("PU"),
		USED("U"),
		EXPIRED("EXP");
		public String values;
		CashBackConstants(String string){
			values = string;
		}
	}
	
	public enum CashbackValidity{
		NOT_APPLICABLE("NA"),
		NOT_COOLDOWN("NCD"),
		VALID("V"),
		EXPIRED("EXP");
		public String values;
		CashbackValidity(String string){
			values = string;
		}
	}
}
