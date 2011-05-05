/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.bluetoothapi;

/**
 * Interface that must be implemented by the listeners of the BTHelper class.
 */
public interface BluetoothListener
{
	/**
	 * Method called by the BTHelper class when a device search has been completed.
	 * @param deviceList - list of all detected bluetooth devices during the search
	 */
	void deviceSearchCompleted(BluetoothDevice[] deviceList);

}
