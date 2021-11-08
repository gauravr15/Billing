package com.odin.configManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.odin.dbManager.DBConnectionAgent;

public class ConfigParamMap {
	
	public static HashMap<String, String> params;
	static Logger LOG = Logger.getLogger(ConfigParamMap.class.getClass());
	
	static void paramValues() {
		LOG.trace("Initializing config manager");
		params = new HashMap<String,String>();
		DBConnectionAgent DBAgentObj = new DBConnectionAgent();
		Connection conn = DBAgentObj.connectionAgent();
		PreparedStatement stmt = null;
	    try {
	    	String query = "Select * from config_params";
			stmt = conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
	    ResultSet rs = null;
	    try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
	    try {
			while(rs.next()) {
				params.put(rs.getString("param_name"),rs.getString("param_value"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
	    }
	    try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
	    try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
	    try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
	}
}
