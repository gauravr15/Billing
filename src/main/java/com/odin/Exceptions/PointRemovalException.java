package com.odin.Exceptions;

import org.apache.log4j.Logger;

public class PointRemovalException extends RuntimeException {
	
	/**
	 * 
	 */
	Logger LOG = Logger.getLogger(ConfigException.class.getClass());
	private static final long serialVersionUID = -4060909184795683357L;

	public PointRemovalException(String str){
		super(str);
		LOG.error(str);
	}
}
