package com.weatherpred.modelbuilder.test;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder;

/**
 * Test Class for Decision Tree Regression Builder 
 * 
 * Date : August 4, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class DecisionTreeRegressionBuilderTest {

	@Test
	public void testMain() {
		DecisionTreeRegressionBuilder regressionBuilder = new DecisionTreeRegressionBuilder();
		assertNotEquals(regressionBuilder.getHumidityModel(), null);
		assertNotEquals(regressionBuilder.getPressureModel(), null);
		assertNotEquals(regressionBuilder.getTemperatureModel(), null);

	}

}
