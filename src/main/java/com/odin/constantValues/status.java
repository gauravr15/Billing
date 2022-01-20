package com.odin.constantValues;

public class status {
	
	public static final String removePoints = "REMP";
	public static final String addCooldownPoints = "ADDCP";
	public static final String smsSuccess = "1";
	public static final String smsFailure = "0";
	public static final String smsDisabled = "-1";
	
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
		SMS_SENT("SMS"),
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
	
	public enum ProductEntry{
		MANUAL("MANUAL"),
		AUTOMATIC("AUTO"),
		SUCCESS("SUCCESS"),
		FAIL("FAIL");
		public String values;
		ProductEntry(String str){
			values = str;
		}
	}
	
	public enum CreateUser{
		INSERTED("INS"),
		INVALID("INV"),
		INTERNAL_ERROR("IE"),
		INCORRECT_PASS("ICP");
		public String values;
		CreateUser(String str){
			values = str;
		}
	}
}
