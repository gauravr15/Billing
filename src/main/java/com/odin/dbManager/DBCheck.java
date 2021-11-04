package com.odin.dbManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.util.Throwables;

import com.odin.constantValues.DBConstants;

public class DBCheck {
	
	Logger LOG = Logger.getLogger(DBCheck.class.getClass());
	
	static boolean dbCheckPass = false;
	
	public boolean dbCheck() {
		String _URL = DBConstants.getIP()+DBConstants.getPORT()+DBConstants.getDBNAME();
		Connection conn =null;
		try {
			Class.forName(DBConstants.getDRIVER());
		} catch (ClassNotFoundException e) {
			LOG.error("Error initializing DB Driver");
			LOG.error(Throwables.toStringList(e));
		}
		try {
			conn = DriverManager.getConnection(_URL,DBConstants.getUSER(),DBConstants.getPASS());
			if(conn != null) {
				dbCheckPass=true;
			}
			try {
				conn.close();
				LOG.trace("Connection released");
			} catch (SQLException e) {
				LOG.error("Unable to close connection");
				LOG.error(Throwables.toStringList(e));
			}
			LOG.trace("dbCheckPass value is "+dbCheckPass);
		} catch (SQLException e) {
			LOG.error(Throwables.toStringList(e));
		}
		return dbCheckPass;
	}
}
