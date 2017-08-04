package com.weatherpred.mlmodel;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;

import scala.Tuple2;

import com.weatherpred.exceptions.WeatherPredException;

/**
 * 
 * Class to implement a linear regression model
 * 
 * Date Aug 2, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 0.1
 *
 *
 */

public class LinearRegressionMlModel implements AbstractMlModel, Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(LinearRegressionMlModel.class);

	/**
	 * model
	 */
	private LinearRegressionModel model;
	/**
	 * numIterations
	 */
	private int numIterations;
	/**
	 * stepSize
	 */
	private double stepSize;
	/**
	 * modelLocation
	 */
	private String modelLocation;
	/**
	 * trainSize
	 */
	private double trainSize;
	/**
	 * testSize
	 */
	private double testSize;

	public LinearRegressionModel getModel() {
		return model;
	}

	public void setModel(LinearRegressionModel model) {
		this.model = model;
	}

	public int getNumIterations() {
		return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public double getStepSize() {
		return stepSize;
	}

	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
	}

	public String getModelLocation() {
		return modelLocation;
	}

	public void setModelLocation(String modelLocation) {
		this.modelLocation = modelLocation;
	}

	public double getTrainSize() {
		return trainSize;
	}

	public void setTrainSize(double trainSize) {
		this.trainSize = trainSize;
	}

	public double getTestSize() {
		return testSize;
	}

	public void setTestSize(double testSize) {
		this.testSize = testSize;
	}

	@Override
	public void trainModel(JavaRDD<LabeledPoint> trainingData)
			throws WeatherPredException {
		model = LinearRegressionWithSGD.train(JavaRDD.toRDD(trainingData),
				numIterations, stepSize);
	}

	@Override
	public Double evaluateModel(JavaRDD<LabeledPoint> testData)
			throws WeatherPredException {

		// Evaluate model on training examples and compute training error
		JavaRDD<Tuple2<Double, Double>> valuesAndPreds = testData
				.map(new Function<LabeledPoint, Tuple2<Double, Double>>() {

					private static final long serialVersionUID = 1L;

					public Tuple2<Double, Double> call(LabeledPoint point) {
						double prediction = model.predict(point.features());
						return new Tuple2<Double, Double>(prediction, point
								.label());
					}
				});

		double MSE = new JavaDoubleRDD(valuesAndPreds.map(
				new Function<Tuple2<Double, Double>, Object>() {

					private static final long serialVersionUID = 1L;

					public Object call(Tuple2<Double, Double> pair) {
						return Math.pow(pair._1() - pair._2(), 2.0);
					}
				}).rdd()).mean();

		return MSE;
	}
}
