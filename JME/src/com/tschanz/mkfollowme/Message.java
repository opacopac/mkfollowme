/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

import java.util.Date;

/**
 * User message that can be added to the message queue.
 */
public class Message
{
	/// Fields ///
	
	private String text;
	private String handle;
	private Date expireTime;
	
	
	/// Constructors ///
	
	public Message(String text)
	{
		this(text, null, 0);
	}
	
	public Message(String text, String handle, int displayTimeMs)
	{
		this.text = text;
		this.handle = handle;
		if (displayTimeMs > 0)
			this.expireTime = new Date(System.currentTimeMillis() + displayTimeMs);
		else
			this.expireTime = null;
	}
	
	
	/// Public Methods ///
	
	public void setText(String text)
	{
		this.text = text;
	}


	public String getText()
	{
		return this.text;
	}


	public String getHandle() {
		return handle;
	}

	
	public boolean isExpired()
	{
		if (this.expireTime != null && System.currentTimeMillis() > this.expireTime.getTime())
			return true;
		else
			return false;
	}
}
