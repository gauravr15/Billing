package com.odin.supportingThreads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.odin.constantValues.DBConstants;

public class BirthdayThread implements Runnable{
	
	static Logger LOG = Logger.getLogger(BirthdayThread.class.getClass());
	
	static BirthdayThread birthdayObject = new BirthdayThread();
	
	static Connection CONN= null;
	public static Connection getConn() {
		return CONN;
	}

	public static void setConn(Connection conn) {
		BirthdayThread.CONN = conn;
	}

	static {
		try {
			Class.forName(DBConstants.getDRIVER());
		} catch (ClassNotFoundException e) {
			LOG.error("Unable to get driver name");
		}
		String _URL = DBConstants.getIP()+DBConstants.getPORT()+DBConstants.getDBNAME();
		try {
			CONN = DriverManager.getConnection(_URL,DBConstants.getUSER(),DBConstants.getPASS());
			BirthdayThread.setConn(CONN);
		} catch (SQLException e) {
			LOG.error("Unable to connect to DB");
		}
	}
	
	private BirthdayThread() {
		
	}
	
	public static BirthdayThread getInstance() {
		return birthdayObject;
	}
	
	public void run() {
		BirthdayCheck();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.error("Error while starting birthdayThread");
		}
		run();
	}
	
	public void BirthdayCheck() {
		LOG.debug(Thread.currentThread().getName()+"   "+" I am birthday thread");
	}
}
