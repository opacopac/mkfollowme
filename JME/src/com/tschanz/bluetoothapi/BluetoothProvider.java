/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.bluetoothapi;

import java.io.IOException;

import javax.microedition.io.StreamConnection;

import com.tschanz.utils.Logger;

/**
 * Class for handling all bluetooth API interactions.
 * (JSR-82 supported so far)
 */
public abstract class BluetoothProvider
{
	/**
	 * Creates an instance of this class.
	 * @return a new instance of this class if a supported API is present or 'null' else.
	 */
	public static BluetoothProvider getInstance()
	{
		BluetoothProvider provider = null;
		
        try
        {
            Class.forName("javax.bluetooth.RemoteDevice"); // this will throw an exception if JSR-82 is missing
            Class c = Class.forName("com.tschanz.bluetoothapi.BluetoothImplementationJsr82");
            provider = (BluetoothProvider)(c.newInstance());
        }
        catch (ClassNotFoundException e)
        {
        	Logger.Log(e);
        }
        catch (InstantiationException e)
        {
        	Logger.Log(e);
        }
        catch (IllegalAccessException e)
        {
        	Logger.Log(e);
		}

        return provider;
	}
	
	
	/**
	 * Subscribes a new listener to receive events raised by this class.
	 * @param listener - a listener (implementing the BluetoothListener interface) which will receive events raised by this class 
	 * @see BluetoothListener
	 */
	public abstract void addListener(BluetoothListener listener);
	
	
	/**
	 * Unsubscribes an existing event listener of this class.
	 * @param listener - the listener which will stop receiving events by this class
	 * @see BluetoothListener 
	 */
	public abstract void removeListener(BluetoothListener listener);	
	
	
	/**
	 * Start a search for bluetooth devices (asynchronously). Method inquiryCompleted is called when the search has completed.
	 * @see inquiryCompleted 
	 */
	public abstract void startSearch();	
	
	
	/**
	 * Opens a serial connection over bluetooth ("btspp://...") to the specified device. 
	 * @param device - the device to connect to
	 * @return a StreamConnection object of the current connetion, allowing access to in- and output stream. 
	 * @throws IOException
	 */
	public abstract StreamConnection openConnection(BluetoothDevice device) throws IOException;
}


