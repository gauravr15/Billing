package com.odin.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.odin.configManager.ConfigParamMap;


/**
 * Servlet Filter implementation class ServletFilter
 */
@WebFilter("/home.html")
public class ServletFilter implements Filter {

	Logger LOG = Logger.getLogger(ServletFilter.class.getClass());
    /**
     * Default constructor. 
     */
    public ServletFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		String user = (String) session.getAttribute("user");
		if(user!=null) {
			chain.doFilter(request, response);
		}
		else {
			HttpServletResponse res = (HttpServletResponse)response;
			res.sendRedirect("http://"+ConfigParamMap.params.get("HOST_IP")+":"+ConfigParamMap.params.get("HOST_PORT")+"/subscription/index.html");
			LOG.error("user needs to log in first");
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
