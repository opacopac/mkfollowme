/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

import com.tschanz.utils.LocalyticsNetwork;
import com.tschanz.utils.Logger;

import henson.midp.Float11;

public final class OsmTileLoader implements Runnable
{
	/// Fields ///
	private static final String BASE_TILE_URL = "http://tile.openstreetmap.org/";
	private static final int MEMCACHE_MAX_TILE_COUNT = 20;
//	private static final int MaxTileStoreBytes = 200000;
//	private static final int MinTileStoreFreeBytes = 50000;

	private static final OsmTileLoader instance = new OsmTileLoader();
	
	private OsmTileLookupTable recordLookupTable;
	private Hashtable memCache;
	private Vector memCacheList;
	private Stack downloadQueue;
	private Vector listeners;
	private Bitmap emptyTile;
	
	
	/// Constructors ///
		
	private OsmTileLoader()
	{
		this.recordLookupTable = new OsmTileLookupTable();
		this.memCache = new Hashtable();
		this.memCacheList = new Vector();
		this.downloadQueue = new Stack();
		this.listeners = new Vector();
		this.emptyTile = this.getEmptyTile();
		
		new Thread(this).start();
	}
	
	
	/// Public Methods ///
	
	public static OsmTileLoader getInstance()
	{
		return instance;
	}

	
	public void addListener(OsmTileLoaderListener listener)
	{
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}
	
	
	public void removeListener(OsmTileLoaderListener listener)
	{
		if (this.listeners.contains(listener))
			this.listeners.removeElement(listener);
	}
	
	
    public Bitmap getMapImage(OsmPosition position)
    {
    	if (this.isOutOfMap(position))
    		return null;
    	
    	String url = this.getMapTileUrl(position.getZoomLevel(), position.getTileNumX(), position.getTileNumY());
    	OsmDemoMidlet.log("URL="+url);
    	// 1st try mem cache
    	if (this.memCache.containsKey(url))
    		return (Bitmap)this.memCache.get(url);
    	
    	// 2nd try local store
    	if (this.recordLookupTable.contains(url))
    	{
    		Bitmap img = this.loadFromStore(url);
    		if (img != null)
    			return img;
    		else
    			this.recordLookupTable.remove(url);
    	}
    	
    	// 3rd add to download queue
    	if (!this.downloadQueue.contains(url))
    		this.downloadQueue.addElement(url);
    	
    	return this.emptyTile;
    }
    
    
    public void SaveState()
    {
    	try
    	{
    		byte[] bytes = this.recordLookupTable.getAsByteArray();
			RecordStoreHelper.saveTileLookupTable(bytes);
		}
    	catch (IOException e)
    	{
    		Logger.Log(e);
		}
    }
    
    
    public void RestoreState()
    {
    	try
    	{
			byte[] bytes = RecordStoreHelper.loadTileLookupTable();
			if (bytes != null)
				this.recordLookupTable.loadFromByteArray(bytes);
		}
    	catch (IOException e)
    	{
    		Logger.Log(e);
		}
    }
    
    
    public void ClearCache()
    {
    	RecordStoreHelper.getInstance().deleteAllOsmTiles();
    	this.recordLookupTable.clear();
    	this.memCache.clear();
    }


