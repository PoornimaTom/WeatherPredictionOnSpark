package com.weatherpred.modelbuilder.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.Before;
import org.junit.Test;

import com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder;
import com.weatherpred.util.CommonUtil;
import com.weatherpred.utils.constants.ModelConstants;
import com.weatherpred.utils.constants.NumericMapping;

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

	/**
	 * Input reading
	 */
	private String inputLine;
	/**
	 * Expected labeled point for temperature
	 */
	private String labelledPointForTemp;
	/**
	 * Expected labeled point for humidity
	 */
	private String labelledPointForHumidity;
	/**
	 * Expected labeled point for pressure
	 */
	private String labelledPointForPressure;

	@Before
	public void loadData() {
		inputLine = "Sunny,0.83,-33.8688197,151.2092955,24.5399284363,1015.5,72.23,1420030800";
		labelledPointForTemp = "(72.23,[-33.8688197,151.2092955,24.5399284363,11.0,18.0])";
		labelledPointForHumidity = "(0.83,[-33.8688197,151.2092955,24.5399284363,11.0,18.0])";
		labelledPointForPressure = "(1015.5,[-33.8688197,151.2092955,24.5399284363,11.0,18.0])";

	}

	/**
	 * Test method for Main method
	 */
	@Test
	public void testMain() {
		DecisionTreeRegressionBuilder regressionBuilder = new DecisionTreeRegressionBuilder();
		assertNotEquals(regressionBuilder.getHumidityModel(), null);
		assertNotEquals(regressionBuilder.getPressureModel(), null);
		assertNotEquals(regressionBuilder.getTemperatureModel(), null);

	}

	/**
	 * /** Test method for function createLabelledPointForTemp
	 */
	@Test
	public void testCreateLabelledPointDataForTemp() {
		String[] parts = inputLine.split(ModelConstants.DELIMITTER_COMA);

		LabeledPoint lb = new LabeledPoint(
				Double.parseDouble(parts[NumericMapping.TEMPERATURE_INDEX]),
				Vectors.dense(Double
						.parseDouble(parts[NumericMapping.LAT_INDEX]), Double
						.parseDouble(parts[NumericMapping.LONG_INDEX]), Double
						.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
						CommonUtil.getMonth(parts[NumericMapping.TIME_INDEX]),
						CommonUtil.getHour(parts[NumericMapping.TIME_INDEX])));

		assertEquals(lb.toString(), labelledPointForTemp);

	}

	/**
	 * Test method for function createLabelledPointForHumidity
	 */
	@Test
	public void testCreateLabelledPointDataForHumidity() {
		String[] parts = inputLine.split(ModelConstants.DELIMITTER_COMA);
		/*
		 * The features to be included for humidity prediction are specified
		 * here.
		 */
		LabeledPoint lb = new LabeledPoint(
				Double.parseDouble(parts[NumericMapping.HUMIDITY_INDEX]),
				Vectors.dense(Double
						.parseDouble(parts[NumericMapping.LAT_INDEX]), Double
						.parseDouble(parts[NumericMapping.LONG_INDEX]), Double
						.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
						CommonUtil.getMonth(parts[NumericMapping.TIME_INDEX]),
						CommonUtil.getHour(parts[NumericMapping.TIME_INDEX])));
		assertEquals(lb.toString(), labelledPointForHumidity);
	}

	/**
	 * Test method for function createLabelledPointForPressure
	 */
	@Test
	public void testCreateLabelledPointDataForPressure() {
		String[] parts = inputLine.split(ModelConstants.DELIMITTER_COMA);
		/*
		 * The features to be included for pressure prediction are specified
		 * here.
		 */
		LabeledPoint lb = new LabeledPoint(
				Double.parseDouble(parts[NumericMapping.PRESSURE_INDEX]),
				Vectors.dense(Double
						.parseDouble(parts[NumericMapping.LAT_INDEX]), Double
						.parseDouble(parts[NumericMapping.LONG_INDEX]), Double
						.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
						CommonUtil.getMonth(parts[NumericMapping.TIME_INDEX]),
						CommonUtil.getHour(parts[NumericMapping.TIME_INDEX])));
		assertEquals(lb.toString(), labelledPointForPressure);
	}
}
