/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.tschanz.mklib4mobile.MKCommunicator;
import com.tschanz.mklib4mobile.MKOutputStream;
import com.tschanz.utils.Logger;

public abstract class MKCommand
{
    /// Fields ///

    private int slaveAddress;


    /// Public Methods ///

    public int getSlaveAddress()
    {
    	return this.slaveAddress;
    }

    
    public abstract char getCommandID();


    public void send(MKCommunicator communicator)
    {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		MKOutputStream dos = new MKOutputStream(bos);
		
		try
		{
			// get parameters
			this.writeToOutputStream(dos);
			dos.flush();
			
			// build command
	        byte[] commandBytes = CommandStringBuilder.build(this.getSlaveAddress(), this.getCommandID(), bos.toByteArray());
	        communicator.SendBytes(commandBytes);
	        
			dos.close();
			bos.close();
		}
		catch (IOException e)
		{
			Logger.Log(e);
		}
    }


	public abstract void writeToOutputStream(MKOutputStream outStream) throws IOException;
}
