/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import com.tschanz.mklib4mobile.MKCommunicator;
import com.tschanz.mklib4mobile.SerialHelper;
import com.tschanz.mklib4mobile.SerialListener;

/**
 * Displays a list of all available serial ports.
 */
public class MenuSerialPort extends List implements CommandListener, SerialListener
{
	private MKFollowMe midlet;
    private Command cmdConnectSerialPort;
    private Command cmdCancel;
    private SerialHelper serialHelper;

	
	public MenuSerialPort(String title, int listType, MKFollowMe midlet)
	{
		super(title, listType);
		
		this.midlet = midlet;
	
		String[] ports = SerialHelper.getPortNames();
		
		if (ports != null)
		{
	    	for (int i = 0; i < ports.length; i++)
	    		this.append(ports[i], null);
	
	    	this.cmdConnectSerialPort = new Command("Connect", Command.ITEM, 2);
	    	this.setSelectCommand(this.cmdConnectSerialPort);
		}
		else
			this.append("No Serial Port Found!", null);
	
		this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
		this.addCommand(cmdCancel);
	    this.setCommandListener(this);
	}


	/**
	 * Called by the API when a command (from menu) has been triggered.
	 */
	public void commandAction(Command cmd, Displayable disp)
	{
		if (disp != this)
			return;
		
		if (cmd == this.cmdConnectSerialPort)
		{
	    	this.midlet.getMessageQueue().addMessage("Connecting...", "connectserial", 0);
	    	this.serialHelper = new SerialHelper();
			this.serialHelper.openSerialPort(this.getString(this.getSelectedIndex()));
			this.serialHelper.addListener(this);
			
			this.midlet.showMapCanvas();
		}
		
		if (cmd == this.cmdCancel)
			this.midlet.showMapCanvas();
	}
    
    
	/**
	 * Called by the SerialHelper when a serial port has been opened.
	 * @see SerialHelper
	 * @see SerialListener 
	 */
	public void serialPortOpened(StreamConnection connection)
	{
		this.midlet.setCommunicator(new MKCommunicator(connection));
		this.midlet.getCommunicator().addListener(this.midlet);
		
		this.midlet.sendMKInitCommands();

		this.midlet.getMessageQueue().delete("connectserial");
		this.midlet.getMessageQueue().addMessage("Connected!", null, 5000);

		this.serialHelper.destroy();
	}		
}
