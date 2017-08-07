package com.weatherpred.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weatherpred.dto.InputFeaturesDTO;
import com.weatherpred.dto.WeatherDTO;
import com.weatherpred.enums.WeatherParameter;
import com.weatherpred.enums.WeatherStatus;
import com.weatherpred.exceptions.WeatherPredException;
import com.weatherpred.mlmodel.DecisionTreeClassifierModel;
import com.weatherpred.mlmodel.DecisionTreeRegressionModel;
import com.weatherpred.mlmodel.LinearRegressionMlModel;
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
	 * Linear Regression model for Temperature Prediction
	 */
	private static LinearRegressionMlModel temperatureLinearModel;
	/**
	 * Linear Regression model for Humidity Prediction
	 */
	private static LinearRegressionMlModel humidityLinearModel;
	/**
	 * Linear Regression model for Pressure Prediction
	 */
	private static LinearRegressionMlModel pressureLinearModel;
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
	 * @return temperatureLinearModel
	 */
	public LinearRegressionMlModel getTemperatureLinearModel() {
		return temperatureLinearModel;
	}

	/**
	 * @return humidityLinearModel
	 */
	public LinearRegressionMlModel getHumidityLinearModel() {
		return humidityLinearModel;
	}

	/**
	 * @return pressureLinearModel
	 */
	public LinearRegressionMlModel getPressureLinearModel() {
		return pressureLinearModel;
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
	 * Predicted Weather output List
	 */
	private static List<WeatherDTO> weatherDTOList;

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
		temperatureLinearModel = MLModelUtil.populateModelParams(
				new LinearRegressionMlModel(), WeatherParameter.TEMPERATURE);
		humidityLinearModel = MLModelUtil.populateModelParams(
				new LinearRegressionMlModel(), WeatherParameter.HUMIDITY);
		pressureLinearModel = MLModelUtil.populateModelParams(
				new LinearRegressionMlModel(), WeatherParameter.PRESSURE);

		weatherDTOList = new ArrayList<WeatherDTO>();

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

			// Creating input vector to predict regression
			Vector inputDataRegression = Vectors.sparse(5, new int[] { 0, 1, 2,
					3, 4 }, new double[] { inputFeatures.getLatitude(),
					inputFeatures.getLongitude(), inputFeatures.getElevation(),
					CommonUtil.getMonth(inputFeatures.getUnixTime()),
					CommonUtil.getHour(inputFeatures.getUnixTime()) });

			// Predicting values using Decision Tree Regression
			double temperature = DecisionTreeModel.load(jsc.sc(),
					temperatureModel.getModelLocation()).predict(
					inputDataRegression);

			double humidity = DecisionTreeModel.load(jsc.sc(),
					humidityModel.getModelLocation()).predict(
					inputDataRegression);

			double pressure = DecisionTreeModel.load(jsc.sc(),
					pressureModel.getModelLocation()).predict(
					inputDataRegression);

			// Creating input vector to predict classification
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
			WeatherStatus weatherStatus = CommonUtil.findWeatherStatus(weather);

			String location = CommonUtil.findLocation(
					inputFeatures.getLatitude(), inputFeatures.getLongitude(),
					inputFeatures.getElevation());

			double latitude = inputFeatures.getLatitude();
			double longitude = inputFeatures.getLongitude();
			double elevation = inputFeatures.getElevation();
			String time = CommonUtil
					.epochConverter(inputFeatures.getUnixTime());

			WeatherDTO weatherOutput1 = new WeatherDTO(location, latitude,
					longitude, elevation,weatherStatus, time, temperature, humidity,
					pressure);
			weatherDTOList.add(weatherOutput1);

			// Predicting values using Linear Regression
			double temperature_linear = LinearRegressionModel.load(jsc.sc(),
					temperatureLinearModel.getModelLocation()).predict(
					inputDataRegression);

			double humidity_linear = LinearRegressionModel.load(jsc.sc(),
					humidityLinearModel.getModelLocation()).predict(
					inputDataRegression);

			double pressure_linear = LinearRegressionModel.load(jsc.sc(),
					pressureLinearModel.getModelLocation()).predict(
					inputDataRegression);

			WeatherDTO weatherOutput2 = new WeatherDTO(location, latitude,
					longitude, elevation, weatherStatus, time, temperature_linear,
					humidity_linear, pressure_linear);
			weatherDTOList.add(weatherOutput2);

			// Write output to specified location
			CommonUtil.saveOutput(weatherDTOList,
					inputFeatures.getOutLocation());

		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			jsc.close();
			jsc.stop();

		}
	}
}
