package com.odin.pageController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;
import com.odin.constantValues.status;
import com.odin.dbManager.DBConnectionAgent;

public class CreateUser extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2473375340557163523L;

	Logger LOG = Logger.getLogger(CreateUser.class.getClass());
	
	String employeeName;
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	String joinDate;
	String salary;
	String accessLevel;
	String username;
	String password;
	String phone;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}

	private void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.debug("Creating new employee details");
		String employeeName = req.getParameter("name");
		String joinDate = req.getParameter("joinDate");
		String salary = req.getParameter("salary");
		String phone = req.getParameter("phone");
		String accessLevel = req.getParameter("level");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String rePassword = req.getParameter("rePassword");
		HttpSession session = req.getSession();
		if(employeeName.isEmpty() || username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
			try {
				session.setAttribute("pageStatus", status.CreateUser.INVALID.values);
				redirectSelf(res);
			} catch (IOException e) {
				LOG.error(e);
			}
		}
		else if(!password.equals(rePassword)) {
			try {
				session.setAttribute("pageStatus", status.CreateUser.INCORRECT_PASS.values);
				redirectSelf(res);
			} catch (IOException e) {
				LOG.error(e);
			}
		}
		else {
			if(accessLevel.isEmpty()) {
				LOG.debug("Setting access level as 2 since access level input field is blank.");
				setAccessLevel("2");
			}
			else if(!accessLevel.isEmpty()) {
				setAccessLevel(accessLevel);
			}
			if(joinDate.isEmpty()) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
				LocalDateTime ldt = LocalDateTime.now();
				setJoinDate(dtf.format(ldt));
			}
			else if(!joinDate.isEmpty()){
				setJoinDate(joinDate);
			}
			if(phone.isEmpty()) {
				LOG.debug("Setting phone number as 0000000000 since Phone number input field is blank.");
				setPhone("0000000000");
			}
			else if(!phone.isEmpty()) {
				setPhone(phone);
			}
			if(salary.isEmpty()) {
				LOG.debug("Setting join date as salary as 000000 since salary input field is blank.");
				setSalary("000000");
			}
			else if(!salary.isEmpty()) {
				setSalary(salary);
			}
			if(!accessLevel.isEmpty() || !joinDate.isEmpty() || !phone.isEmpty() || !salary.isEmpty()){
				
				setEmployeeName(employeeName);
				setUsername(username);
				setPassword(password);
				DBConnectionAgent dbObj = new DBConnectionAgent();
				Connection conn = dbObj.connectionAgent();
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String query = "INSERT INTO EMPLOYEE (name,phone,date_of_joining,salary,level) VALUES (?,?,?,?,?)";
				try {
					stmt = conn.prepareStatement(query);
					stmt.setString(1, getEmployeeName());
					stmt.setString(2, getPhone());
					stmt.setString(3, getJoinDate());
					stmt.setString(4, getSalary());
					stmt.setString(5, getAccessLevel());
					if(stmt.executeUpdate() == 0) {
						LOG.error("Internal error");
						session.setAttribute("pageStatus", status.CreateUser.INTERNAL_ERROR.values);
						try {
							redirectSelf(res);
						} catch (IOException e) {
							LOG.error(e);
						}
					}
					else {
						LOG.debug("Employee registered.");
						query = "SELECT * FROM EMPLOYEE WHERE PHONE = ?";
						stmt = conn.prepareStatement(query);
						stmt.setString(1, getPhone());
						rs = stmt.executeQuery();
						String empId = "";
						while(rs.next()) {
							empId = rs.getString("ID");
						}
						LOG.debug("Inserting username and password for employee id : "+empId);
						query = "INSERT INTO AUTH_USER VALUES (?,?,PASSWORD(?))";
						stmt= conn.prepareStatement(query);
						stmt.setString(1, empId);
						stmt.setString(2, getUsername());
						stmt.setString(3, getPassword());
						if (stmt.executeUpdate() == 0 ) {
							LOG.error("Internal error");
							session.setAttribute("pageStatus", status.CreateUser.INTERNAL_ERROR.values);
							try {
								redirectSelf(res);
							} catch (IOException e) {
								LOG.error(e);
							}
						}
						else {
							LOG.debug("Employee login creadentials inserted");
							session.setAttribute("pageStatus", status.CreateUser.INSERTED.values);
							try {
								redirectSelf(res);
							} catch (IOException e) {
								LOG.error(e);
							}
						}
					}
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					LOG.error(e);
				}
				
			}
		}
	}
	void redirectSelf(HttpServletResponse res) throws IOException {
		res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
				+ "/subscription/createUser.jsp");
	}
}
