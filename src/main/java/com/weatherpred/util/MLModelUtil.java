package com.weatherpred.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.weatherpred.enums.WeatherParameter;
import com.weatherpred.exceptions.WeatherPredException;
import com.weatherpred.mlmodel.DecisionTreeClassifierModel;
import com.weatherpred.mlmodel.DecisionTreeRegressionModel;
import com.weatherpred.utils.constants.Constants;
import com.weatherpred.utils.constants.ModelConstants;

/**
 * Class for model utility methods
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class MLModelUtil {
	final static Logger logger = Logger.getLogger(MLModelUtil.class);

	/**
	 * Method to populate the algorithmic parameters for Decision tree
	 * Classifier
	 * 
	 * @param DecisionTreeClassifierModel
	 * @return DecisionTreeClassifierModel with parameters
	 */
	public static DecisionTreeClassifierModel populateModelParams(
			DecisionTreeClassifierModel decisionTreeMlModel) {
		try {
			final ResourceBundle rb = ResourceBundle
					.getBundle(Constants.MODEL_RESOURCE_BUNDLE);
			decisionTreeMlModel.setTrainSize(Double.parseDouble(rb
					.getString(ModelConstants.KEY_TRAINING_SIZE)));
			decisionTreeMlModel.setTestSize(Double.parseDouble(rb
					.getString(ModelConstants.KEY_TEST_SIZE)));
			/**
			 * Empty categoricalFeaturesInfo indicates all features are
			 * continuous.
			 */
			decisionTreeMlModel
					.setCategoricalFeaturesInfo(new HashMap<Integer, Integer>());
			decisionTreeMlModel.setImpurity(rb
					.getString(ModelConstants.KEY_CLASSIFIER_IMPURITY));
			decisionTreeMlModel.setMaxBins(Integer.parseInt(rb
					.getString(ModelConstants.KEY_CLASSIFIER_MAX_BIN)));
			decisionTreeMlModel.setMaxDepth(Integer.parseInt(rb
					.getString(ModelConstants.KEY_CLASSIFIER_MAX_DEPTH)));
			decisionTreeMlModel.setNumClasses(Integer.parseInt(rb
					.getString(ModelConstants.KEY__NUM_CLASSES)));
			decisionTreeMlModel
					.setModelLocation(rb
							.getString(ModelConstants.KEY_DECISION_TREE_MODEL_LOCATION));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return decisionTreeMlModel;
	}

	/**
	 * Method to populate the algorithmic parameters for Decision Tree
	 * Regression Model
	 * 
	 * @param decisionTreeRegressionModel
	 * @param weatherParameter
	 * @return decisionTreeRegressionModel with parameters
	 */
	public static DecisionTreeRegressionModel populateModelParams(
			DecisionTreeRegressionModel decisionTreeRegressionModel,
			WeatherParameter weatherParameter) {
		try {
			final ResourceBundle rb = ResourceBundle
					.getBundle(Constants.MODEL_RESOURCE_BUNDLE);
			decisionTreeRegressionModel.setTrainSize(Double.parseDouble(rb
					.getString(ModelConstants.KEY_TRAINING_SIZE)));
			decisionTreeRegressionModel.setTestSize(Double.parseDouble(rb
					.getString(ModelConstants.KEY_TEST_SIZE)));
			/**
			 * Empty categoricalFeaturesInfo indicates all features are
			 * continuous.
			 */
			decisionTreeRegressionModel
					.setCategoricalFeaturesInfo(new HashMap<Integer, Integer>());

			switch (weatherParameter) {

			case TEMPERATURE:
				decisionTreeRegressionModel.setImpurity(rb
						.getString(ModelConstants.KEY_TEMP_MODEL_IMPURITY));
				decisionTreeRegressionModel.setMaxBins(Integer.parseInt(rb
						.getString(ModelConstants.KEY_TEMP_MODEL_MAX_BIN)));
				decisionTreeRegressionModel.setMaxDepth(Integer.parseInt(rb
						.getString(ModelConstants.KEY_TEMP_MODEL_MAX_DEPTH)));
				decisionTreeRegressionModel.setModelLocation(rb
						.getString(ModelConstants.KEY_TEMP_MODEL_LOCATION));
				break;
			case HUMIDITY:
				decisionTreeRegressionModel.setImpurity(rb
						.getString(ModelConstants.KEY_HUMIDITY_MODEL_IMPURITY));
				decisionTreeRegressionModel.setMaxBins(Integer.parseInt(rb
						.getString(ModelConstants.KEY_HUMIDITY_MODEL_MAX_BIN)));
				decisionTreeRegressionModel
						.setMaxDepth(Integer.parseInt(rb
								.getString(ModelConstants.KEY_HUMIDITY_MODEL_MAX_DEPTH)));
				decisionTreeRegressionModel.setModelLocation(rb
						.getString(ModelConstants.KEY_HUMIDITY_MODEL_LOCATION));
				break;
			case PRESSURE:
				decisionTreeRegressionModel.setImpurity(rb
						.getString(ModelConstants.KEY_PRESSURE_MODEL_IMPURITY));
				decisionTreeRegressionModel.setMaxBins(Integer.parseInt(rb
						.getString(ModelConstants.KEY_PRESSURE_MODEL_MAX_BIN)));
				decisionTreeRegressionModel
						.setMaxDepth(Integer.parseInt(rb
								.getString(ModelConstants.KEY_PRESSURE_MODEL_MAX_DEPTH)));
				decisionTreeRegressionModel.setModelLocation(rb
						.getString(ModelConstants.KEY_PRESSURE_MODEL_LOCATION));
				break;

			default:
				throw new WeatherPredException("Invalid weather parameter");

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return decisionTreeRegressionModel;
	}

	/**
	 * Method to retrieve location of input weather data
	 * 
	 * @return location of input weather data
	 */
	public static String getWeatherDataLocation() {
		try {
			Locale locale = new Locale("en", "IN");
			final ResourceBundle rb = ResourceBundle
					.getBundle(Constants.MODEL_RESOURCE_BUNDLE, locale);
			return (rb.getString(ModelConstants.WEATHER_DATA_LOCATION));
		} catch (Exception e) {

			logger.error(e.getMessage());
			return null;
		}
	}

}
