# WeatherPredictor
A Machine Learning model for weather prediction using Java and Spark

## Background
The problem was to simulate an environment (taking into account atmosphere, topography, geography, or similar) that evolves over time. Then, generate weather conditions for a specified location and time.

This can be solved in several ways:  
1. Using Mathematical models or equations to predict the weather based on current weather conditions.  
2. Collect historical weather data and observe the changes in weather based on parameters like humidity, pressure, location (lat/long), etc. 
3. ....(probably more)

The second approach is used in this project, where weather prediction is made using Machine Learning algorithms on Spark Mllib using historical weather data.

#### The Machine learning approach
The basic premise of machine learning is to build algorithmic models that receive input data and use statistical analysis to predict an output value within an acceptable range. The input weather data can be obtained from api services or verified weather information websites. For example,
* [OpenWeatherMap](https://openweathermap.org/api)
* [WorldWeatherOnline](https://developer.worldweatheronline.com/)


Weather prediction involves prediction of temperature, pressure, humidity and weather condition. The first three predicates are **continuous** values which needs a **regression model** for prediction. The weather condition can be either of Rain, Snow or Sunny, predicting which is a **classification problem**. For predicting humidity, pressure and temperature, **linear regression** was used and models were created for the same. For predicting weather condition, **Decision Tree Classifier** was used.

Here I have used [spark.mllib](https://spark.apache.org/docs/1.6.0/mllib-guide.html) library for the decision tree regression and classification. Spark Provides support of big data frameworks, which would help in production level deployment


## Prerequisite

[Java 1.7](https://java.com/en/download/) and [Apache Spark 1.6.0](https://spark.apache.org/releases/spark-release-1-6-0.html) must be installed in the system.

## How to run the application ?

Step 1: Build the decision tree models; a classifier for weather condition (SUNNY/RAIN/SNOW) and regression model each for temperature, humidity and pressure. 
Change PropertyFile **model.properties** accordingly to tune the algorithm.


## Build the maven project
```
mvn clean install  
```

## Run the project
Follow the steps to get output  

## To build and evaluate Models:
```
spark-submit --class com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder <jarlocation>
spark-submit --class com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder <jarlocation>

eg: spark-submit --class com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder WeatherPrediction-0.0.1-SNAPSHOT.jar
    spark-submit --class com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder WeatherPrediction-0.0.1-SNAPSHOT.jar
```

## To Predict Weather

```
spark-submit --class com.weatherpred.application.WeatherPredictor <jarlocation> --lat <latitude> --long  <longitude> --ele  <elevation> --time <unixTimeStamp> --out <outputLocation>

eg: spark-submit --class com.weatherpred.application.WeatherPredictor WeatherPredictor.jar --lat 34.0522342 --long  -118.2436849 --ele  86.8470916748 --time 1420034400 --out /home/user/output.txt
```


## Command line arguments 

```
--help          Displays help  
--lat            **Latitiude of the location 
--long           **Longitude of the location
--ele            **Elevation of the location 
--time           **Unix TimeStamp
--out		     **Output Location


**  -> Mandatory arguments  
