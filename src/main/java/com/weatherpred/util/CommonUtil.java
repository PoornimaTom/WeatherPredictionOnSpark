package com.weatherpred.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weatherpred.dto.WeatherDTO;
import com.weatherpred.enums.WeatherStatus;
import com.weatherpred.utils.constants.Constants;
import com.weatherpred.utils.constants.ModelConstants;
import com.weatherpred.utils.constants.NumericMapping;

/**
 * Class for common utility methods
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class CommonUtil {

	/**
	 * Logger
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(CommonUtil.class);

	/**
	 * To get hour of the day from timestamp
	 * 
	 * @param timeinMilliSec
	 * @return hour of the day [0-23]
	 */
	@SuppressWarnings("deprecation")
	public static double getHour(String timeinMilliSec) {
		try {
			Date date = new Date(Long.parseLong(timeinMilliSec) * 1000);
			return (double) date.getHours();
		} catch (Exception e) {
			logger.error("Failed to get hour from timestamp", e);
			return -1;
		}

	}

	/**
	 * To get month from timestamp
	 * 
	 * @param timeinMilliSec
	 * @return month [0-11]
	 */
	@SuppressWarnings("deprecation")
	public static double getMonth(String timeinMilliSec) {
		try {
			Date date = new Date(Long.parseLong(timeinMilliSec) * 1000);
			return (double) date.getMonth();
		} catch (Exception e) {
			logger.error("Failed to get month from timestamp", e);
			return -1;
		}

	}

	/**
	 * Method to convert epoch to dateformat
	 * @param timeinMilliSec
	 * @return formatted date
	 */
	public static String epochConverter(String timeinMilliSec) {
		try {
			Date date = new Date(Long.parseLong(timeinMilliSec) * 1000);
			DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			format.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE));

			return format.format(date);
		} catch (Exception e) {
			logger.error("Failed to convert epoch", e);
			return null;

		}
	}

	/**
	 * Method to find weather status from numeric value
	 * @param weather
	 * @return weather status - Sunny/Rain/Snow
	 */
	public static WeatherStatus findWeatherStatus(double weather) {
		if (weather == NumericMapping.SUNNY)
			return WeatherStatus.SUNNY;
		else if (weather == NumericMapping.RAIN)
			return WeatherStatus.RAIN;
		else if (weather == NumericMapping.SNOW)
			return WeatherStatus.SNOW;
		else
			return null;

	}

	public static String findLocation(double latitude, double longitude,
			double elevation) {
		// TODO: Integrate with location finder APIs. eg. Google Maps Geocoding API
		return Constants.NOT_AVAILABLE;
	}

	public static boolean saveOutput(List<WeatherDTO> weatherDTOList, String outputLocation) {
		boolean returnBoolean = false;
		Writer writer = null;
			
	try {
		writer = new BufferedWriter(new FileWriter(outputLocation));

		String	output = "\n\nOutput of Decision Tree Regression:\n"+ weatherDTOList.get(0).getLocation() + ModelConstants.DELIMITTER_PIPE
				+ weatherDTOList.get(0).getLatitude()
				+ ModelConstants.DELIMITTER_COMA
				+ weatherDTOList.get(0).getLongitude()
				+ ModelConstants.DELIMITTER_COMA
				+ weatherDTOList.get(0).getElevation()
				+ ModelConstants.DELIMITTER_PIPE
				+ weatherDTOList.get(0).getTime()
				+ ModelConstants.DELIMITTER_PIPE + weatherDTOList.get(0).getWeatherStatus().toString()
				+ ModelConstants.DELIMITTER_PIPE +weatherDTOList.get(0).getTemperature()
				+ ModelConstants.DELIMITTER_PIPE + weatherDTOList.get(0).getPressure()
				+ ModelConstants.DELIMITTER_PIPE + weatherDTOList.get(0).getHumidity()
		+"\n\nOutput of Linear Regression:\n"+ weatherDTOList.get(1).getLocation() + ModelConstants.DELIMITTER_PIPE
		+ weatherDTOList.get(1).getLatitude()
		+ ModelConstants.DELIMITTER_COMA
		+ weatherDTOList.get(1).getLongitude()
		+ ModelConstants.DELIMITTER_COMA
		+ weatherDTOList.get(1).getElevation()
		+ ModelConstants.DELIMITTER_PIPE
		+ weatherDTOList.get(1).getTime()
		+ ModelConstants.DELIMITTER_PIPE + weatherDTOList.get(1).getWeatherStatus().toString()
		+ ModelConstants.DELIMITTER_PIPE +weatherDTOList.get(1).getTemperature()
		+ ModelConstants.DELIMITTER_PIPE + weatherDTOList.get(1).getPressure()
		+ ModelConstants.DELIMITTER_PIPE + weatherDTOList.get(1).getHumidity();
	
		logger.info("Output"+output);
		writer.write(output + "\n");
		writer.flush();
		returnBoolean = true;
	} catch (Exception e) {
		logger.error(e.getMessage());
	} finally {
		if (writer != null)
			try {
				writer.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

	}
	return returnBoolean;

		
	}
	
	
}
