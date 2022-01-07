package com.odin.cashback;

import org.apache.log4j.Logger;

import com.odin.Exceptions.ConfigException;
import com.odin.configManager.ConfigParamMap;

public class AddCashback {
	
	Logger LOG = Logger.getLogger(AddCashback.class.getClass());
	
	public int cashBackCalculator(int billAmount) {
		LOG.debug("calculating cashback for amount "+billAmount);
		int cashback_percent;
		int cashback = 0;
		if(ConfigParamMap.params.get("CASHBACK_PERCENT")==null)
			throw new ConfigException("No cashback configuration found in DB. \n Please configure it in config_params table as param_name CASHBACK_PERCENT");
		cashback_percent = Integer.parseInt(ConfigParamMap.params.get("CASHBACK_PERCENT"));
		LOG.debug("cashback % is "+cashback_percent);
		cashback = billAmount * cashback_percent/100;
		LOG.debug("Cashback received is "+cashback);
		return cashback;
	}
}
