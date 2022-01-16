package com.odin.configManager;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.odin.Exceptions.ConfigException;
import com.odin.Exceptions.PropertiesException;
import com.odin.cashback.AddNewPoints;
import com.odin.cashback.RemoveExpiredPoints;
import com.odin.constantValues.DBConstants;
import com.odin.constantValues.status.ConfigParamName;
import com.odin.customerHandler.CustomerBirthday;
import com.odin.dbManager.DBCheck;

public class Config extends HttpServlet{
	
	private static final long serialVersionUID = 5987901213242640293L;
	Logger LOG = Logger.getLogger(Config.class.getClass());
	
	static String IP=null;
	static String PORT=null;
	static String DB=null;
	static String USER=null;
	static String PASS=null;
	
	public void init() {
		LOG.debug("Initializing Daemon");
		FileReader reader = null;
		Properties prop = new Properties();
		try {
			reader = new FileReader("../TOMCAT_RELEASE/conf/dbProp.properties");
			try {
				prop.load(reader);
			} catch (IOException e) {
				LOG.debug("Unable to load DB properties");
			}
			
		} catch (FileNotFoundException e) {
			LOG.error("Unable to read dbProp.properties file");
		}
		if(prop.getProperty("dbDriver")==null || prop.getProperty("dbURL")==null || prop.getProperty("dbPort")==null || prop.getProperty("dbName")==null || prop.getProperty("dbUser") ==null || prop.getProperty("dbPass") == null) {
			try {
				throw new PropertiesException("Please check dbProp.properties file contents");
			}
			catch(Exception e) {
				LOG.error(e);
				LOG.fatal("Initializing with default values");
				LOG.fatal("DB URL is set as : "+DBConstants.getIP()+DBConstants.getPORT()+DBConstants.getDBNAME());
				LOG.fatal("DB User and Password is set as : "+DBConstants.getUSER()+" and "+DBConstants.getPASS());
			}
		}
		else {
			DBConstants.setDRIVER(prop.getProperty("dbDriver"));
			DBConstants.setIP(prop.getProperty("dbURL"));
			DBConstants.setPORT(prop.getProperty("dbPort"));
			DBConstants.setDBNAME(prop.getProperty("dbName"));
			DBConstants.setUSER(prop.getProperty("dbUser"));
			DBConstants.setPASS(prop.getProperty("dbPass"));
			LOG.debug("DB Details : "+DBConstants.getIP()+DBConstants.getPORT()+DBConstants.getDBNAME());
			LOG.debug("DB username is "+DBConstants.getUSER()+" and password is "+DBConstants.getPASS());
		}
		DBCheck dbCheckObject = new DBCheck();
		if(dbCheckObject.dbCheck() == true) {
			LOG.trace("Passed DB Check");
			LOG.debug("System initialized successfully");
		}
		else {
			LOG.error("DB Check failed \n Terminating system.....");
			System.exit(0);
		}
		ConfigParamMap.paramValues();
		if(Boolean.parseBoolean(ConfigParamMap.params.get(ConfigParamName.BIRTHDAY_CHECK.toString()))==true) {
			CustomerBirthday birthdayObject = new CustomerBirthday();
			Thread birthdayThread = new Thread(birthdayObject);
			birthdayThread.setName("BIRTHDAY_THREAD");
			birthdayThread.start();
		}
		else if(Boolean.parseBoolean(ConfigParamMap.params.get("BIRTHDAY_CHECK"))==false) {
			LOG.debug("Birthday SMS is disabled");
		}
		else {
			try {
				ConfigParamMap.params.put("BIRTHDAY_CHECK", "false");
				throw new ConfigException ("Value for BIRTHDAY_CHECK is not configured so birthday SMS will not be sent");
			}
			catch(Exception e) {
				LOG.debug(e);
			}
		}
		LOG.debug("Initialising queue manager");
		QueueManager.initQueue();
		if(Boolean.parseBoolean(ConfigParamMap.params.get("EXPIRE_POINTS"))== true) {
			LOG.debug("Point expiry is set as true");
			LOG.debug("Points will expire after "+ConfigParamMap.params.get("POINTS_VALIDITY")+" days");
			RemoveExpiredPoints pointsObj = new RemoveExpiredPoints();
			Thread pointsThread = new Thread(pointsObj);
			pointsThread.setName("VALIDATE_POINTS");
			pointsThread.start();
		}
		else if(Boolean.parseBoolean(ConfigParamMap.params.get("EXPIRE_POINTS"))== false){
			LOG.debug("Points expiry is set as false");
		}
		if(Boolean.parseBoolean(ConfigParamMap.params.get("COOLDOWN_POINTS"))== true) {
			LOG.debug("Cooldown point is set as true");
			LOG.debug("New points will be added after "+ConfigParamMap.params.get("COOLDOWN_PERIOD")+" days");
			AddNewPoints addPointsObj = new AddNewPoints();
			Thread pointsThread = new Thread(addPointsObj);
			pointsThread.setName("COOLDOWN_POINTS");
			pointsThread.start();
		}
		else if(Boolean.parseBoolean(ConfigParamMap.params.get("COOLDOWN_POINTS"))== false){
			LOG.debug("Cooldown points is set as false");
		}
		if(ConfigParamMap.params.get("BILL_PRINT_SIZE")==null) {
			ConfigParamMap.params.put("BILL_PRINT_SIZE", "14");
			LOG.fatal("No print size configured for bill print, so setting size as 14px");
		}
		else {
			LOG.debug("Bill print size configured as "+ConfigParamMap.params.get("BILL_PRINT_SIZE"));
		}
		LOG.debug("Initialising services "+ServiceMap.getInstance());
		LOG.debug("System initialisation complete.");
	}
}
