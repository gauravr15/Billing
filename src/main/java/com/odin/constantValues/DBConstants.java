package com.odin.constantValues;

public class DBConstants {
	
	
	static DBConstants DBConfigObject = new DBConstants();
	
	static String DRIVER = null;
	static String IP = null;
	static String PORT = null;
	static String DBNAME = null;
	static String USER = null;
	static String PASS = null;
	
	public static String getDRIVER() {
		return DRIVER;
	}

	public static void setDRIVER(String dRIVER) {
		DRIVER = dRIVER;
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public static String getPORT() {
		return PORT;
	}

	public static void setPORT(String pORT) {
		PORT = pORT;
	}

	public static String getDBNAME() {
		return DBNAME;
	}

	public static void setDBNAME(String dBNAME) {
		DBNAME = dBNAME;
	}

	public static String getUSER() {
		return USER;
	}

	public static void setUSER(String uSER) {
		USER = uSER;
	}

	public static String getPASS() {
		return PASS;
	}

	public static void setPASS(String pASS) {
		PASS = pASS;
	}

	
	private DBConstants() {
		
	}
	
	public static DBConstants getInstance() {
		return DBConfigObject;
	}
	
}
