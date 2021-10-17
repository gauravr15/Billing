package com.odin.configManager;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.odin.constantValues.Constants;

public class Config extends HttpServlet{
	
	private static final long serialVersionUID = 5987901213242640293L;
	static Logger LOG = Logger.getLogger(Config.class.getClass());
	
	static String IP=null;
	static String PORT=null;
	static String DB=null;
	static String USER=null;
	static String PASS=null;
	
	public void init() {
		LOG.debug("Initializing Properties file");
		FileReader reader = null;
		try {
			reader = new FileReader("dbProp.properties");
			Properties prop = new Properties();
			try {
				prop.load(reader);
				Constants.setDRIVER(prop.getProperty("dbDriver"));
				Constants.setIP(prop.getProperty("dbURL"));
				Constants.setPORT(prop.getProperty("dbPort"));
				Constants.setDBNAME(prop.getProperty("dbName"));
				Constants.setUSER(prop.getProperty("dbUser"));
				Constants.setPASS(prop.getProperty("dbPass"));
			} catch (IOException e) {
				LOG.debug("Unable to load DB properties");
				System.exit(0);
			}
			
		} catch (FileNotFoundException e) {
			LOG.error("Unable to read dbProp.properties file");
			System.exit(0);
		}
		Constants obj = Constants.getInstance();
		LOG.debug(obj.getDRIVER()+" "+obj.getIP()+" "+obj.getPORT()+" "+obj.getUSER()+" "+obj.getPASS());
	}
}
