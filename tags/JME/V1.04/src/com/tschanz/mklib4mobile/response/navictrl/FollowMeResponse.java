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

public class FollowMeResponse extends MKResponse
{
    /// Fields ///

    private WayPoint waypoint = new WayPoint();
    
    
    /// Public Methods ///
    
	public WayPoint getWaypoint()
	{
		return this.waypoint;
	}


    public void readFromInputStream(MKInputStream inStream) throws IOException
    {
        this.waypoint.readFromInputStream(inStream);
    }
}