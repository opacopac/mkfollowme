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
import java.util.Hashtable;
import java.util.Vector;

public class OsmTileLookupTable implements ByteSerializable
{
	/// Fields ///
	private Hashtable lookupTable;
	private Vector lookupList;
	
	
	/// Constructors ///
	
	public OsmTileLookupTable()
	{
		this.lookupTable = new Hashtable();
		this.lookupList = new Vector();
	}
	
	
	/// Public Methods ///
	
	public boolean contains(String url)
	{
		return this.lookupTable.containsKey(url);
	}

	
	public int getRecordId(String url)
	{
		Integer id = (Integer)this.lookupTable.get(url);
		if (id == null)
			return -1;
		else
			return id.intValue();
	}
	
	
	public int getOldestRecordId()
	{
		String url = (String)this.lookupList.firstElement();
		if (url == null)
			return -1;
		else
			return getRecordId(url);
	}
	

	public void put(String url, int recordId)
	{
		Integer i = new Integer(recordId);

		if (!this.lookupList.contains(url))
			this.lookupList.addElement(url);
		this.lookupTable.put(url, i);
	}
	
	
	public void remove(String url)
	{
		this.lookupList.removeElement(url);
		this.lookupTable.remove(url);
	}
	
	
	public void remove(int recordId)
	{
		for (int i = 0; i < this.lookupList.size(); i++)
		{
			String url = (String)this.lookupList.elementAt(i);
			Integer id = (Integer)this.lookupTable.get(url);
			
			if (id != null && id.intValue() == recordId)
			{
				this.remove(url);
				break;
			}
		}
	}
	
	
	public void clear()
	{
		this.lookupList.removeAllElements();
		this.lookupTable.clear();
	}
	
	
	public byte[] getAsByteArray() throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeInt(this.lookupList.size());

		for (int i = 0; i < this.lookupList.size(); i++)
		{
			String url = (String)this.lookupList.elementAt(i);
			Integer id = (Integer)this.lookupTable.get(url);
			
			dos.writeUTF(url);
			dos.writeInt(id.intValue());
		}
		dos.flush();
		
		byte[] bytes = bos.toByteArray();
		
		dos.close();
		bos.close();
		
		return bytes;
	}


	public void loadFromByteArray(byte[] bytes) throws IOException
	{
		this.clear();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bis);
		
		int size = dis.readInt();
		
		for (int i = 0; i < size; i++)
		{
			String url = dis.readUTF();
			Integer id = new Integer(dis.readInt());
			
			this.lookupList.addElement(url);
			this.lookupTable.put(url, id);
		}
		
		dis.close();
		bis.close();
	}
}
