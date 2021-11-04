package com.odin.dbManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.util.Throwables;

import com.odin.constantValues.DBConstants;

public class DBConnectionAgent {
	
	static Logger LOG = Logger.getLogger(DBConnectionAgent.class.getClass());
	
	public Connection CONN = null;
	public static String DB_DRIVER =DBConstants.getDRIVER();
	public static String DB_URL = DBConstants.getIP()+DBConstants.getPORT()+DBConstants.getDBNAME();
	public static String DB_USER = DBConstants.getUSER();
	public static String DB_PASS =DBConstants.getPASS();
	

	public Connection connectionAgent() {
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			LOG.error("Unable to load DB Driver");
			LOG.error(Throwables.toStringList(e));
		}
		try {
			CONN = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
		} catch (SQLException e) {
			LOG.error("Unable to establish DB connection");
			LOG.error(Throwables.toStringList(e));
		}
		return CONN;
	}
}
