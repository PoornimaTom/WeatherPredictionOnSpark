package com.weatherpred.dto;

/**
 * Class for Input Feature Set
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class InputFeaturesDTO {
	/**
	 * Latitude
	 */
	private double latitude;

	/**
	 * Longitude
	 */
	private double longitude;

	/**
	 * Elevation
	 */
	private double elevation;

	/**
	 * Unix Time
	 */
	private String unixTime;

	/**
	 * For output Location
	 */
	private String outputLocation;

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

	public String getUnixTime() {
		return unixTime;
	}

	public void setUnixTime(String unixTime) {
		this.unixTime = unixTime;
	}

	public String getOutLocation() {
		return outputLocation;
	}

	public void setOutLocation(String outLocation) {
		this.outputLocation = outLocation;
	}

}
