package com.weatherpred.application;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weatherpred.dto.InputFeaturesDTO;
import com.weatherpred.dto.WeatherDTO;
import com.weatherpred.enums.WeatherParameter;
import com.weatherpred.exceptions.WeatherPredException;
import com.weatherpred.mlmodel.DecisionTreeClassifierModel;
import com.weatherpred.mlmodel.DecisionTreeRegressionModel;
import com.weatherpred.util.CmdLineHelper;
import com.weatherpred.util.CommonUtil;
import com.weatherpred.util.MLModelUtil;
import com.weatherpred.utils.constants.Constants;

/**
 * Main class for getting weather prediction
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */

public class WeatherPredictor {
	private final static Logger logger = LoggerFactory
			.getLogger(WeatherPredictor.class);

	/**
	 * Regression model for Temperature Prediction
	 */
	private static DecisionTreeRegressionModel temperatureModel;
	/**
	 * Regression model for Humidity Prediction
	 */
	private static DecisionTreeRegressionModel humidityModel;
	/**
	 * Regression model for Pressure Prediction
	 */
	private static DecisionTreeRegressionModel pressureModel;
	/**
	 * Classifier model for Weather Prediction
	 */
	private static DecisionTreeClassifierModel classifierModel;

	/**
	 * @return temperatureModel
	 */
	public DecisionTreeRegressionModel getTemperatureModel() {
		return temperatureModel;
	}

	/**
	 * @return humidityModel
	 */
	public DecisionTreeRegressionModel getHumidityModel() {
		return humidityModel;
	}

	/**
	 * @return pressureModel
	 */
	public DecisionTreeRegressionModel getPressureModel() {
		return pressureModel;
	}

	/**
	 * @return classifierModel
	 */
	public DecisionTreeClassifierModel getClassifierModel() {
		return classifierModel;
	}

	/**
	 * Input Features from command line
	 */
	private static InputFeaturesDTO inputFeatures;
	/**
	 * Predicted Weather output
	 */
	private static WeatherDTO weatherDTO;
	/**
	 * The static block loads and populate all the data for the required models
	 * 
	 */

	static {
		temperatureModel = MLModelUtil
				.populateModelParams(new DecisionTreeRegressionModel(),
						WeatherParameter.TEMPERATURE);
		humidityModel = MLModelUtil.populateModelParams(
				new DecisionTreeRegressionModel(), WeatherParameter.HUMIDITY);
		pressureModel = MLModelUtil.populateModelParams(
				new DecisionTreeRegressionModel(), WeatherParameter.PRESSURE);
		classifierModel = MLModelUtil
				.populateModelParams(new DecisionTreeClassifierModel());
		weatherDTO = new WeatherDTO();
	}

	/**
	 * Main method for weather prediction
	 * 
	 * @param args
	 * @throws WeatherPredException
	 */
	public static void main(String[] args) throws WeatherPredException {

		SparkConf sparkConf = new SparkConf().setAppName(
				Constants.WEATHER_APP_NAME).setMaster(Constants.LOCAL_STRING);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);

		inputFeatures = new CmdLineHelper(args).parse();

		try {

			// // Including TimeStamp, creating vector to predict regression
			Vector inputDataRegression = Vectors.sparse(5, new int[] { 0, 1, 2,
					3, 4 }, new double[] { inputFeatures.getLatitude(),
					inputFeatures.getLongitude(), inputFeatures.getElevation(),
					CommonUtil.getMonth(inputFeatures.getUnixTime()),
					CommonUtil.getHour(inputFeatures.getUnixTime()) });

			double temperature = DecisionTreeModel.load(jsc.sc(),
					temperatureModel.getModelLocation()).predict(
					inputDataRegression);
			weatherDTO.setTemperature(temperature);

			double humidity = DecisionTreeModel.load(jsc.sc(),
					humidityModel.getModelLocation()).predict(
					inputDataRegression);
			weatherDTO.setHumidity(humidity);

			double pressure = DecisionTreeModel.load(jsc.sc(),
					pressureModel.getModelLocation()).predict(
					inputDataRegression);
			weatherDTO.setPressure(pressure);

			Vector testDataClassifier = Vectors.sparse(
					8,
					new int[] { 0, 1, 2, 3, 4, 5, 6, 7 },
					new double[] { humidity, inputFeatures.getLatitude(),
							inputFeatures.getLongitude(),
							inputFeatures.getElevation(), pressure,
							temperature,
							CommonUtil.getMonth(inputFeatures.getUnixTime()),
							CommonUtil.getHour(inputFeatures.getUnixTime()) });

			double weather = DecisionTreeModel.load(jsc.sc(),
					classifierModel.getModelLocation()).predict(
					testDataClassifier);

			weatherDTO.setWeatherStatus(CommonUtil.findWeatherStatus(weather));

			weatherDTO.setLocation(CommonUtil.findLocation(
					inputFeatures.getLatitude(), inputFeatures.getLongitude(),
					inputFeatures.getElevation()));

			weatherDTO.setLatitude(inputFeatures.getLatitude());
			weatherDTO.setLongitude(inputFeatures.getLongitude());
			weatherDTO.setElevation(inputFeatures.getElevation());
			weatherDTO.setTime(CommonUtil.epochConverter(inputFeatures
					.getUnixTime()));

			// Write output to specified location
			CommonUtil.saveOutput(weatherDTO, inputFeatures.getOutLocation());

		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			jsc.close();
			jsc.stop();

		}
	}
}
