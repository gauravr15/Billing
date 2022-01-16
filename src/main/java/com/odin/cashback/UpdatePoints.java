package com.odin.cashback;

public abstract class UpdatePoints {
	
	private String customerID;
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public int getExpiredPoints() {
		return expiredPoints;
	}
	public void setExpiredPoints(int expiredPoints) {
		this.expiredPoints = expiredPoints;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	
	private String transactionID;
	private int expiredPoints;
	private String transactionType;
	abstract boolean removePoints();
	abstract boolean addPoints();
}
