package com.odin.constantValues;

public enum CustomerConstants {
	NOT_AVAILABLE("NA"),
	INVALID_PHONE("INV");
	public String values ;
	CustomerConstants(String string) {
		values = string;
	}
}
