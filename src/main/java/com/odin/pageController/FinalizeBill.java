package com.odin.pageController;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;
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
		String checkoutDiscount = req.getParameter("checkoutDiscount");
		String checkoutUser = req.getParameter("checkoutUser");
		double discount = 0;
		if(checkoutDiscount.length()!=0) {
			discount = Double.parseDouble(checkoutDiscount);
		}
		else {
			discount = 0;
		}
		String[] services = checkoutList.split(",");
		String item = "";
		String qty = "";
		String rate = "";
		String price = "";
		double total = 0;
		LOG.debug("bill state is : "+billState);
		if(billState.equals(status.BillState.BILL_CHECK.values)) {
			for(int i = 0;i<services.length;i++) {
				if(!services[i].split(" ")[0].equals(null) && ServiceMap.service_code.get(services[i].split(" ")[0])!=null) {
					item = item+ServiceMap.service_code.get(services[i].split(" ")[0])+",";
					qty = qty+services[i].split(" ")[1]+",";
					rate = rate+ServiceMap.service_charges.get(services[i].split(" ")[0])+",";
					price = price+((double) ServiceMap.service_charges.get(services[i].split(" ")[0])* Integer.parseInt(services[i].split(" ")[1]))+",";
					total = total + ((double)ServiceMap.service_charges.get(services[i].split(" ")[0]))* Integer.parseInt(services[i].split(" ")[1]);
				}
			}
		}
		else {
			LOG.debug("item list is : "+checkoutList);
			for(int i = 0;i<services.length;i++) {
				if(services[i]!=null && ServiceMap.service_code.get(ServiceMap.services.get(Integer.valueOf(services[i].split(" ")[0])))!=null) {
					item = item+ServiceMap.service_code.get(ServiceMap.services.get(Integer.valueOf(services[i].split(" ")[0])))+",";
					qty = qty+services[i].split(" ")[1]+",";
					rate = rate+ServiceMap.service_charges.get(ServiceMap.services.get(Integer.parseInt(services[i].split(" ")[0])))+",";
					price = price+((double) ServiceMap.service_charges.get(ServiceMap.services.get(Integer.parseInt(services[i].split(" ")[0])))* Integer.parseInt(services[i].split(" ")[1]))+",";
					total = total + ((double) ServiceMap.service_charges.get(ServiceMap.services.get(Integer.parseInt(services[i].split(" ")[0])))* Integer.parseInt(services[i].split(" ")[1]));
				}
			}
		}
		item = item.substring(0,item.length()-1);
		qty = qty.substring(0, qty.length()-1);
		rate = rate.substring(0, rate.length()-1);
		price = price.substring(0, price.length()-1);
		double payAmount = total-discount;
		LOG.debug("checkout user is : "+checkoutUser);
		LOG.debug("item list is : "+item);
		LOG.debug("quantity list is : "+qty);
		LOG.debug("rate list is : "+rate);
		LOG.debug("price list is : "+price);
		LOG.debug("total list is : "+total);
		LOG.debug("discount list is : "+checkoutDiscount);
		LOG.debug("Pay amount list is : "+payAmount);
		HttpSession session =req.getSession();
		session.setAttribute("checkoutUser", checkoutUser);
		session.setAttribute("itemList", item);
		session.setAttribute("qtyList", qty);
		session.setAttribute("rateList", rate);
		session.setAttribute("priceList", price);
		session.setAttribute("total", Double.toString(total));
		session.setAttribute("checkoutDiscount", checkoutDiscount);
		session.setAttribute("payAmount", Double.toString(payAmount));
		try {
			res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
			+ "/subscription/printBill.jsp");
		} catch (IOException e) {
			LOG.error(e);
		}
	}

}
