package com.odin.configManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.odin.dbManager.DBConnectionAgent;

public class QueueManager {
	
	static Logger LOG = Logger.getLogger(QueueManager.class.getClass());
	
	public static HashMap<String, Integer> queue;
	
	static void initQueue() {
		LOG.debug("Initializing queue");
		queue = new HashMap<String, Integer>();
		DBConnectionAgent dbObj = new DBConnectionAgent();
		Connection conn = dbObj.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM QUEUE_CONTROL";
		try {
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next()) {
				queue.put(rs.getString("MODULE"), Integer.parseInt(rs.getString("QUEUE_SIZE")));
			}
			LOG.debug("queue initialization successful");
		} catch (SQLException e) {
			LOG.error(e);
		}
		try {
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error(e);
		}
	}
}
