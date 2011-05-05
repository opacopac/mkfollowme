/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.StreamConnection;

import com.tschanz.mklib4mobile.response.MKResponse;
import com.tschanz.mklib4mobile.response.ResponseParser;
import com.tschanz.utils.Logger;


public class MKCommunicator implements Runnable
{
    /// Fields ///

    private final char endOfLine = '\r';
    private StreamConnection connection;
    private InputStream inStream;
    private OutputStream outStream;
	private Vector listeners;


    /// Constructors ///
    
    public MKCommunicator(StreamConnection connection)
    {
    	this.listeners = new Vector();
    	this.connection = connection;

    	try
		{
			this.inStream = this.connection.openInputStream();
			this.outStream = this.connection.openOutputStream();
			
	    	new Thread(this).start(); // start reading from connection
		}
		catch (IOException e)
		{
			Logger.Log(e);
		}
    }
    
    
    /// Public Methods ///

	public void addListener(MKResponseListener listener)
	{
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}
	
	
	public void removeListener(MKResponseListener listener)
	{
		if (this.listeners.contains(listener))
			this.listeners.removeElement(listener);
	}
	
    
	public void run()
	{
		ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();

    	while (true) // TODO: as long as connection is open
		{
			try
			{
				
				
				if (this.inStream.available() > 0)
				{
					int nextByte = this.inStream.read();
					
					if (nextByte == endOfLine)
					{
						lineBuffer.flush();
						
						MKResponse response = ResponseParser.createResponse(lineBuffer.toByteArray());
						if (response != null)
							this.notifyResponseReceived(response);

						lineBuffer.reset();
					}
					else
						lineBuffer.write(nextByte);
				}
				else
				{
					try
					{
						Thread.sleep(50);
					}
					catch (InterruptedException e)
					{
						Logger.Log(e);
					}
				}
			}
			catch (IOException e)
			{
				Logger.Log(e);
			}
		}
	}

	
    public void SendBytes(byte[] bytes)
    {
        if (true) //TODO: if port is open
        {
        	try
        	{
				this.outStream.write(bytes);
        		this.outStream.flush();
			}
        	catch (IOException e)
        	{
				Logger.Log(e);
			}
        }
    }
	
	
	/// Private Methods ///
	
	private void notifyResponseReceived(MKResponse response)
	{
    	for (int i = 0; i < this.listeners.size(); i++)
    		((MKResponseListener)this.listeners.elementAt(i)).responseReceived(response);
	}
}
