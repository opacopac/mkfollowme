/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.command.navictrl;

import java.io.IOException;

import com.tschanz.mklib4mobile.MKCommunicator;
import com.tschanz.mklib4mobile.MKOutputStream;
import com.tschanz.mklib4mobile.command.MKCommand;
import com.tschanz.mklib4mobile.parameter.common.SlaveAddress;
import com.tschanz.mklib4mobile.parameter.navictrl.UartSelector;

public class RedirectUartCommand extends MKCommand
{
    /// Fields ///

    private final static char COMMAND_ID = 'u';
    private static byte[] resetToNaviSequence = new byte[] { 0x1B, 0x1B, 0x55, (byte)0xAA, 0x00 }; //TODO: korrekt?

    private int uart;

    
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
	 * (u8)
	 * @param uart
	 */
	public void setUart(int uart)
	{
		this.uart = uart & 0xFF;
	}


	public int getUart()
	{
		return this.uart;
	}

    
    public void send(MKCommunicator communicator)
    {
        if (this.uart == UartSelector.NaviCtrl)
            communicator.SendBytes(resetToNaviSequence);
        else
            super.send(communicator);
    }


	public void writeToOutputStream(MKOutputStream outStream) throws IOException
	{
		outStream.writeByte(this.uart);
	}
}
