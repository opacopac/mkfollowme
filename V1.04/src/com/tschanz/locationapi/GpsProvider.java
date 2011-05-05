/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.locationapi;

import com.tschanz.utils.Logger;

/**
 * Class for handling all location service API interactions.
 * (JSR-179 supported so far)
 */
public abstract class GpsProvider
{
	/**
	 * Creates an instance of this class.
	 * @return a new instance of this class if a supported API is present or 'null' else.
	 */
	public static GpsProvider getInstance()
	{
		GpsProvider provider = null;
		
		return new GpsImplementationTcpProxy();
		
//        try
//        {
//            Class.forName("javax.microedition.location.Location"); // this will throw an exception if JSR-179 is missing
//            Class c = Class.forName("com.tschanz.locationapi.GpsImplementationJsr179");
//            provider = (GpsProvider)(c.newInstance());
//        }
//        catch (ClassNotFoundException e)
//        {
//        	Logger.Log(e);
//        }
//        catch (InstantiationException e)
//        {
//        	Logger.Log(e);
//        }
//        catch (IllegalAccessException e)
//        {
//        	Logger.Log(e);
//		}
//
//        return provider;
	}
	
	
	/**
	 * Subscribes a new listener to receive events raised by this class.
	 * @param listener - a listener (implementing the GpsListener interface) which will receive events raised by this class 
	 * @see GpsListener
	 */
	public abstract void addListener(GpsListener listener);	
	
	
	/**
	 * Unsubscribes an existing event listener of this class.
	 * @param listener - the listener which will stop receiving events by this class
	 * @see GpsListener 
	 */
	public abstract void removeListener(GpsListener listener);
	
	
	/**
	 * Starts sending location updates to the subscribed listeners.
	 * @see GpsListener
	 */
	public abstract void startLocationUpdates();
	
	
	/**
	 * Stops sending location updates to the subscribed listeners.
	 * @see GpsListener
	 */
	public abstract void stopLocationUpdates();
	
	
	/**
	 * Checks whether location updates are sent or not.
	 * @return - 'true' if location updates are sent to the subscribed listeners, 'false' else.   
	 * @see GpsListener
	 */
	public abstract boolean isUpdatingLocation();
}
