/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class OsmDemoMidlet extends MIDlet
{
	/// Fields ///
	
	private Display display;
	private OsmDemoCanvas mapCanvas;


	/// Public Methods ///
    
    public void exitMIDlet()
    {
    	this.destroyApp(false);
    	this.notifyDestroyed();
    }
    
    
    /// Protected Methods ///
	
	protected void startApp() throws MIDletStateChangeException
	{
    	this.display = Display.getDisplay(this);
    	this.mapCanvas = new OsmDemoCanvas(this);
    	this.display.setCurrent(this.mapCanvas);
    	this.restoreLastMapPos();
	}
	
	
	protected void pauseApp()
	{
	}

	
	protected void destroyApp(boolean arg0)
	{
		this.saveState();
	}
	
	
	/// Private Methods ///
	
	private void restoreLastMapPos()
	{
		OsmPosition pos = RecordStoreHelper.loadMapPosition();
		
		if (pos != null)
			this.mapCanvas.setMapPosition(pos);
	}
	
	
	private void saveState()
	{
    	RecordStoreHelper.saveMapPosition(this.mapCanvas.getMapPosition());
    	OsmTileLoader.getInstance().SaveState();
	}
}
