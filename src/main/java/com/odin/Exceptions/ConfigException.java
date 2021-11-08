package com.odin.Exceptions;

import org.apache.log4j.Logger;

public class ConfigException extends RuntimeException{
	
	Logger LOG = Logger.getLogger(ConfigException.class.getClass());
	
	public ConfigException(String exception){
		super(exception);
		LOG.fatal(exception);
	}
}
