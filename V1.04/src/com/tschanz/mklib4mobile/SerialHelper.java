/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.tschanz.utils.Logger;


public class SerialHelper implements Runnable
{
	/// Fields ///
	private static final String BAUDRATE = "57600"; 
	private String cmdOpenComPort;
	private boolean cmdDestroy;
	private Vector listeners;
	private boolean isRunning;
	
	
	/// Constructors ///
	
	public SerialHelper()
	{
		this.listeners = new Vector();
		new Thread(this).start();
	}
	
	
	/// Public Methods ///
	
	public void addListener(SerialListener listener)
	{
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}
	
	
	public void removeListener(SerialListener listener)
	{
		if (this.listeners.contains(listener))
			this.listeners.removeElement(listener);
	}
	
	
	public static String[] getPortNames()
	{
		String ports = System.getProperty("microedition.commports");
		
		if (ports != null)
			return MKLibHelper.splitString(ports, ",");
		else
			return null;
	}
	
	
	public void openSerialPort(String portName)
	{
		this.cmdOpenComPort = portName; // set flag to open port (see run()-thread) 
	}
	
	
	public void destroy()
	{
		this.cmdDestroy = true;
	}
	
	
	public void run()
	{
		if (this.isRunning == true)
			return; // run only once  
		else
			this.isRunning = true;
		
		while (!this.cmdDestroy)
		{
			if (this.cmdOpenComPort != null)
			{
				StreamConnection connection = this.openSerialConnection(this.cmdOpenComPort);
				if (connection != null)
					this.notifyPortOpened(connection);
				
				this.cmdOpenComPort = null;
			}
			
			MKLibHelper.sleep(100);
		}
	}
	
    
    /// Private Methods ///

	private StreamConnection openSerialConnection(String portName)
	{  
		String parameter = "comm:" + portName + ";baudrate=" + BAUDRATE + ";autocts=off";
		
		try
		{
			Connection con = Connector.open(parameter);
			return (StreamConnection) con;
		}
		catch (IOException e)
		{
			Logger.Log(e);
		}
		
		return null;
	}


    private void notifyPortOpened(StreamConnection connection)
    {
    	for (int i = 0; i < this.listeners.size(); i++)
    		((SerialListener)this.listeners.elementAt(i)).serialPortOpened(connection);
    }
}