	public void run()
	{
		while (true)
		{
			if (this.downloadQueue.isEmpty())
			{
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					Logger.Log(e);
				}
			}
			else
			{
				String url = (String)this.downloadQueue.pop();
				
		        try
		        {
		        	byte[] imgBytes = loadFromWeb(url);
		        	
		        	if (imgBytes != null)
		        	{
		        		//Image img = Image.createImage(imgBytes, 0, imgBytes.length);
		        		Bitmap bmp=Bitmap.createBitmapFromPNG(imgBytes,0,imgBytes.length);

		        		this.saveToStore(url, imgBytes);
		        		this.putToMemCache(url, bmp);
		        		this.notifyTileLoaded();
		        	}
		        }
		        catch (IOException ex)
		        {
					Logger.Log(ex);
		        }
			}
		}
    }
	
	
    /// Private Methods ///
    
    private String getMapTileUrl(int zoomLevel, int tileX, int tileY)
    {
    	return BASE_TILE_URL + zoomLevel + "/" + tileX + "/" + tileY + ".png";
    }
    
    
    private byte[] loadFromWeb(String url) throws IOException
    {
        HttpConnection httpConn = null;
        InputStream is = null;
        byte[] bytes = null;
        
	    try
	    {
			httpConn = (HttpConnection)Connector.open(url+LocalyticsNetwork.getConnectionString());
			httpConn.setRequestMethod(HttpConnection.GET);
			httpConn.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Configuration/CLDC-1.0");
	
			int respCode = httpConn.getResponseCode();
			if (respCode == HttpConnection.HTTP_OK)
			{
				is = httpConn.openDataInputStream();
				bytes = this.getByteArray(is);
			}
			else
				Logger.Log("Error in opening HTTP Connection. Error#" + respCode);
		}
	    finally
	    {
	    	if(is != null)
	    		is.close();
			if(httpConn != null)
				httpConn.close();
	    }
	    
	    return bytes;
    }
    
    
	private Bitmap loadFromStore(String url)
	{
		int recordId = this.recordLookupTable.getRecordId(url);
		if (recordId < 0)
			return null;
		
		byte[] imgBytes = RecordStoreHelper.getInstance().loadOsmTile(recordId);
		if (imgBytes == null)
		{
			this.recordLookupTable.remove(url);
			return null;
		}
		
		Bitmap bmp=Bitmap.createBitmapFromPNG(imgBytes,0,imgBytes.length);
		this.putToMemCache(url, bmp);
		
		return bmp;
	}
	
	
	private void saveToStore(String url, byte[] bytes)
	{
		int recordId = -1;
		
		if (this.recordLookupTable.contains(url))
			recordId = this.recordLookupTable.getRecordId(url);

//		while (RecordStoreHelper.getInstance().getTileStoreSize() > MaxTileStoreBytes 
//			|| RecordStoreHelper.getInstance().getTileStoreSizeAvailable() < MinTileStoreFreeBytes)
//		{
//			int delRecordId = this.recordLookupTable.getOldestRecordId();
//			if (delRecordId > 0)
//			{
//				this.recordLookupTable.remove(delRecordId);
//				RecordStoreHelper.getInstance().deleteOsmTile(delRecordId);
//			}
//			else
//				break;
//		}
		
		recordId = RecordStoreHelper.getInstance().saveOsmTile(recordId, bytes);
		
		this.recordLookupTable.put(url, recordId);
	}
	
	
	private void putToMemCache(String url, Bitmap img)
	{
		if (url == null || img == null)
			return;
		
		this.memCache.put(url, img);
		this.memCacheList.addElement(url);
		
		while (this.memCacheList.size() > MEMCACHE_MAX_TILE_COUNT)
		{
			String delUrl = (String)this.memCacheList.elementAt(0);
			this.memCacheList.removeElementAt(0);
			this.memCache.remove(delUrl);
		}
	}
    
    
    private boolean isOutOfMap(OsmPosition position)
    {
    	if (position.getTileNumX() < 0)
    		return true;
    	
    	if (position.getTileNumY() < 0)
    		return true;
    	
    	if (position.getTileNumX() > Float11.pow(2, position.getZoomLevel()) -1)
    		return true;

    	if (position.getTileNumY() > Float11.pow(2, position.getZoomLevel()) -1)
    		return true;
    	
    	return false;
    }
    
    
    private Bitmap getEmptyTile()
    {
    	Bitmap img = new Bitmap(OsmPosition.TILE_PIXEL_WIDTH, OsmPosition.TILE_PIXEL_HEIGHT);//Image.createImage(OsmPosition.TILE_PIXEL_WIDTH, OsmPosition.TILE_PIXEL_HEIGHT);
    	Graphics g = new Graphics(img);
    	
    	for (int y = 0; y < img.getHeight(); y += 8)
    	{
    		for (int x = 0; x < img.getWidth(); x += 8)
    		{
    			if (((x + y) / 8) % 2 == 0)
    				g.setColor(0xFFFFFF);
    			else
    				g.setColor(0xCCCCCC);
    				
    			g.fillRect(x, y, 8, 8);
    		}
    	}
    	
    	g.setColor(0x000000);
    	g.drawRect(0, 0, OsmPosition.TILE_PIXEL_WIDTH, OsmPosition.TILE_PIXEL_HEIGHT);
    	
    	return img;
    }
    
    
    private byte[] getByteArray(InputStream inStream)
    {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] bytes = null;
		
		try
		{
			int b;
			while ((b = inStream.read()) > -1)
				byteStream.write(b);
			
			bytes = byteStream.toByteArray();
			
			byteStream.close();
		}
		catch (IOException e)
		{
			Logger.Log(e);
		}
		
		return bytes;
    }
	

	private void notifyTileLoaded()
    {
    	for (int i = 0; i < this.listeners.size(); i++)
    		((OsmTileLoaderListener)this.listeners.elementAt(i)).osmTileLoaded();
    }
}
