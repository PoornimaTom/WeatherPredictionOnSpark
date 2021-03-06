package com.weatherpred.exceptions;

import org.apache.log4j.Logger;

/**
 * Custom exception class
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class WeatherPredException extends Exception {
	
	private static final long serialVersionUID = 3725912523790905L;
	final static Logger logger = Logger.getLogger(WeatherPredException.class);
	
	/**
	 * @param message
	 *  Passing message argument to superclass
	 */
	public WeatherPredException(String message) {
		super(message);
		logger.debug("Exception thrown :: " + message);
	}
	
}
