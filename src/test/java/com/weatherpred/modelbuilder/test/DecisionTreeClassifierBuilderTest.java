/**
 * 
 */
package com.weatherpred.modelbuilder.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.Before;
import org.junit.Test;

import com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder;
import com.weatherpred.util.CommonUtil;
import com.weatherpred.utils.constants.ModelConstants;
import com.weatherpred.utils.constants.NumericMapping;

/**
 * Test Class for Decision Tree Classifier Builder
 * 
 * Date : August 4, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class DecisionTreeClassifierBuilderTest {

	/**
	 * Input reading
	 */
	private String inputLine;
	/**
	 * Expected output reading
	 */
	private String outputLine;
	/**
	 * Expected labeled point output
	 */
	private String labelledPoint;

	@Before
	public void loadData() {
		inputLine = "Sunny,0.83,-33.8688197,151.2092955,24.5399284363,1015.5,72.23,1420030800";
		outputLine = "0.0,0.83,-33.8688197,151.2092955,24.5399284363,1015.5,72.23,1420030800";
		labelledPoint = "(0.0,[0.83,-33.8688197,151.2092955,24.5399284363,1015.5,72.23,11.0,18.0])";

	}

	/**
	 * Test method for
	 * {@link com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder#main(java.lang.String[])}
	 * .
	 */
	@Test
	public void testMain() {
		DecisionTreeClassifierBuilder classifierBuilder = new DecisionTreeClassifierBuilder();
		assertNotEquals(classifierBuilder.getDecisionTreeClassifierModel(),
				null);
	}

	/**
	 * Test method for function transformData
	 */
	@Test
	public void testTransformData() {

		String[] parts = inputLine.split(ModelConstants.DELIMITTER_COMA);
		double numericValue = 0.0;

		// Convert categorical feature to numerical
		switch (parts[0]) {
		case ModelConstants.SUNNY:
			numericValue = NumericMapping.SUNNY;
			break;
		case ModelConstants.RAIN:
			numericValue = NumericMapping.RAIN;
			break;
		case ModelConstants.SNOW:
			numericValue = NumericMapping.SNOW;
			break;
		default:
			numericValue = -1;
			break;
		}
		parts[0] = Double.toString(numericValue);
		StringBuilder strBuilder = new StringBuilder();

		for (int i = 0; i < parts.length; i++) {
			strBuilder.append(parts[i]);
			strBuilder.append(ModelConstants.DELIMITTER_COMA);
		}

		// Remove extra comma
		if (strBuilder.length() > 0) {
			strBuilder.setLength(strBuilder.length() - 1);
		}

		assertEquals(strBuilder.toString(), outputLine);

	}

	/**
	 * /** Test method for function createLabelledPointData
	 */
	@Test
	public void testCreateLabelledPointData() {
		String[] parts = inputLine.split(ModelConstants.DELIMITTER_COMA);

		LabeledPoint b = new LabeledPoint(0, Vectors.dense(
				Double.parseDouble(parts[NumericMapping.HUMIDITY_INDEX]),
				Double.parseDouble(parts[NumericMapping.LAT_INDEX]),
				Double.parseDouble(parts[NumericMapping.LONG_INDEX]),
				Double.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
				Double.parseDouble(parts[NumericMapping.PRESSURE_INDEX]),
				Double.parseDouble(parts[NumericMapping.TEMPERATURE_INDEX]),
				CommonUtil.getMonth(parts[NumericMapping.TIME_INDEX]),
				CommonUtil.getHour(parts[NumericMapping.TIME_INDEX])));
		assertEquals(b.toString(), labelledPoint);

	}
}
