package com.weatherpred.mlmodel;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;

import com.weatherpred.exceptions.WeatherPredException;

/**
 * Abstract class for Machine Learning Model
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */

public interface AbstractMlModel {

	/**
	 * For training a Machine Learning Model
	 * 
	 * @param trainingData
	 * @throws WeatherPredException
	 */
	public void trainModel(JavaRDD<LabeledPoint> trainingData)
			throws WeatherPredException;

	/**
	 * For evaluating a Machine Learning Model
	 * 
	 * @param testData
	 * @return testError
	 * @throws WeatherPredException
	 */
	public Double evaluateModel(JavaRDD<LabeledPoint> testData)
			throws WeatherPredException;
}
