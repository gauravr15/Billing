package com.odin.pageController;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;

public class BillingController extends HttpServlet{
	
	Logger LOG = Logger.getLogger(BillingController.class.getClass());
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	void process(HttpServletRequest req, HttpServletResponse res) {
		LOG.trace("inside billing controller");
		HttpSession session = req.getSession();
		session.setAttribute("customerID", "123");
		try {
			res.sendRedirect("http://"+ConfigParamMap.params.get("HOST_IP")+":"+ConfigParamMap.params.get("HOST_PORT")+"/subscription/billing.jsp");
		} catch (IOException e) {
			LOG.error(e);
		}
	}
}
