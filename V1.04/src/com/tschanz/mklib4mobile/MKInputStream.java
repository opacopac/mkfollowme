/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile;

import java.io.IOException;
import java.io.InputStream;


public class MKInputStream extends InputStream
{
	/// Fields ///
	
	private InputStream in;
	
	
	/// Constructors ///
	
	public MKInputStream(InputStream in)
	{
		this.in = in;
	}

	
	public int read() throws IOException
	{
		return this.in.read();
	}

	
	public byte readByte() throws IOException
	{
		int value = this.in.read();
		
		return (byte)value;
	}
	
	
	public int readUnsignedByte() throws IOException
	{
		return this.in.read();
	}
	
	
	public short readShort() throws IOException
	{
		int value = (this.in.read() << 0)
		          | (this.in.read() << 8);
		
		return (short)value;
	}

	
	public int readUnsignedShort() throws IOException
	{
		int value = (this.in.read() << 0) 
		          | (this.in.read() << 8);

		return value;
	}
	
	
	public int readInt() throws IOException
	{
		int value = (this.in.read() << 0)
			      | (this.in.read() << 8)
			      | (this.in.read() << 16)
		          | (this.in.read() << 24);
		
		return value;
	}
}
