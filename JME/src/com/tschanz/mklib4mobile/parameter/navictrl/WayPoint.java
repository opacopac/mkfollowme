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

public class WayPoint extends MKParameter
{
	/// Fields ///
	
    /**
     * GPS position of waypoint.
     */
    public GpsPos Position = new GpsPos();

    /**
     * Orientation (for future implementation).
     */
    public short Heading;

    /**
     * Tolerance radius in meters. If the MK is within that range around the target, then the next target is triggered.
     * (u8)
     */
    public int ToleranceRadius;

    /**
     * Hold time in seconds. If the was once in the tolerance area around a WP, this time defines the delay before the next WP is triggered.
     * (u8)
     */
    public int HoldTime;

    /**
     * For future implementation.
     * (u8)
     */
    public int Event_Flag;

    /**
     * Reserve (12 bytes).
     * (u8)
     */
    public int[] Reserve = new int[12];

    
    /// Public Methods ///

	public void readFromInputStream(MKInputStream inStream) throws IOException
	{
        this.Position.readFromInputStream(inStream);
        this.Heading = inStream.readShort();
        this.ToleranceRadius = inStream.readUnsignedByte();
        this.HoldTime = inStream.readUnsignedByte();
        this.Event_Flag = inStream.readUnsignedByte();
        this.Reserve = new int[12];
        for (int i = 0; i < 12; i++)
            this.Reserve[i] = inStream.readUnsignedByte();
        }


	public void writeToOutputStream(MKOutputStream outStream) throws IOException
	{
		this.Position.writeToOutputStream(outStream);
		outStream.writeShort(this.Heading);
		outStream.writeByte(this.ToleranceRadius);
		outStream.writeByte(this.HoldTime);
		outStream.writeByte(this.Event_Flag);
        for (int i = 0; i < 12; i++)
        	outStream.writeByte(this.Reserve[i]);
	}
}
