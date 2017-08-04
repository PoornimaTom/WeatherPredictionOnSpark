package com.weatherpred.modelbuilder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weatherpred.enums.WeatherParameter;
import com.weatherpred.exceptions.WeatherPredException;
import com.weatherpred.mlmodel.DecisionTreeRegressionModel;
import com.weatherpred.util.CommonUtil;
import com.weatherpred.util.MLModelUtil;
import com.weatherpred.utils.constants.Constants;
import com.weatherpred.utils.constants.ModelConstants;
import com.weatherpred.utils.constants.NumericMapping;

/**
 * Class to build Decision Tree Regression Models with required algorithmic
 * parameters
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class DecisionTreeRegressionBuilder {
	/**
	 * Logger
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(DecisionTreeRegressionBuilder.class);

	/**
	 * Model to predict Temperature
	 */
	private static DecisionTreeRegressionModel temperatureModel;
	/**
	 * Model to predict Humidity
	 */
	private static DecisionTreeRegressionModel humidityModel;
	/**
	 * Model to predict predict Pressure
	 */
	private static DecisionTreeRegressionModel pressureModel;

	/**
	 * @return temperature model
	 */
	public DecisionTreeRegressionModel getTemperatureModel() {
		return temperatureModel;
	}

	/**
	 * @return humidity model
	 */
	public DecisionTreeRegressionModel getHumidityModel() {
		return humidityModel;
	}

	/**
	 * @return pressure model
	 */
	public DecisionTreeRegressionModel getPressureModel() {
		return pressureModel;
	}
	
	/**
	 * The static block populates the models with required ML parameters
	 */
	static {
		temperatureModel = MLModelUtil
				.populateModelParams(new DecisionTreeRegressionModel(),
						WeatherParameter.TEMPERATURE);
		humidityModel = MLModelUtil.populateModelParams(
				new DecisionTreeRegressionModel(), WeatherParameter.HUMIDITY);
		pressureModel = MLModelUtil.populateModelParams(
				new DecisionTreeRegressionModel(), WeatherParameter.PRESSURE);
	}

	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName(
				Constants.DECISION_TREE_REGRESSION_BUILDER_APP_NAME).setMaster(
				Constants.LOCAL_STRING);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);

		try {

			// Load weather data from CSV file
			JavaRDD<String> weatherData = jsc.textFile(MLModelUtil
					.getWeatherDataLocation());

			// Split weather data into training and test sets
			JavaRDD<LabeledPoint>[] temperatureSplit = getTrainingAndTestSplit(
					weatherData, WeatherParameter.TEMPERATURE);
			JavaRDD<LabeledPoint>[] humiditySplit = getTrainingAndTestSplit(
					weatherData, WeatherParameter.HUMIDITY);
			JavaRDD<LabeledPoint>[] pressureSplit = getTrainingAndTestSplit(
					weatherData, WeatherParameter.PRESSURE);

			// Train and save the DecisionTree Regression models
			// Training Temperature Model
			temperatureModel.trainModel(temperatureSplit[0]);
			// Saving Temperature Model
			temperatureModel.getModel().save(jsc.sc(),
					temperatureModel.getModelLocation());
			// Training Humidity Model
			humidityModel.trainModel(humiditySplit[0]);
			// Saving Humidity Model
			humidityModel.getModel().save(jsc.sc(),
					humidityModel.getModelLocation());
			// Training Pressure Model
			pressureModel.trainModel(pressureSplit[0]);
			// Saving Pressure Model
			pressureModel.getModel().save(jsc.sc(),
					pressureModel.getModelLocation());

			logger.info("Model training completed");

			// Evaluate each model and compute test error
			logger.info("Evaluating Models");
			Double temperatureTestErr = temperatureModel
					.evaluateModel(temperatureSplit[1]);
			logger.info("Temperature Model MSE = " + temperatureTestErr);
			Double humidityTestErr = humidityModel
					.evaluateModel(humiditySplit[1]);
			logger.info("Humidity Model MSE = " + humidityTestErr);
			Double pressureTestErr = pressureModel
					.evaluateModel(pressureSplit[1]);
			logger.info("Pressure Model MSE = " + pressureTestErr);

		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			jsc.close();
			jsc.stop();

		}

	}

	/**
	 * To convert the weather data to Labeled points and split into training and
	 * test sets
	 * 
	 * @param weatherData
	 * @param weatherParameter
	 * @return RDD of LabeledPoint Array
	 */
	private static JavaRDD<LabeledPoint>[] getTrainingAndTestSplit(
			JavaRDD<String> weatherData, WeatherParameter weatherParameter) {
		try {

			switch (weatherParameter) {
			case TEMPERATURE:
				// Convert weather RDD to RDD of Labeled Points
				JavaRDD<LabeledPoint> labelledPointForTemperature = weatherData
						.map(createLabelledPointForTemp);

				// Split the data into training and test sets (by default 7:3)
				JavaRDD<LabeledPoint>[] tempSplit = labelledPointForTemperature
						.randomSplit(new double[] {
								temperatureModel.getTrainSize(),
								temperatureModel.getTestSize() });
				return (tempSplit);

			case HUMIDITY:
				// Convert weather RDD to RDD of Labeled Points
				JavaRDD<LabeledPoint> labelledPointForHumidity = weatherData
						.map(createLabelledPointForHumidity);

				// Split the data into training and test sets (by default 7:3)
				JavaRDD<LabeledPoint>[] humiditySplit = labelledPointForHumidity
						.randomSplit(new double[] {
								humidityModel.getTrainSize(),
								humidityModel.getTestSize() });
				return (humiditySplit);

			case PRESSURE:
				// Convert weather RDD to RDD of Labeled Points
				JavaRDD<LabeledPoint> labelledPointForPressure = weatherData
						.map(createLabelledPointForPressure);

				// Split the data into training and test sets (by default 7:3)
				JavaRDD<LabeledPoint>[] pressureSplit = labelledPointForPressure
						.randomSplit(new double[] {
								humidityModel.getTrainSize(),
								humidityModel.getTestSize() });
				return (pressureSplit);

			default:
				throw new WeatherPredException("Invalid weather parameter");

			}

		} catch (Exception e) {

			logger.error(e.getMessage());
			return null;
		}

	}

	/**
	 * Function to transform weather data to LabeledPoints for temperature
	 * prediction
	 */
	public static Function<String, LabeledPoint> createLabelledPointForTemp = new Function<String, LabeledPoint>() {

		private static final long serialVersionUID = 1L;

		public LabeledPoint call(String line) throws Exception {

			String[] parts = line.split(ModelConstants.DELIMITTER_COMA);
			/*
			 * The features to be included for temperature prediction are
			 * specified here.
			 */
			return new LabeledPoint(
					Double.parseDouble(parts[NumericMapping.TEMPERATURE_INDEX]),
					Vectors.dense(
							Double.parseDouble(parts[NumericMapping.LAT_INDEX]),
							Double.parseDouble(parts[NumericMapping.LONG_INDEX]),
							Double.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
							CommonUtil
									.getMonth(parts[NumericMapping.TIME_INDEX]),
							CommonUtil
									.getHour(parts[NumericMapping.TIME_INDEX])));
		}
	};

	/**
	 * 
	 * Function to transform weather data to LabeledPoints for humidity
	 * prediction
	 * 
	 */
	public static Function<String, LabeledPoint> createLabelledPointForHumidity = new Function<String, LabeledPoint>() {

		private static final long serialVersionUID = 1L;

		public LabeledPoint call(String line) throws Exception {

			String[] parts = line.split(ModelConstants.DELIMITTER_COMA);
			/*
			 * The features to be included for humidity prediction are specified
			 * here.
			 */
			return new LabeledPoint(
					Double.parseDouble(parts[NumericMapping.HUMIDITY_INDEX]),
					Vectors.dense(
							Double.parseDouble(parts[NumericMapping.LAT_INDEX]),
							Double.parseDouble(parts[NumericMapping.LONG_INDEX]),
							Double.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
							CommonUtil
									.getMonth(parts[NumericMapping.TIME_INDEX]),
							CommonUtil
									.getHour(parts[NumericMapping.TIME_INDEX])));
		}
	};

	/**
	 * Function to transform weather data to LabeledPoints for pressure
	 * prediction
	 */
	public static Function<String, LabeledPoint> createLabelledPointForPressure = new Function<String, LabeledPoint>() {

		private static final long serialVersionUID = 1L;

		public LabeledPoint call(String line) throws Exception {

			String[] parts = line.split(ModelConstants.DELIMITTER_COMA);
			/*
			 * The features to be included for pressure prediction are specified
			 * here.
			 */
			return new LabeledPoint(
					Double.parseDouble(parts[NumericMapping.PRESSURE_INDEX]),
					Vectors.dense(
							Double.parseDouble(parts[NumericMapping.LAT_INDEX]),
							Double.parseDouble(parts[NumericMapping.LONG_INDEX]),
							Double.parseDouble(parts[NumericMapping.ELEVATION_INDEX]),
							CommonUtil
									.getMonth(parts[NumericMapping.TIME_INDEX]),
									CommonUtil
									.getHour(parts[NumericMapping.TIME_INDEX])));
		}
	};
}
