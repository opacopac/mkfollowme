/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Message queue handling a list of user messages and deleting expired ones.  
 * @author Armand Tschanz (armand@tschanz.com)
 * @see "README file for more info"
 *
 */
public class MessageQueue extends TimerTask 
{
	/// Fields ///
	
	private Vector messageList;
	private Vector listeners;
	private Timer timer;
	
	
	/// Constructor ///
	
	public MessageQueue()
	{
		this.messageList = new Vector();
		this.listeners = new Vector();
		this.timer = new Timer();
		this.timer.schedule(this, 0, 500);
	}

	
	/// Public Methods ///
	
	public void addListener(MessageQueueListener listener)
	{
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}
	
	
	public void removeListener(MessageQueueListener listener)
	{
		if (this.listeners.contains(listener))
			this.listeners.removeElement(listener);
	}
	
	
	public void addMessage(Message msg)
	{
		this.messageList.addElement(msg);
		this.notifyMessageQueueUpdated();
	}
	
	
	public void addMessage(String text, String handle, int displayTimeMs)
	{
		this.messageList.addElement(new Message(text, handle, displayTimeMs));
		this.notifyMessageQueueUpdated();
	}
	
	
	public Message[] getMessages()
	{
		Message[] msgs = new Message[this.messageList.size()];
		
		for (int i = 0; i < this.messageList.size(); i++)
			msgs[i] = (Message)this.messageList.elementAt(i);
		
		return msgs;
	}
	
	
	public void clear()
	{
		this.messageList.removeAllElements();
		this.notifyMessageQueueUpdated();
	}
	
	
	public void delete(String handle)
	{
		boolean updated = false;
		
		for (int i = 0; i < this.messageList.size(); i++)
		{
			if (((Message)this.messageList.elementAt(i)).getHandle() == handle)
			{
				this.messageList.removeElementAt(i);
				i--;
				updated = true;
			}
		}
		
		if (updated)
			this.notifyMessageQueueUpdated();
	}
	
	
	public void run()
	{
		boolean updated = false;
		
		for (int i = 0; i < this.messageList.size(); i++)
		{
			if (((Message)this.messageList.elementAt(i)).isExpired())
			{
				this.messageList.removeElementAt(i);
				i--;
				updated = true;
			}
		}
		
		if (updated)
			this.notifyMessageQueueUpdated();
	}
	
	/// Private Methods ///
	
    private void notifyMessageQueueUpdated()
    {
    	for (int i = 0; i < this.listeners.size(); i++)
    		((MessageQueueListener)this.listeners.elementAt(i)).messageQueueUpdated();
    }
	
	
	/// Interfaces ///
	
	public interface MessageQueueListener
	{
		void messageQueueUpdated();
	}
}
