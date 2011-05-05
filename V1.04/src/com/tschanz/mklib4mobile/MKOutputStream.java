/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile;

import java.io.IOException;
import java.io.OutputStream;


public class MKOutputStream extends OutputStream
{
	/// Fields ///
	
	private OutputStream out;
	
	
	/// Constructors ///
	
	public MKOutputStream(OutputStream out)
	{
		this.out = out;
	}
	
	
	/// Public Methods ///
	
	public void write(int value) throws IOException
	{
		this.out.write(value & 0xFF);
	}
	
	
	public void writeByte(int value) throws IOException
	{
		this.out.write(value & 0xFF);
	}
	
	
	public void writeShort(int value) throws IOException
	{
		this.out.write((value & 0x00FF) >> 0);
		this.out.write((value & 0xFF00) >> 8);
	}
	
	
	public void writeInt(int value) throws IOException
	{
		this.out.write((value & 0x000000FF) >> 0);
		this.out.write((value & 0x0000FF00) >> 8);
		this.out.write((value & 0x00FF0000) >> 16);
		this.out.write((value & 0xFF000000) >> 24);
	}
}
