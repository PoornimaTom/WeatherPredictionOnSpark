/**
 * 
 */
package com.weatherpred.util.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.weatherpred.dto.WeatherDTO;
import com.weatherpred.enums.WeatherStatus;
import com.weatherpred.util.CommonUtil;
import com.weatherpred.utils.constants.Constants;
import com.weatherpred.utils.constants.NumericMapping;

/**
 * Test Class for CommonUtil
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class CommonUtilTest {

	/**
	 * dummy value for populating condition
	 */
	private static double dummyCoordinate;
	/**
	 * timestamp in long
	 */
	private static String timeinMilliSec;
	/**
	 * hour value
	 */
	private static double hour;
	/**
	 * month value
	 */
	private static double month;
	/**
	 * formatted time
	 */
	private static String formattedTime;
	/**
	 * dummy double value
	 */
	private static double dummy;
	/**
	 * sample output 1
	 */
	private static WeatherDTO sampOutput1;
	/**
	 * sample output 2
	 */
	private static WeatherDTO sampOutput2;
	/**
	 * sample output List
	 */
	private static List<WeatherDTO> sampOutputList;
	
	/**
	 * sampleLocation
	 */
	private static String sampleLocation;
	/**
	 * Delta value for unit floating comparisons
	 */
	private static int delta;

	/**
	 * Load initial data before testing
	 */
	@Before
	public void loadData(){
		dummyCoordinate = 0.0;
		timeinMilliSec="1425582000";
		hour=0;
		month = 2;
		formattedTime = "2015-03-05T19:00:00Z";
		dummy = -1;
		delta = 0;
		sampleLocation="/tmp/test"+Math.random()+".txt";
		sampOutput1 = new WeatherDTO(Constants.NOT_AVAILABLE, 0.0, 0.0,0.0, WeatherStatus.SUNNY, formattedTime, 0, 0, 0);
		sampOutput2 = new WeatherDTO(Constants.NOT_AVAILABLE, 0.0, 0.0,0.0, WeatherStatus.RAIN, formattedTime, 0, 0, 0);
		sampOutputList = new ArrayList<WeatherDTO>();
		sampOutputList.add(sampOutput1);	
		sampOutputList.add(sampOutput2);
		
	}
	
	/**
	 * Test method for {@link com.weatherpred.util.CommonUtil#getHour(java.lang.String)}.
	 */
	@Test
	public void testGetHour() {
		assertEquals(CommonUtil.getHour(timeinMilliSec), hour, delta);
	}

	/**
	 * Test method for {@link com.weatherpred.util.CommonUtil#getMonth(java.lang.String)}.
	 */
	@Test
	public void testGetMonth() {
		assertEquals(CommonUtil.getMonth(timeinMilliSec), month, delta);
	}

	/**
	 * Test method for {@link com.weatherpred.util.CommonUtil#epochConverter(java.lang.String)}.
	 */
	@Test
	public void testEpochConverter() {
		assertEquals(CommonUtil.epochConverter(timeinMilliSec), formattedTime);
	}

	/**
	 * Test method for {@link com.weatherpred.util.CommonUtil#findWeatherStatus(double)}.
	 */
	@Test
	public void testFindWeatherStatus() {
		assertEquals(CommonUtil.findWeatherStatus(NumericMapping.SUNNY), WeatherStatus.SUNNY);
		assertEquals(CommonUtil.findWeatherStatus(NumericMapping.RAIN), WeatherStatus.RAIN);
		assertEquals(CommonUtil.findWeatherStatus(NumericMapping.SNOW), WeatherStatus.SNOW);
		assertEquals(CommonUtil.findWeatherStatus(dummy), null);
	}

	/**
	 * Test method for {@link com.weatherpred.util.CommonUtil#findLocation(double, double, double)}.
	 */
	@Test
	public void testFindLocation() {
		assertEquals(CommonUtil.findLocation(dummyCoordinate, dummyCoordinate, dummyCoordinate), Constants.NOT_AVAILABLE);
	}

	/**
	 * Test method for {@link com.weatherpred.util.CommonUtil#saveOutput(com.weatherpred.dto.WeatherDTO, java.lang.String)}.
	 */
	@Test
	public void testSaveOutput() {
		assertEquals(CommonUtil.saveOutput(sampOutputList,sampleLocation), true);

	}

}
