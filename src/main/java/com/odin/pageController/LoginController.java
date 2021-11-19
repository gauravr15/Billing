package com.odin.pageController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;
import com.odin.dbManager.DBConnectionAgent;
import com.odin.employeeHandler.EmployeeAttendance;

public class LoginController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3296921239660708180L;
	Logger LOG = Logger.getLogger(LoginController.class.getClass());
	
	String user = null;
	int empId = 0;

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	void process(HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		LOG.debug("Inside login controller");
		String user = req.getParameter("user");
		String pass = req.getParameter("pass");
		DBConnectionAgent DBObject = new DBConnectionAgent();
		Connection conn = DBObject.connectionAgent();
		String query = "SELECT * FROM AUTH_USER WHERE USER_ID = ? AND PASS = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			LOG.debug("Query to fire : " + query);
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user);
			stmt.setString(2, pass);
		} catch (SQLException e) {
			LOG.error(e);
		}
		try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
		boolean userExists = false;
		try {
			while (rs.next()) {
				userExists = true;
				this.empId = Integer.parseInt(rs.getString("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
		LOG.debug("User exists is : " + userExists);
		if (userExists) {
			LOG.debug("setting user session");
			session.setAttribute("user", user);
			LOG.debug("logged in as : " + (String) session.getAttribute("user"));
			this.user = (String) session.getAttribute("user");
			EmployeeAttendance attendanceObject = new EmployeeAttendance();
			attendanceObject.attendanceCheck(this);
			try {
				rs.close();
				stmt.close();
				conn.close();
				try {
					res.sendRedirect("/subscription/home.html");
				} catch (IOException e) {
					LOG.error(e);
				}
			} catch (SQLException e) {
				LOG.error(e);
			}
		} else {
			LOG.error("User does not exists");
			try {
				res.sendRedirect("http://"+ConfigParamMap.params.get("HOST_IP")+":"+ConfigParamMap.params.get("HOST_PORT")+"/subscription/index.html");
			} catch (IOException e) {
				LOG.error(e);
			}
		}
	}
}
