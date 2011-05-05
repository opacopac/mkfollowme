/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.locationapi;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import com.tschanz.utils.TcpProxyClient;
import com.tschanz.utils.TcpProxyClientListener;


class GpsImplementationTcpProxy extends GpsProvider implements TcpProxyClientListener
{
	/// Fields ///
	
	private Vector listeners;
	private TcpProxyClient proxy;
	private boolean isUpdatingLocation;
	
	
	/// Constructors ///
	
	GpsImplementationTcpProxy()
	{
		this.listeners = new Vector();
		this.proxy = new TcpProxyClient("socket://127.0.0.1:13000");
	}
	
	
	/// Public Methods ///
	
	public void addListener(GpsListener listener)
	{
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}
	
	
	public void removeListener(GpsListener listener)
	{
		if (this.listeners.contains(listener))
			this.listeners.removeElement(listener);
	}
	
	
	public void startLocationUpdates()
	{
		this.isUpdatingLocation = true;
		this.proxy.sendData("Ping", null);
	}
	
	
	public void stopLocationUpdates()
	{
		this.isUpdatingLocation = false;
	}
	
	
	public boolean isUpdatingLocation()
	{
		return this.isUpdatingLocation;
	}
	
	
	public void ProxyMessageReceived(String msgName, DataInputStream msgData) throws IOException
	{
		GpsCoordinates gc = new GpsCoordinates();
		gc.setLatitude(47);
		gc.setLongitude(7);
		this.notifyLocationUpdated(gc);

//		if (msgName == "GPS_LocationChanged")
//		{
//			GpsCoordinates gc = new GpsCoordinates();
//			// TODO
//			
//			this.notifyLocationUpdated(gc);
//		}
//		else if (msgName == "GPS_DeviceStateChanged")
//		{
//			// TODO
//			
//		}
	}


	
    /// Private Methods ///


    private void notifyLocationUpdated(GpsCoordinates c)
    {
    	for (int i = 0; i < this.listeners.size(); i++)
    		((GpsListener)this.listeners.elementAt(i)).locationUpdated(c);
    }
}
