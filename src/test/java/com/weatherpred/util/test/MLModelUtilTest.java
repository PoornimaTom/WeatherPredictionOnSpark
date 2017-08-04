package com.weatherpred.util.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.weatherpred.enums.WeatherParameter;
import com.weatherpred.mlmodel.DecisionTreeClassifierModel;
import com.weatherpred.mlmodel.DecisionTreeRegressionModel;
import com.weatherpred.util.MLModelUtil;
import com.weatherpred.utils.constants.Constants;
import com.weatherpred.utils.constants.ModelConstants;

/**
 * Test Class for MLModelUtil
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class MLModelUtilTest {

	/**
	 * Decision Tree Classifier model
	 */
	private static DecisionTreeClassifierModel classificationTreeMlModel;
	/**
	 * Decision Tree Regression model
	 */
	private static DecisionTreeRegressionModel regressionTreeMlModel;
	/**
	 * Resource bundle
	 */
	private static ResourceBundle rb;
	/**
	 * Delta value for unit floating comparisons
	 */
	private static double delta;

	/**
	 *  Load initial data before testing
	 */
	@Before
	public void loadData() {
		classificationTreeMlModel = MLModelUtil
				.populateModelParams(new DecisionTreeClassifierModel());
		regressionTreeMlModel = MLModelUtil
				.populateModelParams(new DecisionTreeRegressionModel(),
						WeatherParameter.TEMPERATURE);
		rb = ResourceBundle.getBundle(Constants.MODEL_RESOURCE_BUNDLE);
	}

	/**
	 * Test method for
	 * {@link com.weatherpred.util.MLModelUtil#populateModelParams(com.weatherpred.mlmodel.DecisionTreeClassifierModel)}
	 * .
	 */
	@Test
	public void testPopulateModelParamsDecisionTreeClassifierModel() {
		assertEquals(classificationTreeMlModel.getTrainSize(),
				Double.parseDouble(rb
						.getString(ModelConstants.KEY_TRAINING_SIZE)), delta);
		assertEquals(classificationTreeMlModel.getTestSize(),
				Double.parseDouble(rb.getString(ModelConstants.KEY_TEST_SIZE)),
				delta);
		assertEquals(classificationTreeMlModel.getCategoricalFeaturesInfo(),
				new HashMap<Integer, Integer>());
		assertEquals(classificationTreeMlModel.getImpurity(),
				rb.getString(ModelConstants.KEY_CLASSIFIER_IMPURITY));
		assertEquals(classificationTreeMlModel.getMaxBins(),
				Double.parseDouble(rb
						.getString(ModelConstants.KEY_CLASSIFIER_MAX_BIN)),
				delta);
		assertEquals(classificationTreeMlModel.getMaxDepth(),
				Double.parseDouble(rb
						.getString(ModelConstants.KEY_CLASSIFIER_MAX_DEPTH)),
				delta);
		assertEquals(
				classificationTreeMlModel.getNumClasses(),
				Integer.parseInt(rb.getString(ModelConstants.KEY__NUM_CLASSES)),
				delta);
		assertEquals(classificationTreeMlModel.getModelLocation(),
				rb.getString(ModelConstants.KEY_DECISION_TREE_MODEL_LOCATION));

	}

	/**
	 * Test method for
	 * {@link com.weatherpred.util.MLModelUtil#populateModelParams(com.weatherpred.mlmodel.DecisionTreeRegressionModel, com.weatherpred.enums.WeatherParameter)}
	 * .
	 */
	@Test
	public void testPopulateModelParamsDecisionTreeRegressionModelWeatherParameter() {
		assertEquals(regressionTreeMlModel.getTrainSize(),
				Double.parseDouble(rb
						.getString(ModelConstants.KEY_TRAINING_SIZE)), delta);
		assertEquals(regressionTreeMlModel.getTestSize(),
				Double.parseDouble(rb.getString(ModelConstants.KEY_TEST_SIZE)),
				delta);
		assertEquals(regressionTreeMlModel.getCategoricalFeaturesInfo(),
				new HashMap<Integer, Integer>());
		assertEquals(regressionTreeMlModel.getImpurity(),
				rb.getString(ModelConstants.KEY_TEMP_MODEL_IMPURITY));
		assertEquals(regressionTreeMlModel.getMaxBins(), Double.parseDouble(rb
				.getString(ModelConstants.KEY_TEMP_MODEL_MAX_BIN)), delta);
		assertEquals(regressionTreeMlModel.getMaxDepth(), Double.parseDouble(rb
				.getString(ModelConstants.KEY_TEMP_MODEL_MAX_DEPTH)), delta);
		assertEquals(regressionTreeMlModel.getModelLocation(),
				rb.getString(ModelConstants.KEY_TEMP_MODEL_LOCATION));
	}

	/**
	 * Test method for
	 * {@link com.weatherpred.util.MLModelUtil#getWeatherDataLocation()}.
	 */
	@Test
	public void testGetWeatherDataLocation() {
		assertEquals(MLModelUtil.getWeatherDataLocation(),
				rb.getString(ModelConstants.WEATHER_DATA_LOCATION));
	}

}
