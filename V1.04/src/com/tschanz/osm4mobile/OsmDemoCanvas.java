/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

public class OsmDemoCanvas extends Canvas implements CommandListener, OsmTileLoaderListener
{
	/// Fields ///
	private final int OFFSET_STEP_X = 20;
	private final int OFFSET_STEP_Y = 20;
	private Command cmdExit;
	private Command cmdClearWaypoints;
	private OsmPosition mapCoordinates;
	private OsmDemoMidlet midlet;
	private Vector wayPointList;

    	
	/// Constructors ///
	
	public OsmDemoCanvas(OsmDemoMidlet midlet)
	{
		this.midlet = midlet;
		
		// add commands
		this.cmdExit = new Command("Exit", Command.EXIT, 0);
		this.cmdClearWaypoints = new Command("Clear Waypoints", Command.ITEM, 2);
		this.addCommand(this.cmdExit);
		this.addCommand(this.cmdClearWaypoints);
		this.setCommandListener(this);
		
		// init coordinates
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
		this.repaint();
	}
	
	
	/**
	 * Called by OsmTileLoader when a new tile has been loaded.
	 * @see OsmTileLoader
	 */
	public void osmTileLoaded()
	{
		this.repaint();
	}

	
	/// Protected Methods ///
	
	protected void paint(Graphics g)
	{
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
		g.drawString("Lat: " + this.mapCoordinates.getLatLong().latitude, 5, 5, Graphics.TOP | Graphics.LEFT);
		g.drawString("Lon: " + this.mapCoordinates.getLatLong().longitude, 5, 20, Graphics.TOP | Graphics.LEFT);
	}
	
	
	protected void keyPressed(int keyCode)
	{
		this.handleKeyCode(keyCode);
	}
	
	
	protected void keyRepeated(int keyCode)
	{
		this.handleKeyCode(keyCode);
	}


	public void commandAction(Command cmd, Displayable d)
	{
		if (cmd == this.cmdExit)
			this.midlet.exitMIDlet();
		
		if (cmd == this.cmdClearWaypoints)
		{
			this.wayPointList.removeAllElements();
			this.repaint();
		}
	}

	
	/// Private Methods ///
	
	private void handleKeyCode(int keyCode)
	{
		switch (getGameAction(keyCode))
		{
			case Canvas.UP:
				this.mapCoordinates.incPositionY(0, -this.OFFSET_STEP_Y);
				break;
				
			case Canvas.DOWN:
				this.mapCoordinates.incPositionY(0, this.OFFSET_STEP_Y);
				break;
				
			case Canvas.LEFT:
				this.mapCoordinates.incPositionX(0, -this.OFFSET_STEP_X);
				break;
				
			case Canvas.RIGHT:
				this.mapCoordinates.incPositionX(0, this.OFFSET_STEP_X);
				break;
				
			case Canvas.FIRE:
				this.wayPointList.addElement(this.mapCoordinates.getLatLong());
				break;
				
			default:
				switch (keyCode)
				{
					case Canvas.KEY_NUM1:
						this.mapCoordinates.incZoomLevel();
						break;
					
					case Canvas.KEY_NUM3:
						this.mapCoordinates.decZoomLevel();
						break;
				}
				break;
		}
		
		this.repaint();
	}
	
	
	private void clearScreen(Graphics g)
	{
		g.setColor(0xFFFFFF);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
	}
}


