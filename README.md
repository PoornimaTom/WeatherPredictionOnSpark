# WeatherPredictor
Weather prediction using machine learning models on Spark and Java.

# Problem Statement
To simulate an environment (taking into account atmosphere, topography, geography, or similar) that evolves over time and generate weather conditions for a specified location and time.

## Possible Solutions 
1. Using Mathematical models or equations to predict the weather based on current weather conditions.  
2. Collect historical weather data and observe the changes in weather based on parameters like humidity, pressure, location (lat/long),etc to predict future values.
3. ....(probably more)

# The Machine learning approach
The second approach is used in this project, where weather prediction is made using Machine Learning algorithms on Spark Mllib using historical weather data. The basic premise of machine learning is to build algorithmic models that receive input data and use statistical analysis to predict an output value within an acceptable range. The input weather data can be obtained from api services or verified weather information websites. For example,
* [Wunderground](www.wunderground.com)
* [WorldWeatherOnline](https://developer.worldweatheronline.com/)

Weather prediction involves prediction of temperature, pressure, humidity and weather condition. The first three predicates are **continuous** values which need a **regression model** for prediction. The weather condition can be either of Rain, Snow or Sunny, predicting which is a **classification problem**. For predicting weather condition, **Decision Tree Classifier** was used. For predicting humidity, pressure and temperature, two models were used - **Linear Regression** and **Decision Tree Regression** and models were created for the same. On comparing the results, **Decision Tree Regression** provided more accurate results. 

Here I have used [spark.mllib](https://spark.apache.org/docs/1.6.0/mllib-guide.html) library to implement regression and classification. MLlib is Spark's machine learning(ML) library which makes practical machine learning scalable and easy.


## Prerequisites
[Java 1.7](https://java.com/en/download/) and [Apache Spark 1.6.0](https://spark.apache.org/releases/spark-release-1-6-0.html) must be installed in the system.

## How to run the application ?
Step 1: Build the ML models; a classifier for weather condition (SUNNY/RAIN/SNOW) and regression models(both Linear and Decision Tree) each for temperature, humidity and pressure. Change Property file **model.properties** accordingly to tune the algorithm.

## Build the maven project
```
mvn clean install  
```

## Run the project

### To build and evaluate Models:
```
spark-submit --class com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder <jarlocation>
spark-submit --class com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder <jarlocation>
spark-submit --class com.weatherpred.modelbuilder.LinearRegressionBuilder <jarlocation>

eg: spark-submit --class com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder WeatherPredictionOnSpark-0.0.1-SNAPSHOT.jar
    spark-submit --class com.weatherpred.modelbuilder.DecisionTreeRegressionBuilder WeatherPredictionOnSpark-0.0.1-SNAPSHOT.jar
    spark-submit --class com.weatherpred.modelbuilder.LinearRegressionBuilder WeatherPredictionOnSpark-0.0.1-SNAPSHOT.jar
```

### To Predict Weather

```
spark-submit --class com.weatherpred.application.WeatherPredictor <jarlocation> --lat <latitude> --long  <longitude> --ele  <elevation> --time <unixTimeStamp> --out <outputLocation>

eg: spark-submit --class com.weatherpred.application.WeatherPredictor WeatherPredictor.jar --lat 24.8614622 --long 67.0099388 --ele 9.870092392 --time 1423123200 --out /home/user/output.txt
```

## Expected Output:

Output of Decision Tree Regression:
NA|24.8614622,67.0099388,9.870092392|2015-02-05T08:00:00Z|SUNNY|70.98783018867924|1004.0401934972245|0.47326432022084186

Output of Linear Regression:
NA|24.8614622,67.0099388,9.870092392|2015-02-05T08:00:00Z|SUNNY|-3.6620187284539376E19|-7.472898826048609E47|-4.167236938571616E17

## Command line arguments 

```
--help          Displays help  
--lat            **Latitiude of the location 
--long           **Longitude of the location
--ele            **Elevation of the location 
--time           **Unix TimeStamp
--out		     **Output Location


**  -> Mandatory arguments  
