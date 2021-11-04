package com.odin.constantValues;

public enum SMSConstants {
	BIRTHDAY("BD"),
	PROMOTIONAL("P"),
	SALARY("S"),
	BILLING("B");
	String values;
	SMSConstants(String type) {
		values = type;
	}
}
