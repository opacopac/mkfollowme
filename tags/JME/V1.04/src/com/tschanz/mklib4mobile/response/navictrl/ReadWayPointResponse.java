/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.response.navictrl;

import java.io.IOException;

import com.tschanz.mklib4mobile.MKInputStream;
import com.tschanz.mklib4mobile.parameter.navictrl.WayPoint;
import com.tschanz.mklib4mobile.response.MKResponse;

public class ReadWayPointResponse extends MKResponse
{
    /// Fields ///

	private int wayPointCount; // u8
	private int wayPointIndex; // u8
    private WayPoint waypoint = new WayPoint();
    
    
    /// Public Methods ///
    
	public int getWayPointCount()
	{
		return this.wayPointCount;
	}


	public int getWayPointIndex()
	{
		return this.wayPointIndex;
	}


	public WayPoint getWaypoint()
	{
		return this.waypoint;
	}


    public void readFromInputStream(MKInputStream inStream) throws IOException
    {
    	this.wayPointCount = inStream.readUnsignedByte();
    	this.wayPointIndex = inStream.readUnsignedByte();
        this.waypoint.readFromInputStream(inStream);
    }
}