/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.bluetoothapi;

public class BluetoothDevice
{
	/// Fields ///
	private String bluetoothAddress;
	private String friendlyName;

	
	/// Public Methods ///
	
	public void setBluetoothAddress(String bluetoothAddress)
	{
		this.bluetoothAddress = bluetoothAddress;
	}
	

	public String getBluetoothAddress()
	{
		return this.bluetoothAddress;
	}


	public void setFriendlyName(String friendlyName)
	{
		this.friendlyName = friendlyName;
	}

	
	public String getFriendlyName()
	{
		return this.friendlyName;
	}
}
