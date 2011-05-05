/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import henson.midp.Float11;

public class OsmPosition implements ByteSerializable
{
	/// Fields ///
	public final static int TILE_PIXEL_WIDTH = 256;
	public final static int TILE_PIXEL_HEIGHT = 256;
	public final static int MAX_ZOOMLEVEL = 18;
	private static final double EARTH_RADIUS_METERS = 6371009;
	private static final double EARTH_PERIMETER_METERS = 2 * Math.PI * EARTH_RADIUS_METERS;
    private int zoomLevel;
    private double tilePosX;
    private double tilePosY;
    
    
    /// Constructors ///
    
    public OsmPosition()
    {
    }
    

    public OsmPosition(int zoomLevel, double tilePosX, double tilePosY)
    {
    	this.setPosition(zoomLevel, tilePosX, tilePosY);
    }
    

    public OsmPosition(int zoomLevel, int tileNumX, int tileNumY, int pixelOffsetX, int pixelOffsetY)
    {
    	this.setPosition(zoomLevel, tileNumX, tileNumY, pixelOffsetX, pixelOffsetY);
    }
    

    public OsmPosition(int zoomLevel, LatLon pos)
    {
    	this.setPosition(zoomLevel, pos);
    }
    

    /// Public Methods ///
        
    public int getZoomLevel()
    {
    	return this.zoomLevel;
    }

   
    public double getTilePosX()
    {
    	return this.tilePosX;
    }

   
    public double getTilePosY()
    {
    	return this.tilePosY;
    }

   
    public int getTileNumX()
    {
    	return (int)Math.floor(this.tilePosX);
    }

   
    public int getTileNumY()
    {
    	return (int)Math.floor(this.tilePosY);
    }

   
    public int getPixelOffsetX()
    {
    	return (int)((this.tilePosX - Math.floor(this.tilePosX)) * TILE_PIXEL_WIDTH);
    }

   
    public int getPixelOffsetY()
    {
    	return (int)((this.tilePosY - Math.floor(this.tilePosY)) * TILE_PIXEL_HEIGHT);
    }
    
    
    public LatLon getLatLong()
    {
    	LatLon pos = new LatLon();
    	pos.longitude = this.getLongitude();
       	pos.latitude = this.getLatitude();
       	
       	return pos;
    }
    
    
    public void setPosition(int zoomLevel, double tilePosX, double tilePosY)
    {
    	this.zoomLevel = this.getValidZoomLevel(zoomLevel);
       	this.tilePosX = tilePosX;
       	this.tilePosY = tilePosY;
    }

    
    public void setPosition(int zoomLevel, int tileNumX, int tileNumY, int pixelOffsetX, int pixelOffsetY)
    {
    	this.zoomLevel = this.getValidZoomLevel(zoomLevel);
       	this.tilePosX = tileNumX + this.getOffsetX(pixelOffsetX);
       	this.tilePosY = tileNumY + this.getOffsetY(pixelOffsetY);
    }
    
    
    public void setPosition(int zoomLevel, LatLon pos)
    {
    	this.zoomLevel = this.getValidZoomLevel(zoomLevel);
    	this.tilePosX = (pos.longitude + 180) / 360 * (1 << this.zoomLevel);
    	this.tilePosY = (1 - Float11.log(Math.tan(pos.latitude * Math.PI / 180) + 1 / Math.cos(pos.latitude * Math.PI / 180)) / Math.PI) / 2 * (1 << this.zoomLevel);
    }

    
    public void incPositionX(double tileCount, int pixelCount)
    {
    	this.tilePosX = this.tilePosX + tileCount + this.getOffsetX(pixelCount);
    }
    
    
    public void incPositionY(double tileCount, int pixelCount)
    {
    	this.tilePosY = this.tilePosY + tileCount + this.getOffsetY(pixelCount);
    }
    
    
    public void incZoomLevel()
    {
    	if (this.zoomLevel >= MAX_ZOOMLEVEL)
    		return;
    	
    	this.zoomLevel++;
    	this.tilePosX = this.tilePosX * 2;
    	this.tilePosY = this.tilePosY * 2;
    }
    
    
    public void decZoomLevel()
    {
    	if (this.zoomLevel <= 0)
    		return;
    	
    	this.zoomLevel--;
    	this.tilePosX = this.tilePosX / 2;
    	this.tilePosY = this.tilePosY / 2;
    }
    
    
	public int getPixelDistX(int distMeter)
	{
		double tileDist = distMeter * Float11.pow(2, this.zoomLevel) / EARTH_PERIMETER_METERS / Math.cos(this.getLatitude() / 180 * Math.PI);
		int pixelDist = (int)(tileDist * OsmPosition.TILE_PIXEL_WIDTH);
		
		return pixelDist;
	}
	
	
	public double getMeterDistX(int pixelDist)
	{
		double tileDist = pixelDist / (double)OsmPosition.TILE_PIXEL_WIDTH;
		double distMeter = tileDist * EARTH_PERIMETER_METERS / Float11.pow(2, this.zoomLevel) * Math.cos(this.getLatitude() / 180 * Math.PI);
		
		return distMeter;
	}
    
    
    public OsmPosition clone()
    {
    	return new OsmPosition(this.zoomLevel, this.tilePosX, this.tilePosY);
    }
    
    
	public byte[] getAsByteArray() throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		dos.writeInt(this.zoomLevel);
		dos.writeDouble(this.tilePosX);
		dos.writeDouble(this.tilePosY);
		dos.flush();
		
		return bos.toByteArray();
	}


	public void loadFromByteArray(byte[] bytes) throws IOException
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bis);
		
		this.zoomLevel = this.getValidZoomLevel(dis.readInt());
		this.tilePosX = dis.readDouble();
		this.tilePosY = dis.readDouble();
	}
    
    
    /// Private Methods ///
    
    private double getOffsetX(int pixelOffset)
    {
    	return (double)pixelOffset / TILE_PIXEL_WIDTH;
    }
    
    
    private double getOffsetY(int pixelOffset)
    {
    	return (double)pixelOffset / TILE_PIXEL_HEIGHT;
    }
    
    
    private int getValidZoomLevel(int zoomLevel)
    {
    	if (zoomLevel < 0)
    		return 0;
    	
    	if (zoomLevel > MAX_ZOOMLEVEL)
    		return MAX_ZOOMLEVEL;
    	
    	return zoomLevel;
    }
    
    
    private double getLongitude()
    {
		return (this.tilePosX / Float11.pow(2.0, this.zoomLevel) * 360.0) - 180;
    }

   
    private double getLatitude()
    {
		double n = Math.PI - ((2.0 * Math.PI * this.tilePosY) / Float11.pow(2.0, this.zoomLevel));
		return 180.0 / Math.PI * Float11.atan(0.5 * (Float11.exp(n) - Float11.exp(-n)));
    }
 }
