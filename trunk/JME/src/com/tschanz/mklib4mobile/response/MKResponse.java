/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.response;

import java.io.IOException;

import com.tschanz.mklib4mobile.MKInputStream;
import com.tschanz.mklib4mobile.parameter.common.SlaveAddress;

public abstract class MKResponse
{
	/// Fields ///
	
	private int slaveAddress = SlaveAddress.FlightCtrl;

	
	/// Public Methods /// 
	
	public int getSlaveAddress()
	{
		return this.slaveAddress;
	}


	public void setSlaveAddress(int slaveAddress)
	{
		this.slaveAddress = slaveAddress;
	}

	
    public abstract void readFromInputStream(MKInputStream inStream) throws IOException;
}
