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


public class ReadWayPointCommand extends MKCommand
{
	/// Fields ///
	
	private final static char COMMAND_ID = 'x';
	private int wayPointIndex; // u8
	
	
	/// Public Methods ///
	
	public char getCommandID()
	{
		return COMMAND_ID;
	}


	public int getSlaveAddress()
	{
		return SlaveAddress.NaviCtrl;
	}


	/**
	 * u8
	 * @param wayPointIndex
	 */
	public void setWayPointIndex(int wayPointIndex)
	{
		this.wayPointIndex = wayPointIndex & 0xFF;
	}


	public int getWayPointIndex()
	{
		return wayPointIndex;
	}

	
	public void writeToOutputStream(MKOutputStream outStream) throws IOException
    {
		outStream.writeByte(this.wayPointIndex);
    }
}
