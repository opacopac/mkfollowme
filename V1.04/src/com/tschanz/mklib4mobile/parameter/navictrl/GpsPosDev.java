/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.parameter.navictrl;

import java.io.IOException;

import com.tschanz.mklib4mobile.MKInputStream;
import com.tschanz.mklib4mobile.MKOutputStream;
import com.tschanz.mklib4mobile.parameter.MKParameter;

public class GpsPosDev extends MKParameter
{
    /**
     * distance to target in cm
     * (u16)
     */
    public int Distance;

    /**
     * course to target in deg
     */
    public short Bearing;


    /// Public Methods ///

    public void writeToOutputStream(MKOutputStream outStream) throws IOException
    {
    	outStream.writeShort(this.Distance);
    	outStream.writeShort(this.Bearing);
    }


	public void readFromInputStream(MKInputStream inStream) throws IOException
	{
		this.Distance = inStream.readUnsignedShort();
		this.Bearing = inStream.readShort();
	}
}
