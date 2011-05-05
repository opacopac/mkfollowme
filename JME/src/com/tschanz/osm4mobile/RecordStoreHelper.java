/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import com.tschanz.utils.Logger;

public class RecordStoreHelper
{
	/// Fields ///
	private static final String StoreNameTileStore = "OsmTileStore";
	private static final String StoreNameTileLookupTable = "OsmTileLookupTable";
	private static final String StoreNameLastMapPosition = "LastMapPosition";
	private static RecordStoreHelper instance = new RecordStoreHelper();
	private RecordStore osmTileStore;


	/// Constructor ///
	
	private RecordStoreHelper()
	{
		this.osmTileStore = openStore(StoreNameTileStore, true);
	}
	
	
	/// Public Methods ///
	
	public static RecordStoreHelper getInstance()
	{
		return instance;
	}
	
	
	public static OsmPosition loadMapPosition()
	{
		RecordStore store = openStore(StoreNameLastMapPosition, true);
		if (store == null)
			return null;
		
		int recordId = getSingleRecordId(store);
		if (recordId <= 0)
			return null;
		
		byte[] bytes = getRecord(store, recordId);
		if (bytes == null)
			return null;
		
		closeStore(store);
		
		OsmPosition pos = new OsmPosition();
		
		try
		{
			pos.loadFromByteArray(bytes);
		}
		catch (IOException e)
		{
			Logger.Log(e);
			return null;
		}
		
		return pos;
	}
	

	public static void saveMapPosition(OsmPosition pos)
	{
		RecordStore store = openStore(StoreNameLastMapPosition, true);
		if (store == null)
			return;
		
		int recordId = getSingleRecordId(store);
		
		byte[] bytes;
		try
		{
			bytes = pos.getAsByteArray();
		}
		catch (IOException e)
		{
			Logger.Log(e);
			return;
		}

		if (bytes == null)
			return;
		
		saveRecord(store, recordId, bytes);
		closeStore(store);
	}
	
	
	public static byte[] loadTileLookupTable()
	{
		RecordStore store = openStore(StoreNameTileLookupTable, true);
		if (store == null)
			return null;
		
		int recordId = getSingleRecordId(store);
		if (recordId < 0)
			return null;
		
		byte[] bytes = getRecord(store, recordId);
		
		closeStore(store);
		
		return bytes;
	}
	

	public static void saveTileLookupTable(byte[] bytes)
	{
		if (bytes == null)
			return;

		RecordStore store = openStore(StoreNameTileLookupTable, true);
		if (store == null)
			return;
		
		int recordId = getSingleRecordId(store);
		
		saveRecord(store, recordId, bytes);
		closeStore(store);
	}
	
	
	public int getTileStoreSize()
	{
		try
		{
			return this.osmTileStore.getSize();
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
			return -1;
		}
	}
	
	
	public int getTileStoreSizeAvailable()
	{
		try
		{
			return this.osmTileStore.getSizeAvailable();
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
			return -1;
		}
	}
	
	
	public int getTileStoreNumRecords()
	{
		try
		{
			return this.osmTileStore.getNumRecords();
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
			return 0;
		}
	}
	
	
	public byte[] loadOsmTile(int recordId)
	{
		return getRecord(this.osmTileStore, recordId);
	}
	
	
	public int saveOsmTile(int recordId, byte[] bytes)
	{
		return saveRecord(this.osmTileStore, recordId, bytes);
	}
	
	
	public void deleteOsmTile(int recordId)
	{
		deleteRecord(this.osmTileStore, recordId);
	}
	
	
	public void deleteAllOsmTiles()
	{
		closeStore(this.osmTileStore);
		deleteStore(StoreNameTileStore);
		this.osmTileStore = openStore(StoreNameTileStore, true);
	}
	

	/// Private Methods ///
	
	private static RecordStore openStore(String storeName, boolean createIfNecessary)
	{
		try
		{
			return RecordStore.openRecordStore(storeName, createIfNecessary);
		}
		catch (RecordStoreFullException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreNotFoundException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreException e)
		{
			Logger.Log(e);
		}
		
		return null;
	}
	
	
	private static void closeStore(RecordStore store)
	{
		try
		{
			store.closeRecordStore();
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreException e)
		{
			Logger.Log(e);
		}
	}

	
	public static void deleteStore(String storeName)
	{
		try
		{
			RecordStore.deleteRecordStore(storeName);
		}
		catch (RecordStoreNotFoundException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreException e)
		{
			Logger.Log(e);
		}
	}

	
	private static int getSingleRecordId(RecordStore store)
	{
		RecordEnumeration recEnum = getRecordEnumeration(store);
		
		if (recEnum == null || !recEnum.hasNextElement())
			return -1;

		try
		{
			return recEnum.nextRecordId();
		}
		catch (InvalidRecordIDException e)
		{
			Logger.Log(e);
		}
		
		return -1;
	}
	
	
	private static RecordEnumeration getRecordEnumeration(RecordStore store)
	{
		try
		{
			return store.enumerateRecords(null, null, false);
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
		}

		return null;
	}

	
	private static byte[] getRecord(RecordStore store, int recordId)
	{
		try
		{
			return store.getRecord(recordId);
		}
		catch (InvalidRecordIDException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreException e)
		{
			Logger.Log(e);
		}
		
		return null;
	}
	
	
	private static int saveRecord(RecordStore store, int recordId, byte[] bytes)
	{
		try
		{
			if (recordId > 0)
			{
				store.setRecord(recordId, bytes, 0, bytes.length);
				return recordId;
			}
			else
				return store.addRecord(bytes, 0, bytes.length);
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
		}
		catch (InvalidRecordIDException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreFullException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreException e)
		{
			Logger.Log(e);
		}
		
		return -1;
	}
	
	
	private static void deleteRecord(RecordStore store, int recordId)
	{
		try
		{
			store.deleteRecord(recordId);
		}
		catch (RecordStoreNotOpenException e)
		{
			Logger.Log(e);
		}
		catch (InvalidRecordIDException e)
		{
			Logger.Log(e);
		}
		catch (RecordStoreException e)
		{
			Logger.Log(e);
		}
	}
}
