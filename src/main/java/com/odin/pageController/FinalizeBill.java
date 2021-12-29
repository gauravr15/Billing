package com.odin.pageController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.odin.configManager.ServiceMap;
import com.odin.constantValues.status;

public class FinalizeBill extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6715481227618450805L;

	Logger LOG =Logger.getLogger(FinalizeBill.class.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	void process(HttpServletRequest req, HttpServletResponse res) {
		String billState = req.getParameter("checkoutBillState");
		String checkoutList = req.getParameter("checkoutList");
		double checkoutDiscount = Double.parseDouble(req.getParameter("checkoutDiscount"));
		String[] services = checkoutList.split(",");
		String item = "";
		String qty = "";
		String prices = "";
		double total = 0;
		LOG.debug("bill state is : "+billState);
		if(billState.equals(status.BillState.BILL_CHECK.values)) {
			for(int i = 0;i<services.length;i++) {
				if(!services[i].split(" ")[0].equals(null)) {
					item = item+services[i].split(" ")[0]+",";
					qty = qty+services[i].split(" ")[1]+",";
					prices = prices+ServiceMap.service_charges.get(services[i].split(" ")[0])+",";
					total = total + ((double)ServiceMap.service_charges.get(services[i].split(" ")[0]))* Integer.parseInt(services[i].split(" ")[1]);
				}
			}
		}
		else {
			for(int i=0;i<services.length;i++) {
				if(services[i]!=null) {
					item = item+ServiceMap.services.get(Integer.parseInt(services[i]))+",";
					qty = qty+services[i].split(" ")[1]+",";
					prices = prices+ServiceMap.service_charges.get(ServiceMap.services.get(Integer.parseInt(services[i])))+",";
					total = total + ((double) ServiceMap.service_charges.get(ServiceMap.services.get(Integer.parseInt(services[i])))* Integer.parseInt(services[i].split(" ")[1]));
				}
			}
		}
		
		item = item.substring(0,item.length()-1);
		qty = qty.substring(0, qty.length()-1);
		prices = prices.substring(0, prices.length()-1);
		double payAmount = total-checkoutDiscount;
		LOG.debug("item list is : "+item);
		LOG.debug("quantity list is : "+qty);
		LOG.debug("Price list is : "+prices);
		LOG.debug("total list is : "+total);
		LOG.debug("discount list is : "+checkoutDiscount);
		LOG.debug("Pay amount list is : "+payAmount);
	}

}
