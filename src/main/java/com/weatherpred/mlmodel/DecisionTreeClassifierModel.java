package com.weatherpred.mlmodel;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

import scala.Tuple2;

import com.weatherpred.exceptions.WeatherPredException;

/**
 * 
 * Class to implement a decision tree model for classification
 * 
 * Date Aug 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 0.1
 *
 *
 */

public class DecisionTreeClassifierModel implements AbstractMlModel,
		Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(DecisionTreeClassifierModel.class);

	/**
	 * training data size
	 */
	private double trainSize;
	/**
	 * testing data size
	 */
	private double testSize;
	/**
	 * Number of classes
	 */
	private int numClasses;
	/**
	 * impurity
	 */
	private String impurity;
	/**
	 * Max Depth
	 */
	private int maxDepth;
	/**
	 * Max Bins
	 */
	private int maxBins;
	/**
	 * categoricalFeaturesInfo
	 */
	private Map<Integer, Integer> categoricalFeaturesInfo;
	/**
	 * Model
	 */
	private DecisionTreeModel model;
	/**
	 * modelLocation
	 */
	private String modelLocation;

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

	public int getNumClasses() {
		return numClasses;
	}

	public void setNumClasses(int numClasses) {
		this.numClasses = numClasses;
	}

	public String getImpurity() {
		return impurity;
	}

	public void setImpurity(String impurity) {
		this.impurity = impurity;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getMaxBins() {
		return maxBins;
	}

	public void setMaxBins(int maxBins) {
		this.maxBins = maxBins;
	}

	public Map<Integer, Integer> getCategoricalFeaturesInfo() {
		return categoricalFeaturesInfo;
	}

	public void setCategoricalFeaturesInfo(
			Map<Integer, Integer> categoricalFeaturesInfo) {
		this.categoricalFeaturesInfo = categoricalFeaturesInfo;
	}

	public DecisionTreeModel getModel() {
		return model;
	}

	public void setModel(DecisionTreeModel model) {
		this.model = model;
	}

	public String getModelLocation() {
		return modelLocation;
	}

	public void setModelLocation(String modelLocation) {
		this.modelLocation = modelLocation;
	}

	@Override
	public void trainModel(JavaRDD<LabeledPoint> trainingData)
			throws WeatherPredException {
		model = DecisionTree.trainClassifier(trainingData, numClasses,
				categoricalFeaturesInfo, impurity, maxDepth, maxBins);
	}

	@Override
	public Double evaluateModel(JavaRDD<LabeledPoint> testData)
			throws WeatherPredException {
		JavaPairRDD<Double, Double> predictionAndLabel = testData
				.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<Double, Double> call(LabeledPoint p) {
						return new Tuple2<Double, Double>(getModel().predict(
								p.features()), p.label());
					}
				});

		// Evaluate model on test instances and compute test error
		Double testErr = 1.0
				* predictionAndLabel.filter(
						new Function<Tuple2<Double, Double>, Boolean>() {

							private static final long serialVersionUID = 1L;

							@Override
							public Boolean call(Tuple2<Double, Double> pl) {
								return !pl._1().equals(pl._2());
							}
						}).count() / testData.count();

		return testErr;
	}

}
