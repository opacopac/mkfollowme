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

public class GpsPos extends MKParameter
{
    /**
     * Longitude in 1E-7 deg.
     */
    public int longitude;

    /**
     * Latitude in 1E-7 deg.
     */
    public int latitude;

    /**
     * Altitude in mm.
     */
    public int altitude;

    /**
     * Validity of data.
     * (u8)
     */
    public int status;


    /// Public Methods ///

	public void writeToOutputStream(MKOutputStream outStream) throws IOException
	{
		outStream.writeInt(this.longitude);
		outStream.writeInt(this.latitude);
		outStream.writeInt(this.altitude);
		outStream.writeByte(this.status);
	}


	public void readFromInputStream(MKInputStream inStream) throws IOException
	{
		this.longitude = inStream.readInt();
		this.latitude = inStream.readInt();
		this.altitude = inStream.readInt();
		this.status = inStream.readUnsignedByte();
	}
}
