/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import java.util.Vector;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.container.MainScreen;

public class OsmDemoCanvas extends MainScreen implements OsmTileLoaderListener
{
	/// Fields ///
	private final int OFFSET_STEP_X = 20;
	private final int OFFSET_STEP_Y = 20;
	private OsmPosition mapCoordinates;
	private Vector wayPointList;
	private static final int SEP_IDX2 = 0x00020000;
    	
	/// Constructors ///
	
	public OsmDemoCanvas(OsmDemoMidlet midlet)
	{
		// init coordinates
		addMenuItem(new ClearWayPoints());
		
		this.mapCoordinates = new OsmPosition(1, 1, 1);
		
		this.wayPointList = new Vector();
		// add listener
		OsmTileLoader.getInstance().addListener(this);
	}
	
	
	/// Public Methods ///
	
	public OsmPosition getMapPosition()
	{
		return this.mapCoordinates;
	}
	
	
	public void setMapPosition(OsmPosition pos)
	{
		this.mapCoordinates = pos;
		invalidate();
	}
	
	
	/**
	 * Called by OsmTileLoader when a new tile has been loaded.
	 * @see OsmTileLoader
	 */
	public void osmTileLoaded()
	{
		invalidate();
	}

	
	/// Protected Methods ///
	
	protected void paint(Graphics g)
	{
		g.clear();
		this.clearScreen(g);
		
		// map
		OsmMapPainter.drawMap(g, this.mapCoordinates);
		
		// waypoints
		for (int i = 0; i < this.wayPointList.size(); i++)
		{
			OsmMapPainter.drawPositionMark(g, this.mapCoordinates, (LatLon)this.wayPointList.elementAt(i), 0x00FF00);
			if (i > 0)
				OsmMapPainter.drawLine(g, this.mapCoordinates, (LatLon)this.wayPointList.elementAt(i - 1), (LatLon)this.wayPointList.elementAt(i), 0x00FF00);
		}

		// crosshair
		OsmMapPainter.drawCrossHair(g);
		
		// coordinates
		g.drawText("Lat: " + this.mapCoordinates.getLatLong().latitude, 5, 5, Graphics.TOP | Graphics.LEFT);
		g.drawText("Lon: " + this.mapCoordinates.getLatLong().longitude, 5, 20, Graphics.TOP | Graphics.LEFT);
	}
	
	public boolean keyUp(int keycode, int time) {
		return super.keyUp(keycode, time);
	}
	
	public boolean keyChar(char key, int status, int time){
		if (key==Characters.DIGIT_TWO){
		//case Canvas.UP:
			this.mapCoordinates.incPositionY(0, -this.OFFSET_STEP_Y);
		}else if (key==Characters.DIGIT_EIGHT){
			
		//case Canvas.DOWN:
			this.mapCoordinates.incPositionY(0, this.OFFSET_STEP_Y);
		}else if (key==Characters.DIGIT_FOUR){	
			
		//case Canvas.LEFT:
			this.mapCoordinates.incPositionX(0, -this.OFFSET_STEP_X);
		}else if (key==Characters.DIGIT_SIX){
			
		//case Canvas.RIGHT:
			this.mapCoordinates.incPositionX(0, this.OFFSET_STEP_X);
		}else if (key==Characters.DIGIT_FIVE){
		//case Canvas.FIRE:
			this.wayPointList.addElement(this.mapCoordinates.getLatLong());
			
		}else if (key==Characters.LATIN_CAPITAL_LETTER_I || key==Characters.LATIN_SMALL_LETTER_I){
			this.mapCoordinates.incZoomLevel();
		}else if (key==Characters.LATIN_CAPITAL_LETTER_O || key==Characters.LATIN_SMALL_LETTER_O){
			this.mapCoordinates.decZoomLevel();
		}
		invalidate();
		return super.keyChar(key, status, time);
		
	}
	
	public boolean navigationMovement(int dx,int dy,int status,int time){
		if (dx<0){//left
			this.mapCoordinates.incPositionX(0, -this.OFFSET_STEP_X);
		}else if (dx>0){//right
			this.mapCoordinates.incPositionX(0, this.OFFSET_STEP_X);
		}
		if (dy<0){//up
			this.mapCoordinates.incPositionY(0, -this.OFFSET_STEP_Y);
		}else if (dy>0){//down
			this.mapCoordinates.incPositionY(0, this.OFFSET_STEP_Y);
		}
		invalidate();
		return super.navigationMovement(dx, dy, status, time);
	}
	
	public boolean 	keyDown(int keycode, int time){
		return super.keyDown(keycode, time);
		
	}
	public boolean 	keyRepeat(int keycode, int time){
		return super.keyRepeat(keycode, time);
	}
    
	public boolean 	keyStatus(int keycode, int time){
		return super.keyStatus(keycode, time);
	}

	
	private class ClearWayPoints extends MenuItem {

		public ClearWayPoints() {
			super( "Clear Waypoints", SEP_IDX2 + 5, 1 );
		}

		public void run() {
			wayPointList.removeAllElements();
			invalidate();
		}
	}
	
	private void clearScreen(Graphics g)
	{
		g.setColor(0xFFFFFF);
		g.fillRect(0, 0, g.getClippingRect().width, g.getClippingRect().height);
	}
}


