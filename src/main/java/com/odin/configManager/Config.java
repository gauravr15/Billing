package com.odin.configManager;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.odin.Exceptions.PropertiesException;
import com.odin.constantValues.DBConstants;
import com.odin.dbManager.DBCheck;
import com.odin.supportingThreads.BirthdayThread;

public class Config extends HttpServlet{
	
	private static final long serialVersionUID = 5987901213242640293L;
	static Logger LOG = Logger.getLogger(Config.class.getClass());
	
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
			reader = new FileReader("dbProp.properties");
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
		}
		DBCheck dbCheckObj = new DBCheck();
		if(dbCheckObj.dbCheck(DBConstants.getInstance()) ==null) {
			LOG.error("Cannot initialize DB");
			System.exit(0);
		}
		else
			LOG.debug("DB initialized successfully");
		BirthdayThread birthdayObject = BirthdayThread.getInstance();;
		Thread birthdayThread = new Thread(birthdayObject);
		birthdayThread.setName("BIRTHDAY_THREAD");
		birthdayThread.start();
	}
}
