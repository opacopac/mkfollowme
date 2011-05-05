/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.bluetoothapi;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.tschanz.utils.Logger;


/**
 * A helper class for handling bluetooth connections.
 */
class BluetoothImplementationJsr82 extends BluetoothProvider implements DiscoveryListener
{
	/// Fields
	private Vector listeners;
	private Vector deviceList;
	
	
	/// Constructors

	/**
	 * Class constructor. Private to prevent more than one instance (singleton).
	 */
	BluetoothImplementationJsr82()
	{
		this.listeners = new Vector();
		this.deviceList = new Vector();
	}
	
	
	/// Public Methods ///

	/**
	 * Subscribe a new listener to receive events raised by this class.
	 * @param listener - a listener (implementing the BluetoothListener interface) which will receive events raised by this class 
	 * @see BluetoothListener
	 */
	public void addListener(BluetoothListener listener)
	{
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}
	
	
	/**
	 * Unsubscribe an existing event listener of this class.
	 * @param listener - the listener which will stop receiving events by this class
	 * @see BluetoothListener 
	 */
	public void removeListener(BluetoothListener listener)
	{
		if (this.listeners.contains(listener))
			this.listeners.removeElement(listener);
	}
	
	
	/**
	 * Start a search for bluetooth devices (asynchronously). Method inquiryCompleted is called when the search has completed.
	 * @see inquiryCompleted 
	 */
	public void startSearch()
	{
		this.deviceList = new Vector();
		
		try
		{
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();
			agent.startInquiry(DiscoveryAgent.GIAC, this);
		}
		catch (BluetoothStateException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Called when a new bluetooth device has been discovered during a search.
	 * @see DiscoveryListener
	 */
	public void deviceDiscovered(RemoteDevice device, DeviceClass devClass)
	{
		String name;
		String adr = device.getBluetoothAddress();
		
		try
		{
			name = device.getFriendlyName(false);
		}
		catch (IOException e)
		{
			Logger.Log(e);
			name = "[" + adr + "]";
		}

		BluetoothDevice dev = new BluetoothDevice();
		dev.setBluetoothAddress(adr);
		dev.setFriendlyName(name);
		
		this.deviceList.addElement(dev);
	}

	
	/**
	 * Called when the search for bluetooth devices has completed
	 * @see DiscoveryListener 
	 */
	public void inquiryCompleted(int discType)
	{
		BluetoothDevice[] devices = null;

		if (this.deviceList.size() > 0)
		{
			devices = new BluetoothDevice[this.deviceList.size()];
			this.deviceList.copyInto(devices);
		}
		
		this.notifyDeviceSearchCompleted(devices);
	}

	
	/**
	 * Not used.
	 * @see DiscoveryListener
	 */
	public void serviceSearchCompleted(int transID, int respCode)
	{
	}

	
	/**
	 * Not used.
	 * @see DiscoveryListener
	 */
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord)
	{
	}
	
	
	/**
	 * Opens a serial connection over bluetooth ("btspp://...") to the specified device. 
	 * @param device - the device to connect to
	 * @return a StreamConnection object of the current connetion, allowing access to in- and output stream. 
	 * @throws IOException
	 */
	public StreamConnection openConnection(BluetoothDevice device) throws IOException
	{
		StreamConnection connection = null;
		String url = "btspp://" + device.getBluetoothAddress() + ":1";
		
		connection = (StreamConnection) Connector.open(url, Connector.READ_WRITE, true);
		
		return connection;
	}


    /// Private Methods ///

	/**
	 * Notifies all the subscribed listeners that the device search has been completed
	 * @see BluetoothListener
	 */
    private void notifyDeviceSearchCompleted(BluetoothDevice[] deviceList)
    {
    	for (int i = 0; i < this.listeners.size(); i++)
    		((BluetoothListener)this.listeners.elementAt(i)).deviceSearchCompleted(deviceList);
    }
}


