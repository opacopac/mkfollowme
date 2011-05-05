/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.locationapi;

public class GpsCoordinates
{
	/// Fields ///
	
	private float altitude;
	private double latitude;
	private double longitude;
	
	
	/// Public Methods ///
	
	public void setAltitude(float altitude)
	{
		this.altitude = altitude;
	}


	public float getAltitude()
	{
		return this.altitude;
	}


	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	
	public double getLatitude()
	{
		return this.latitude;
	}
	
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	
	public double getLongitude()
	{
		return this.longitude;
	}
}
