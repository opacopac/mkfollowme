/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.command.navictrl;

import java.io.IOException;

import com.tschanz.mklib4mobile.MKOutputStream;
import com.tschanz.mklib4mobile.command.MKCommand;
import com.tschanz.mklib4mobile.parameter.common.SlaveAddress;
import com.tschanz.mklib4mobile.parameter.navictrl.WayPoint;

public class SetTargetPositionCommand extends MKCommand
{
    /// Fields ///

    private final static char COMMAND_ID = 's';

    private WayPoint wayPoint = new WayPoint();

    
    /// Public Methods ///

	public int getSlaveAddress()
	{
		return SlaveAddress.NaviCtrl;
	}
    
    
    public char getCommandID()
    {
        return COMMAND_ID;
    }


	public void setWayPoint(WayPoint wayPoint)
	{
		this.wayPoint = wayPoint;
	}


	public WayPoint getWayPoint()
	{
		return this.wayPoint;
	}

	
	public void writeToOutputStream(MKOutputStream outStream) throws IOException
	{
		this.wayPoint.writeToOutputStream(outStream);
	}
}
