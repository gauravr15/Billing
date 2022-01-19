package com.odin.pageController;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;

public class Logout extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5598191474775544810L;
	
	Logger LOG = Logger.getLogger(Logout.class.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}

	private void process(HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		LOG.debug("Logging out user id : "+session.getAttribute("user"));
		session.invalidate();
		try {
			res.sendRedirect("http://"+ConfigParamMap.params.get("HOST_IP")+":"+ConfigParamMap.params.get("HOST_PORT")+"/subscription/login.jsp");
		} catch (IOException e) {
			LOG.error(e);
		}
	}

}
