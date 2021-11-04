package com.odin.Exceptions;

import org.apache.log4j.Logger;

import com.odin.constantValues.DBConstants;

public class PropertiesException extends RuntimeException{
	
	private static final long serialVersionUID = -5344465846458003528L;
	Logger LOG = Logger.getLogger(PropertiesException.class.getClass());
	
	public PropertiesException(String excep){
		super(excep);
		LOG.debug("Setting default values");
		DBConstants.setDRIVER("com.mysql.cj.jdbc.Driver");
		DBConstants.setIP("jdbc:mysql://localhost:");
		DBConstants.setPORT("3306/");
		DBConstants.setDBNAME("sm");
		DBConstants.setUSER("root");
		DBConstants.setPASS("root");
	}
}
