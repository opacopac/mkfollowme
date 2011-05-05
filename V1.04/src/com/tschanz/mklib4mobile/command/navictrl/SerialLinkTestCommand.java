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

public class SerialLinkTestCommand extends MKCommand
{
	/// Fields ///
	
	private final static char COMMAND_ID = 'z';
	private int echoPattern; // u16
	
	
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
	 * u16
	 * @param echoPattern
	 */
	public void setEchoPattern(int echoPattern)
	{
		this.echoPattern = echoPattern & 0xFFFF;
	}


	public int getEchoPattern()
	{
		return echoPattern;
	}

	
	public void writeToOutputStream(MKOutputStream outStream) throws IOException
    {
		outStream.writeShort(this.echoPattern);
    }
}
