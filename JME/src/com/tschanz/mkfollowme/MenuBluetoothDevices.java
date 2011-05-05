/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

import java.io.IOException;

import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import com.tschanz.bluetoothapi.BluetoothDevice;
import com.tschanz.mklib4mobile.MKCommunicator;
import com.tschanz.utils.Logger;

public class MenuBluetoothDevices extends List implements CommandListener
{
	private MKFollowMe midlet;
    private Command cmdConnectBTDevice;
    private Command cmdCancel;
    private BluetoothDevice[] btDevices;

	
	public MenuBluetoothDevices(String title, int listType, MKFollowMe midlet, BluetoothDevice[] deviceList)
	{
		super(title, listType);

		this.midlet = midlet;
		this.btDevices = deviceList;
    	
    	if (deviceList != null)
    	{
	    	for (int i = 0; i < deviceList.length; i++)
	    		this.append(deviceList[i].getFriendlyName(), null);

	    	this.cmdConnectBTDevice = new Command("Connect", Command.ITEM, 2);
	    	this.setSelectCommand(this.cmdConnectBTDevice);
    	}
    	else
    		this.append("No device found!", null);

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
		
		if (cmd == this.cmdConnectBTDevice)
		{
			if (this.midlet.getBluetoothProvider() == null)
			{
	        	this.midlet.getMessageQueue().addMessage("No Bluetooth API fount!", null, 5000);
				this.midlet.showMapCanvas();
				return;
			}
			
	    	try
			{
				StreamConnection connection = this.midlet.getBluetoothProvider().openConnection(this.btDevices[this.getSelectedIndex()]);
				this.midlet.setCommunicator(new MKCommunicator(connection));
				this.midlet.getCommunicator().addListener(this.midlet);
	    		
				this.midlet.sendMKInitCommands();

				this.midlet.getMessageQueue().addMessage("Connected!", null, 5000);
			}
			catch (IOException e)
			{
				Logger.Log(e);
				this.midlet.getMessageQueue().addMessage("Connection Failed!", null, 5000);
			}
			
			this.midlet.showMapCanvas();
		}
		
		if (cmd == this.cmdCancel)
			this.midlet.showMapCanvas();
	}
}
