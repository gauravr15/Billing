package com.odin.employeeHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Logger;

import com.odin.dbManager.DBConnectionAgent;
import com.odin.pageController.LoginController;

public class EmployeeAttendance {
	
	Logger LOG = Logger.getLogger(EmployeeAttendance.class.getClass());
	
	String user = null;
	int emp_id = 0;
	String inTime = null;

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

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
		setEmp_id(loginObject.getEmpId());
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ld = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM EMPLOYEE_ATTENDANCE WHERE ID = ?");
			stmt.setString(1, Integer.toString(getEmp_id()));
		} catch (SQLException e) {
			LOG.error(e);
		}
		try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			LOG.error(e);
		}
		int count = 0;
		try {
			while(rs.next()) {
				count = count + 1;
			}
		} catch (SQLException e) {
			LOG.error(e);
		}
		if(count == 0) {
			try {
				stmt = conn.prepareStatement("INSERT INTO EMPLOYEE_ATTENDANCE (id, in_time) VALUES (?,?)");
				ld = LocalDateTime.now();
				stmt.setString(1, Integer.toString(loginObject.getEmpId()));
				stmt.setString(2, dtf.format(ld));
				stmt.executeUpdate();
				LOG.debug("Inserted data in employee attendance table.");
				check = true;
			} catch (SQLException e) {
				LOG.error(e);
			}
			finally {
				try {
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					LOG.error(e);
				}
			}
		}
		else {
			try {
				setInTime(getLastAttendance());
				LOG.debug("Last attendance for employee "+this.getUser()+" is "+getInTime());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date existingAttendance = sdf.parse(getInTime());
					Date currentDateTime = sdf.parse(LocalDateTime.now().toString());
					if(currentDateTime.compareTo(existingAttendance) > 0) {
						LOG.debug("No attendance available for "+getUser()+" on "+sdf.format(currentDateTime));
						insertNewAttendance();
					}
					else if(currentDateTime.compareTo(existingAttendance) < 0) {
						deleteIncorrectAttendance();
						insertNewAttendance();
					}
					else {
						LOG.debug("Employee attendance already logged for today");
					}
				} catch (ParseException e) {
					LOG.error(e);
				}
			} catch (SQLException e) {
				LOG.error(e);
			}
			finally {
				try {
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					LOG.error(e);
				}
			}
		}
		return check;
	}

	void deleteIncorrectAttendance() throws SQLException {
		LOG.debug("Deleting incorrect attendance for "+getUser());
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement("DELETE FROM EMPLOYEE_ATTENDANCE WHERE id = ? and in_time = ?");
		stmt.setString(1, Integer.toString(getEmp_id()));
		stmt.setString(2, getInTime());
		stmt.executeUpdate();
		stmt.close();
		conn.close();
	}

	void insertNewAttendance() throws SQLException {
		LOG.debug("Inserting new attendance for "+getUser());
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		PreparedStatement stmt = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ld = LocalDateTime.now();
		stmt = conn.prepareStatement("INSERT INTO EMPLOYEE_ATTENDANCE (id, in_time) values (?,?)");
		stmt.setString(1, Integer.toString(getEmp_id()));
		stmt.setString(2, dtf.format(ld));
		stmt.executeUpdate();
		stmt.close();
		conn.close();
	}

	String getLastAttendance() throws SQLException {
		String lastAttendance = null;
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		stmt = conn.prepareStatement("select * from employee_attendance where id = ? order by in_time DESC limit 1;");
		stmt.setString(1, Integer.toString(this.getEmp_id()));
		rs = stmt.executeQuery();
		while(rs.next()) {
			lastAttendance = rs.getString("in_time");
		}
		rs.close();
		stmt.close();
		conn.close();
		return lastAttendance;
	}
}
