package com.odin.billingHandler;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;
import com.odin.configManager.ServiceMap;
import com.odin.constantValues.status.BillState;

public class ItemCheck extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3840225684411902795L;
	
	Logger LOG = Logger.getLogger(ItemCheck.class.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req, res);
	}

	private void process(HttpServletRequest req, HttpServletResponse res) {
		String billList = req.getParameter("checkItemList");
		String customer_name = req.getParameter("checkItemCustomerName");
		String customer_phone = req.getParameter("checkItemCustomerPhone");
		String customer_id = req.getParameter("checkItemCustomerId");
		int count = Integer.parseInt(req.getParameter("checkItemCount"));
		String[] itemList = billList.split(",");
		String item = "";
		String quantity ="";
		double totalAmount = 0;
		HttpSession session = req.getSession();
		for(int i=0;i<count;i++) {
			if(ServiceMap.services.get(Integer.parseInt(itemList[i].split(" ")[0])) !=null) {
			item = item+ServiceMap.services.get(Integer.parseInt(itemList[i].split(" ")[0]))+",";
			quantity = quantity+itemList[i].split(" ")[1]+",";
			totalAmount = totalAmount + ((int) ServiceMap.service_charges.get(ServiceMap.services.get(Integer.parseInt(itemList[i].split(" ")[0]))))*Integer.parseInt(itemList[i].split(" ")[1]);
			}
			else {
				item = item+"%"+",";
				quantity = quantity+itemList[i].split(" ")[1]+",";
			}
		}
		item = item.substring(0, item.length()-1);
		quantity = quantity.substring(0, quantity.length()-1);
		session.setAttribute("customer_name", customer_name);
		session.setAttribute("customer_phone", customer_phone);
		session.setAttribute("customer_id", customer_id);
		session.setAttribute("item", item);
		session.setAttribute("quantity", quantity);
		session.setAttribute("count", Integer.toString(count));
		session.setAttribute("checkTotal", Double.toString(totalAmount));
		session.setAttribute("billState", BillState.BILL_CHECK.values);
		try {
			redirectSelf(res);
		} catch (IOException e) {
			LOG.debug(e);
		}
	}
	void redirectSelf(HttpServletResponse res) throws IOException {
		res.sendRedirect("http://" + ConfigParamMap.params.get("HOST_IP") + ":" + ConfigParamMap.params.get("HOST_PORT")
				+ "/subscription/billing.jsp");
	}
}
