package com.odin.constantValues;

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
