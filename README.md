# WeatherPredictor
Weather prediction using machine learning models on Spark and Java.

# Problem Statement
To simulate an environment (taking into account atmosphere, topography, geography, or similar) that evolves over time and generate weather conditions for a specified location and time.

## Possible Solutions 
1. Using Mathematical models or equations to predict the weather based on current weather conditions.  
2. Collect historical weather data and observe the changes in weather based on parameters like humidity, pressure, location (lat/long),etc to predict future values.
3. ....(probably more)

# The Machine learning approach
The second approach is used in this project, where weather prediction is made using Machine Learning algorithms on Spark Mllib using historical weather data. The basic premise of machine learning is to build algorithmic models that receive input data and use statistical analysis to predict an output value within an acceptable range. The input weather data can be obtained from API services or verified weather information websites. For example, [Wunderground](https://wunderground.com/) and [WorldWeatherOnline](https://developer.worldweatheronline.com/).
In this solution, there are four values which we are trying to predict : <br />
**1. Temperature** <br />
**2. Pressure** <br />
**3. Humidity** <br />
**4. Weather Condition - Rain/Snow/Sunny** <br />
The first three predicates are **continuous** values which need a **regression model** for prediction. For this, two algorithms were used - **Linear Regression** and **Decision Tree Regression** and models were created for the same. On comparing the results, **Decision Tree Regression** provided more accurate results. (Refer branch [WithLinearRegression](https://github.com/PoornimaTom/WeatherPredictionOnSpark/tree/WithLinearRegression) for implementation and comparison with Linear Regression Model) For predicting the weather condition (Rain, Snow or Sunny), **Decision Tree Classifier** was used. 

Here I have used [spark.mllib](https://spark.apache.org/docs/1.6.0/mllib-guide.html) library to implement regression and classification. MLlib is Spark's machine learning(ML) library which makes practical machine learning scalable and easy.

## Machine Learning Workflow
![alt text](https://github.com/PoornimaTom/WeatherPredictionOnSpark/blob/master/images/Steps-to-Predictive-Modelling.jpg)
Image Courtesy : [upxacademy](https://upxacademy.com/introduction-machine-learning/)

| Predicted Value| Input Feature Set| Algorithm Used|
| --- | --- |--- |
| Temperature/Pressure/Humidity        |     { latitude, longitude, elevation, month, hour of the day }      |          Decision Tree Regression |
| Weather Condition  (Rain/Snow/Sunny)| { humidity, pressure, temperature, latitude, longitude, elevation, month, hour of the day }     | Decision Tree Classification |

## Decision Trees
If you are a beginner to Machine Learning, this section will help you understand the basics of Decision Tree and how we have used it to solve the problem at hand.

Decision tree builds classification or regression models in the form of a tree structure. A dataset is incrementally broken down into smaller subsets simultaneously developing an associated decision tree.  Each subset is chosen greedily by selecting the best split from a set of possible splits, in order to maximize the information gain at a tree node.The final result is a tree with **decision nodes** and **leaf nodes**. A decision node (e.g. Latitude, Pressure, Humidity) has two or more branches split based on its values. For classification problems, leaf node represents a classification or decision like *Rain*. For regression problems, leaf node represents a predicted numerical value like *temperature value*. The below diagram shows how we have used Decision Tree classification to predict the weather condition. 

![alt text](https://github.com/PoornimaTom/WeatherPredictionOnSpark/blob/master/images/Decision%20Tree.jpg)

## Prerequisites
[Java 1.7](https://java.com/en/download/) and [Apache Spark 1.6.0](https://spark.apache.org/releases/spark-release-1-6-0.html) must be installed in the system.

# How to run the application ?
Step 1: Modify Property file **model.properties** accordingly to tune the algorithm and to specify input/output file locations. Input dataset used is weather_data.csv.

Step 2: Build and evaluate the ML models; a classifier for weather condition (SUNNY/RAIN/SNOW) and a regression model each for temperature, humidity and pressure.

Step 3: Predict weather for a given latitude, longitude, elevation and time using the models built in step 2.
Predicted output format : Location|latitude,longitude,elevation|Timestamp|WeatherCondition|Temperature|Pressure|Humidity

## Build the maven project
```
mvn clean install package
```

## Run the project

### To build and evaluate Models:
```
spark-submit --class com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder <jarlocation>
spark-submit --class com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder <jarlocation>

eg: spark-submit --class com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder WeatherPredictionOnSpark-0.0.1-SNAPSHOT.jar
    spark-submit --class com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder WeatherPredictionOnSpark-0.0.1-SNAPSHOT.jar

```

### To Predict Weather

```
spark-submit --class com.weatherpred.application.WeatherPredictor <jarlocation> --lat <latitude> --long  <longitude> --ele  <elevation> --time <unixTimeStamp> --out <outputLocation>

eg: spark-submit --class com.weatherpred.application.WeatherPredictor WeatherPredictionOnSpark-0.0.1-SNAPSHOT.jar --lat 24.8614622 --long 67.0099388 --ele 9.870092392 --time 1423123200 --out /home/user/output.txt
```

## Expected Output:

NA|24.8614622,67.0099388,9.870092392|2015-02-05T08:00:00Z|SUNNY|70.98783018867924|1004.0401934972245|0.47326432022084186


## Command line arguments 

```
--help          Displays help  
--lat            **Latitiude of the location 
--long           **Longitude of the location
--ele            **Elevation of the location 
--time           **Unix TimeStamp
--out            **Output Location

**  -> Mandatory arguments  
