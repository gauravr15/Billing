package com.odin.employeeHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.odin.dbManager.DBConnectionAgent;
import com.odin.pageController.LoginController;

public class EmployeeAttendance {
	
	Logger LOG = Logger.getLogger(EmployeeAttendance.class.getClass());
	
	String user = null;
	int emp_id = 0;

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean attendanceCheck(LoginController loginObject) {
		LOG.trace("Inside attendance check");
		boolean check = false;
		setUser(loginObject.getUser());
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM EMPLOYEE WHERE NAME = ?";
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, getUser());
			LOG.debug("Checking attendance of "+getUser());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
		try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
		int count = 0;
		try {
			while(rs.next()) {
				count = count+1;
				setEmp_id(Integer.parseInt(rs.getString("ID")));
			}
		} catch (SQLException e) {
			LOG.error(e);
		}
		query = "SELECT * FROM EMPLOYEE_ATTENDANCE WHERE ID = ? ORDER BY IN_TIME DESC LIMIT 1";
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, Integer.toString(getEmp_id()));
		} catch (SQLException e) {
			LOG.error(e);
		}
		return check;
	}
}
