package com.odin.constantValues;

public enum ConfigParamName {
	BIRTHDAY_CHECK("BC"),
	SMS_SEND_TIME("SST");
	String values;
	ConfigParamName(String str){
		values = str;
	}
}
