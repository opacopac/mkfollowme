/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import com.tschanz.osm4mobile.OsmTileLoader;
import com.tschanz.osm4mobile.RecordStoreHelper;

public class MenuSettings extends Form implements CommandListener
{
	/// Fields ///
	
	private MKFollowMe midlet;
	private Command cmdSave;
	private Command cmdCancel;
	private Command cmdClearCache;
	private TextField txtHoldTime;
	private TextField txtToleranceRadius;

	
	/// Constructors ///
	
	public MenuSettings(String title, MKFollowMe midlet)
	{
		super(title);
		
		this.midlet = midlet;

		// add commands
		this.cmdCancel = new Command("Back", Command.BACK, 1);
		this.cmdSave = new Command("Save", Command.ITEM, 2);
		this.cmdClearCache = new Command("Clear Cache", Command.ITEM, 3);

		this.addCommand(this.cmdSave);
		this.addCommand(this.cmdCancel);
		this.addCommand(this.cmdClearCache);
	    this.setCommandListener(this);
	    
	    // init form items
	    String holdTime = (new Integer(this.midlet.getSettings().getWPHoldTime())).toString();
		this.txtHoldTime = new TextField("Hold Time [s]", holdTime , 3, TextField.NUMERIC);

		String toleranceRadius = (new Integer(this.midlet.getSettings().getWPToleranceRadius())).toString();
		this.txtToleranceRadius = new TextField("Tolerance Radius [m]", toleranceRadius, 3, TextField.NUMERIC);
		
		String cacheSize = (new Integer(RecordStoreHelper.getInstance().getTileStoreSize())).toString(); 

		// build form
		this.append("Waypoints:");
		this.append(this.txtHoldTime);
		this.append(this.txtToleranceRadius);
		this.append("Map Cache:");
		this.append(cacheSize + " Bytes");
	}
	
	
	/// Public Methods

	/**
	 * Called by the API when a command (from menu) has been triggered.
	 */
	public void commandAction(Command cmd, Displayable disp)
	{
		if (disp != this)
			return;
		
		if (cmd == this.cmdSave)
		{
			int holdTime = Integer.parseInt(this.txtHoldTime.getString());
			this.midlet.getSettings().setWPHoldTime(holdTime);

			int toleranceRadius = Integer.parseInt(this.txtToleranceRadius.getString());
			this.midlet.getSettings().setWPToleranceRadius(toleranceRadius);
			
			this.midlet.getMessageQueue().addMessage("Settings saved!", null, 5000);

			this.midlet.showMapCanvas();
		}
		
		if (cmd == this.cmdClearCache)
		{
			OsmTileLoader.getInstance().ClearCache();
			this.midlet.showMenuSettings();
		}
		
		if (cmd == this.cmdCancel)
			this.midlet.showMapCanvas();
	}
}
