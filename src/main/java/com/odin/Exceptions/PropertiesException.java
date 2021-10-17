package com.odin.Exceptions;

import org.apache.log4j.Logger;

import com.odin.constantValues.Constants;

public class PropertiesException extends RuntimeException{
	
	Logger LOG = Logger.getLogger(PropertiesException.class.getClass());
	
	public PropertiesException(String excep){
		super(excep);
		LOG.fatal("Setting default values");
		Constants.setDRIVER("com.mysql.cj.jdbc.Driver");
		Constants.setIP("jdbc:mysql://localhost:");
		Constants.setPORT("3306/");
		Constants.setDBNAME("sm");
		Constants.setUSER("root");
		Constants.setPASS("root");
	}
}
