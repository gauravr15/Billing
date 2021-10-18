package com.odin.dbManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.odin.constantValues.DBConstants;

public class DBCheck {
	
	Logger LOG = Logger.getLogger(DBCheck.class.getClass());
	
	public Connection dbCheck(DBConstants dbObject) {
		Connection conn = null;
		try {
			Class.forName(DBConstants.getDRIVER());
			String _URL = DBConstants.getIP()+DBConstants.getPORT()+DBConstants.getDBNAME();
			LOG.debug("DB URL : "+_URL);
			conn = DriverManager.getConnection(_URL,DBConstants.getUSER(),DBConstants.getPASS());
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error("Unable to connect to DB");
		}
		return conn;
	}
}
