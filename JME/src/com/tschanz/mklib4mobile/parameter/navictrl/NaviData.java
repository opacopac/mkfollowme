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

public class NaviData extends MKParameter
{
	/**
	 * version of this structure for compatibility.
	 * (u8)
	 */
    public int version;

    public GpsPos CurrentPosition = new GpsPos();                      // see ubx.h for details
    public GpsPos TargetPosition = new GpsPos();
    public GpsPosDev TargetPositionDeviation = new GpsPosDev();
    public GpsPos HomePosition = new GpsPos();
    public GpsPosDev HomePositionDeviation = new GpsPosDev();

    /**
     * index of current waypoints running from 0 to WaypointNumber-1
     * (u8)
     */
    public int WaypointIndex;

    /**
     * number of stored waypoints
     * (u8)
     */
    public int WaypointNumber;

    /**
     * no of satellites used for position solution
     * (u8)
     */
    public int SatsInUse;

    /**
     * hight according to air pressure
     */
    public short Altimeter;

    /**
     * climb(+) and sink(-) rate
     */
    public short Variometer;

    /**
     * flying time in seconds
     * (u16)
     */
    public int FlyingTime;

    /**
     * Battery Voltage in 0.1 Volts
     * (u8)
     */
    public int UBat;

    /**
     * speed over ground in cm/s (2D)
     * (u16)
     */
    public int GroundSpeed;

    /**
     * current flight direction in deg as angle to north
     */
    public short Heading;

    /**
     * current compass value
     */
    public short CompassHeading;

    /**
     * current Nick angle in 1°
     */
    public byte AngleNick;

    /**
     * current Rick angle in 1°
     */
    public byte AngleRoll;

    /**
     * RC_Quality
     *(u8)
     */
    public int RC_Quality;

    /**
     * Flags from FC
     * (u8)
     */
    public int MKFlags;

    /**
     * Flags from NC
     * (u8)
     */
    public int NCFlags;

	/**
	 * 0 --> okay
	 * (u8)
	 */
    public int Errorcode;

    /**
     * current operation radius around the Home Position in m
     * (u8)
     */
    public int OperatingRadius;

    /**
     * gps verical speed in cm/s
     */
    public short TopSpeed;

    /**
     * time to stay at the current waypoint in s
     * (u8)
     */
    public int TargetHoldTime;

    /**
     * for future use (4 bytes)
     * (u8)
     */
    public int[] Reserve = new int[4];

    
    /// Public Methods ///

	public void readFromInputStream(MKInputStream inStream) throws IOException
	{
        this.version = inStream.readUnsignedByte();
        this.CurrentPosition.readFromInputStream(inStream);
        this.TargetPosition.readFromInputStream(inStream);
        this.TargetPositionDeviation.readFromInputStream(inStream);
        this.HomePosition.readFromInputStream(inStream);
        this.HomePositionDeviation.readFromInputStream(inStream);
        this.WaypointIndex = inStream.readUnsignedByte();
        this.WaypointNumber = inStream.readUnsignedByte();
        this.SatsInUse = inStream.readUnsignedByte();
        this.Altimeter = inStream.readShort();
        this.Variometer = inStream.readShort();
        this.FlyingTime = inStream.readUnsignedShort();
        this.UBat = inStream.readUnsignedByte();
        this.GroundSpeed = inStream.readUnsignedShort();
        this.Heading = inStream.readShort();
        this.CompassHeading = inStream.readShort();
        this.AngleNick = inStream.readByte();
        this.AngleRoll = inStream.readByte();
        this.RC_Quality = inStream.readUnsignedByte();
        this.MKFlags = inStream.readUnsignedByte();
        this.NCFlags = inStream.readUnsignedByte();
        this.Errorcode = inStream.readUnsignedByte();
        this.OperatingRadius = inStream.readUnsignedByte();
        this.TopSpeed = inStream.readShort();
        this.TargetHoldTime = inStream.readUnsignedByte();
        this.Reserve = new int[4];
        for (int i = 0; i < 4; i++)
        	this.Reserve[i] = inStream.readUnsignedByte();
        
	}


	public void writeToOutputStream(MKOutputStream outStream) throws IOException
	{
        outStream.writeByte(this.version);
        this.CurrentPosition.writeToOutputStream(outStream);
        this.TargetPosition.writeToOutputStream(outStream);
        this.TargetPositionDeviation.writeToOutputStream(outStream);
        this.HomePosition.writeToOutputStream(outStream);
        this.HomePositionDeviation.writeToOutputStream(outStream);
        outStream.writeByte(this.WaypointIndex);
        outStream.writeByte(this.WaypointNumber);
        outStream.writeByte(this.SatsInUse);
        outStream.writeShort(this.Altimeter);
        outStream.writeShort(this.Variometer);
        outStream.writeShort(this.FlyingTime);
        outStream.writeByte(this.UBat);
        outStream.writeShort(this.GroundSpeed);
        outStream.writeShort(this.Heading);
        outStream.writeShort(this.CompassHeading);
        outStream.writeByte(this.AngleNick);
        outStream.writeByte(this.AngleRoll);
        outStream.writeByte(this.RC_Quality);
        outStream.writeByte(this.MKFlags);
        outStream.writeByte(this.NCFlags);
        outStream.writeByte(this.Errorcode);
        outStream.writeByte(this.OperatingRadius);
        outStream.writeShort(this.TopSpeed);
        outStream.writeByte(this.TargetHoldTime);
        this.Reserve = new int[4];
        for (int i = 0; i < 4; i++)
        	outStream.writeByte(this.Reserve[i]);
	}
}
