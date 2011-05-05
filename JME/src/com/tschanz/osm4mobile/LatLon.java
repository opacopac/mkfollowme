/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

public class LatLon
{
   	public double longitude;
   	public double latitude;
   	
   	
   	public LatLon ()
   	{
   	}
   	
   	
   	public LatLon(double latitude, double longitude)
   	{
   		this.latitude = latitude;
   		this.longitude = longitude;
   	}

   	
   	public boolean isZero()
   	{
   		if (latitude == 0 && longitude == 0)
   			return true;
   		else
   			return false;
   	}
   	
   	
   	public LatLon clone()
   	{
   		return new LatLon(this.latitude, this.longitude);
   	}
}
