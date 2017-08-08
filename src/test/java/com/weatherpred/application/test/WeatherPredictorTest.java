/**
 * 
 */
package com.weatherpred.application.test;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.weatherpred.application.WeatherPredictor;

/**
 * Test Class for Weather Predictor 
 * 
 * Date : August 4, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class WeatherPredictorTest {

	/**
	 * Test method for {@link com.weatherpred.application.WeatherPredictor#main(java.lang.String[])}.
	 */
	@Test
	public void testMain() {
		WeatherPredictor weatherPredApp = new WeatherPredictor();
		assertNotEquals(weatherPredApp.getClassifierModel(), null);
		assertNotEquals(weatherPredApp.getTemperatureModel(), null);
		assertNotEquals(weatherPredApp.getHumidityModel(), null);
		assertNotEquals(weatherPredApp.getPressureModel(), null);
		
		assertNotEquals(weatherPredApp.getTemperatureLinearModel(), null);
		assertNotEquals(weatherPredApp.getHumidityLinearModel(), null);
		assertNotEquals(weatherPredApp.getPressureLinearModel(), null);
	}

}
