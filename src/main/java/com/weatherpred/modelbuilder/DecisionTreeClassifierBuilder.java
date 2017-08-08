package com.weatherpred.modelbuilder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weatherpred.mlmodel.DecisionTreeClassifierModel;
import com.weatherpred.util.CommonUtil;
import com.weatherpred.util.MLModelUtil;
import com.weatherpred.utils.constants.Constants;
import com.weatherpred.utils.constants.ModelConstants;
import com.weatherpred.utils.constants.NumericMapping;

/**
 * Class to build a Decision Tree Classifier Model with required algorithmic
 * parameters
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */

public class DecisionTreeClassifierBuilder {

	/**
	 * Logger
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(DecisionTreeClassifierBuilder.class);

	/**
	 * Decision Tree Machine Learning model
	 */
	private static DecisionTreeClassifierModel decisionTreeClassifierModel;

	/**
	 * Get DecisionTree Classifier Model
	 */
	public DecisionTreeClassifierModel getDecisionTreeClassifierModel() {
		return decisionTreeClassifierModel;
	}

	/**
	 * The static block populates the model with required ML parameters
	 */
	static {
		decisionTreeClassifierModel = MLModelUtil
				.populateModelParams(new DecisionTreeClassifierModel());
	}

	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf().setAppName(
				Constants.DECISION_TREE_BUILDER_APP_NAME).setMaster(
				Constants.LOCAL_STRING);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);

		try {

			// Load weather data from CSV file
			JavaRDD<String> weatherData = jsc.textFile(MLModelUtil
					.getWeatherDataLocation());

			// Transform the non-numeric features into numeric values
			JavaRDD<String> transformedWeatherData = weatherData
					.map(transformData);

			// Convert transformed RDD to RDD of Labeled Points
			JavaRDD<LabeledPoint> labelledData = transformedWeatherData
					.map(createLabelledPointData);

			// Split the data into training and test sets (by default 7:3)
			JavaRDD<LabeledPoint>[] splits = labelledData
					.randomSplit(new double[] {
							decisionTreeClassifierModel.getTrainSize(),
							decisionTreeClassifierModel.getTestSize() });
			JavaRDD<LabeledPoint> trainingData = splits[0];
			JavaRDD<LabeledPoint> testData = splits[1];

			// Train the decision tree model
			decisionTreeClassifierModel.trainModel(trainingData);
			// Save the decision tree model
			decisionTreeClassifierModel.getModel().save(jsc.sc(),
					decisionTreeClassifierModel.getModelLocation());

			// Evaluate the decision tree classifier model on test instances and
			// compute test error
			Double testErr = decisionTreeClassifierModel
					.evaluateModel(testData);

			logger.info("Test Error: " + testErr);
			logger.info("Built decision tree model:\n"
					+ decisionTreeClassifierModel.getModel().toDebugString());

		} catch (Exception e) {

			logger.error(e.getMessage());
		}
		jsc.close();
		jsc.stop();
	}

	/**
	 * Function to transform each input element : converts categorical feature
	 * to numerical
	 */
	public static Function<String, String> transformData = new Function<String, String>() {

		private static final long serialVersionUID = 1L;

		StringBuilder strBuilder = null;

		@Override
		public String call(String line) throws Exception {
			String[] parts = line.split(ModelConstants.DELIMITTER_COMA);
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
			strBuilder = new StringBuilder();

			for (int i = 0; i < parts.length; i++) {
				strBuilder.append(parts[i]);
				strBuilder.append(ModelConstants.DELIMITTER_COMA);
			}

			// Remove extra comma
			if (strBuilder.length() > 0) {
				strBuilder.setLength(strBuilder.length() - 1);
			}

			return strBuilder.toString();
		}
	};

	/**
	 * Function to transform each input element to LabeledPoint
	 */
	public static Function<String, LabeledPoint> createLabelledPointData = new Function<String, LabeledPoint>() {

		private static final long serialVersionUID = 1L;

		public LabeledPoint call(String line) throws Exception {

			String[] parts = line.split(ModelConstants.DELIMITTER_COMA);
			return new LabeledPoint(
					Double.parseDouble(parts[NumericMapping.WEATHER_STATUS_INDEX]),
					Vectors.dense(
							Double.parseDouble(parts[NumericMapping.HUMIDITY_INDEX]),
							Double.parseDouble(parts[NumericMapping.LAT_INDEX]),
							Double.parseDouble(parts[NumericMapping.LONG_INDEX]),
							Double.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
							Double.parseDouble(parts[NumericMapping.PRESSURE_INDEX]),
							Double.parseDouble(parts[NumericMapping.TEMPERATURE_INDEX]),
							CommonUtil
									.getMonth(parts[NumericMapping.TIME_INDEX]),
							CommonUtil
									.getHour(parts[NumericMapping.TIME_INDEX])));
		}
	};

}
