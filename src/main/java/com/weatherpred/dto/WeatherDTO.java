package com.weatherpred.dto;

import com.weatherpred.enums.WeatherStatus;

/**
 * Output class containing Weather info
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class WeatherDTO {

	/**
	 * Represents the location where the weather is predicted
	 */
	private String location;
	/**
	 * Represents the time when the weather record is predicted
	 */
	private String time;

	/**
	 * Represents weather status
	 */
	private WeatherStatus weatherStatus;

	/**
	 * Temperature
	 */
	private double temperature;
	/**
	 * Humidity
	 */
	private double humidity;
	/**
	 * Pressure
	 */
	private double pressure;

	/**
	 * Latitude of location.
	 */
	private double latitude;
	/**
	 * Longitude of location.
	 */
	private double longitude;
	/**
	 * Elevation of location from sea level.
	 */
	private double elevation;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public WeatherStatus getWeatherStatus() {
		return weatherStatus;
	}

	public void setWeatherStatus(WeatherStatus weatherStatus) {
		this.weatherStatus = weatherStatus;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	/**
	 * Parameterized constructor
	 * 
	 * @param location
	 * @param latitude
	 * @param longitude
	 * @param elevation
	 * @param weatherStatus
	 * @param temperature
	 * @param humidity
	 * @param pressure
	 */
	public WeatherDTO(String location, double latitude, double longitude,
			double elevation, WeatherStatus weatherStatus,String time, double temperature,
			double humidity, double pressure) {
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		this.weatherStatus = weatherStatus;
		this.time = time;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
	}

}
