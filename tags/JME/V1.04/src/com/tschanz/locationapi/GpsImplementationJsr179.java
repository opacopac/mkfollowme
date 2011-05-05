/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.locationapi;

import java.util.Vector;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import com.tschanz.utils.Logger;
import com.tschanz.utils.Utils;


/**
 * An JSR-179 implementation for the GpsProvider class for handling all location service interactions.
 */
class GpsImplementationJsr179 extends GpsProvider implements Runnable, LocationListener
{
	/// Fields ///
	
	private Vector listeners;
	private LocationProvider locationProvider;
	private boolean cmdStartLocationUpdates;
	private boolean cmdStopLocationUpdates;
	private boolean isUpdatingLocation;
	
	
	/// Constructors ///
	
	GpsImplementationJsr179()
	{
		this.listeners = new Vector();

		new Thread(this).start();
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
		this.cmdStartLocationUpdates = true;
	}
	
	
	public void stopLocationUpdates()
	{
		this.isUpdatingLocation = false;
		this.cmdStopLocationUpdates = true;
	}
	

	public void run()
	{
		while (true)
		{
			if (this.cmdStartLocationUpdates)
			{
				if (this.getLocationProvider() == null)
					return;
				
				this.getLocationProvider().setLocationListener(this, -1, -1, -1);
				this.cmdStartLocationUpdates = false;
			}
			
			if (this.cmdStopLocationUpdates)
			{
				if (this.getLocationProvider() == null)
					return;
				
				this.getLocationProvider().setLocationListener(null, -1, -1, -1);
				this.cmdStopLocationUpdates = false;
			}
			
			Utils.sleep(100);
		}
	}
	
	
	public boolean isUpdatingLocation()
	{
		return this.isUpdatingLocation;
	}

	
	public void locationUpdated(LocationProvider lp, Location loc)
	{
		if (lp.getState() == LocationProvider.AVAILABLE)
		{
			Coordinates c = loc.getQualifiedCoordinates();

			GpsCoordinates gc = new GpsCoordinates();
			gc.setLatitude(c.getLatitude());
			gc.setLongitude(c.getLongitude());
			gc.setAltitude(c.getAltitude());
			
			this.notifyLocationUpdated(gc);
		}
	}


	public void providerStateChanged(LocationProvider lp, int newState)
	{
	}
    
    
    /// Private Methods ///
    
	private LocationProvider getLocationProvider()
	{
	    if (this.locationProvider == null)
	    {
			try
			{
			    Criteria cr = new Criteria();
			    cr.setHorizontalAccuracy(500);

				this.locationProvider = LocationProvider.getInstance(cr);
			}
			catch (LocationException e)
			{
				Logger.Log(e);
				return null;
			}
	    }
	    
	    return this.locationProvider;
	}


    private void notifyLocationUpdated(GpsCoordinates c)
    {
    	for (int i = 0; i < this.listeners.size(); i++)
    		((GpsListener)this.listeners.elementAt(i)).locationUpdated(c);
    }
}
