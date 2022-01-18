package com.odin.configManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.odin.dbManager.DBConnectionAgent;

public class ServiceMap {
	
	static Logger LOG = Logger.getLogger(ServiceMap.class.getClass());
	private static ServiceMap obj = new ServiceMap();
	static public HashMap<Integer, String> services;
	static public HashMap<String, Integer> service_charges;
	static public HashMap<String, String> service_code;
	static public HashMap<String, String> service_name;
	private ServiceMap() {}
	
	public static ServiceMap getInstance() {
		services = new HashMap<Integer, String>();
		service_charges = new HashMap<String, Integer>();
		service_code = new HashMap<String, String>();
		service_name = new HashMap<String, String>();
		DBConnectionAgent DBObject = new DBConnectionAgent();
		String query = "Select * from services;";
		Connection conn = DBObject.connectionAgent();
		try {
			LOG.debug("Query to fire : "+query);
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				services.put(rs.getInt("SERVICE_ID"), rs.getString("SERVICE_NAME"));
				service_charges.put(rs.getString("SERVICE_NAME"), rs.getInt("PRICE"));
				service_code.put(rs.getString("SERVICE_NAME"), rs.getString("SERVICE_CODE"));
				service_name.put(rs.getString("SERVICE_CODE"), rs.getString("SERVICE_NAME"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		try {
			conn.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
		return obj;
	}
}
