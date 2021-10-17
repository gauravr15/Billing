package com.odin.dbManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.odin.constantValues.Constants;

public class DBCheck {
	
	Logger LOG = Logger.getLogger(DBCheck.class.getClass());
	
	public Connection dbCheck(Constants dbObject) {
		Connection conn = null;
		try {
			Class.forName(Constants.getDRIVER());
			String _URL = Constants.getIP()+Constants.getPORT()+Constants.getDBNAME();
			LOG.debug("DB URL : "+_URL);
			conn = DriverManager.getConnection(_URL,Constants.getUSER(),Constants.getPASS());
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error("Unable to connect to DB");
		}
		return conn;
	}
}
