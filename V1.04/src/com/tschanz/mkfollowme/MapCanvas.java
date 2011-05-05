/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import com.tschanz.mkfollowme.MessageQueue.MessageQueueListener;
import com.tschanz.osm4mobile.LatLon;
import com.tschanz.osm4mobile.OsmMapPainter;
import com.tschanz.osm4mobile.OsmPosition;
import com.tschanz.osm4mobile.OsmTileLoader;
import com.tschanz.osm4mobile.OsmTileLoaderListener;


/**
 * The main canvas of this program displaying the map.
 */
public class MapCanvas extends Canvas implements CommandListener, OsmTileLoaderListener, MessageQueueListener
{
	/// Fields ///
	private final int OFFSET_STEP_X = 20;
	private final int OFFSET_STEP_Y = 20;
	private MKFollowMe midlet;
	private Command cmdExit;
	private Command cmdSearchBTDevice;
	private Command cmdSearchSerialPort;
	private Command cmdToggleLocationUpdates;
	private Command cmdToggleFollowMeMode;
	private Command cmdSendWaypoints;
	private Command cmdClearWaypoints;
	private Command cmdSettings;
	private LatLon devicePos, mkPos, mkHomePos;
	private int operationRadius;
	private OsmPosition mapCoordinates;
	private Vector wayPointList;

    	
	/// Constructors ///

	/**
	 * Class constructor.
	 */
	public MapCanvas(MKFollowMe midlet)
	{
		this.midlet = midlet;

		// commands
		this.cmdExit = new Command("Exit", Command.EXIT, 0);
		this.cmdToggleLocationUpdates = new Command("GPS On/Off", Command.ITEM, 1);
		this.cmdToggleFollowMeMode = new Command("FollowMe On/Off", Command.ITEM, 2);
		this.cmdSendWaypoints = new Command("Send Waypoints", Command.ITEM, 3);
		this.cmdClearWaypoints = new Command("Clear Waypoints", Command.ITEM, 4);
		this.cmdSearchBTDevice = new Command("Search BT Device", Command.ITEM, 5);
		this.cmdSearchSerialPort = new Command("Open Serial Port", Command.ITEM, 6);
		this.cmdSettings = new Command("Settings", Command.ITEM, 7);
		this.addCommand(this.cmdExit);
		this.addCommand(this.cmdToggleLocationUpdates);
		this.addCommand(this.cmdToggleFollowMeMode);
		this.addCommand(this.cmdSendWaypoints);
		this.addCommand(this.cmdClearWaypoints);
		this.addCommand(this.cmdSearchBTDevice);
		this.addCommand(this.cmdSearchSerialPort);
		this.addCommand(this.cmdSettings);
		this.setCommandListener(this);
		
		// coordinates
		this.mapCoordinates = new OsmPosition(1, 1, 1);
		this.devicePos = new LatLon();
		this.mkPos = new LatLon();
		this.mkHomePos = new LatLon();
		this.operationRadius = 0;
		this.wayPointList = new Vector();

		// listeners
		OsmTileLoader.getInstance().addListener(this);
		this.midlet.getMessageQueue().addListener(this);
	}
	
	
	/// Public Methods ///
	
	/**
	 * Called by OsmTileLoader when a new tile has been loaded.
	 * @see OsmTileLoader
	 * @see OsmTileLoaderListener
	 */
	public void osmTileLoaded()
	{
		this.repaint();
	}
	
	
	/**
	 * Called when a command has been invoked.
	 * @see CommandListener
	 */
	public void commandAction(Command cmd, Displayable d)
	{
		if (d != this)
			return;
		
		if (cmd == this.cmdExit)
			this.midlet.exitMIDlet();
		
		if (cmd == this.cmdToggleLocationUpdates)
			this.midlet.toggleGpsLocationUpdates();
		
		if (cmd == this.cmdToggleFollowMeMode)
			this.midlet.toggleFollowMe();
		
		if (cmd == this.cmdSendWaypoints)
			this.midlet.sendWayPointList(this.wayPointList);

		if (cmd == this.cmdClearWaypoints)
		{
			this.wayPointList.removeAllElements();
			this.repaint();
		}
		
		if (cmd == this.cmdSearchBTDevice)
			this.midlet.startBluetoothDeviceSearch();

		if (cmd == this.cmdSearchSerialPort)
			this.midlet.showMenuSerialPort();
		
		if (cmd == this.cmdSettings)
			this.midlet.showMenuSettings();
	}
	
	
	/**
	 * Returns the current map drawing position.
	 * @return the current drawing position of the map (anchor point: crosshair)  
	 */
	public OsmPosition getMapPosition()
	{
		return this.mapCoordinates;
	}
	
	
	/**
	 * Sets the map drawing position.
	 * @param pos - new drawing position of the map (anchor point: crosshair)
	 */
	public void setMapPosition(OsmPosition pos)
	{
		this.mapCoordinates = pos;
		this.repaint();
	}
	
	
	/**
	 * Sets the drawing position of the mobile device (blue dot). 
	 * @param pos - new position of the mobile device
	 */
	public void setDevicePosition(LatLon pos)
	{
		this.devicePos = pos;
		this.repaint();
	}

	
	/**
	 * Sets the drawing position of the MK (red dot).
	 * @param pos - new drawing position of the MK
	 */
	public void setMKPosition(LatLon pos)
	{
		this.mkPos = pos;
		this.repaint();
	}
	
	
	public void setMkHomePos(LatLon homePos)
	{
		this.mkHomePos = homePos;
	}


	public LatLon getHomePos()
	{
		return this.mkHomePos;
	}
	
	
	public void setOperationRadius(int operationRadius)
	{
		this.operationRadius = operationRadius;
	}


	public int getOperationRadius()
	{
		return this.operationRadius;
	}


	/**
	 * Called by MessageQueue when a the message queue has been updated.
	 * @see MessageQueue
	 * @see MessageQueueListener 
	 */
	public void messageQueueUpdated()
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

		// mk home position
		if (!this.mkHomePos.isZero())
		{
			OsmMapPainter.drawPositionMark(g, this.mapCoordinates, this.mkHomePos, 0xFFFF00);

			// operation radius
			if (this.operationRadius > 0)
				OsmMapPainter.drawCircle(g, this.mapCoordinates, this.mkHomePos, this.operationRadius, 0xFFFF00);
		}
		
		// device position
		if (!this.devicePos.isZero())
			OsmMapPainter.drawPositionMark(g, this.mapCoordinates, this.devicePos, 0x0000FF);
		
		// mk position
		if (!this.mkPos.isZero())
			OsmMapPainter.drawPositionMark(g, this.mapCoordinates, this.mkPos, 0xFF0000);
		
		// crosshair
		OsmMapPainter.drawCrossHair(g);
		
		// scale
		OsmMapPainter.drawScale(g, this.mapCoordinates);
		
		// messages
		g.setColor(0x000000);
		Message[] msgs = this.midlet.getMessageQueue().getMessages();
		for (int i = 0; i < msgs.length; i++)
			g.drawString(msgs[i].getText(), 5, 5 + i * 15, Graphics.TOP | Graphics.LEFT);
	}
	
	
	protected void keyPressed(int keyCode)
	{
		this.handleKeyCode(keyCode);
	}
	
	
	protected void keyRepeated(int keyCode)
	{
		this.handleKeyCode(keyCode);
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
				
			case Canvas.GAME_A:
				this.mapCoordinates.incZoomLevel();
				break;
				
			case Canvas.GAME_B:
				this.mapCoordinates.decZoomLevel();
				break;
				
			case Canvas.GAME_C:
				this.centerMap();
				break;
				
			default:
				switch (keyCode)
				{
					case Canvas.KEY_NUM0:
						this.centerMap();
						break;
				
					case Canvas.KEY_NUM1:
						this.mapCoordinates.incZoomLevel();
						break;
					
					case Canvas.KEY_NUM2:
						this.mapCoordinates.incPositionY(0, -this.OFFSET_STEP_Y);
						break;
					
					case Canvas.KEY_NUM3:
						this.mapCoordinates.decZoomLevel();
						break;

					case Canvas.KEY_NUM4:
						this.mapCoordinates.incPositionX(0, -this.OFFSET_STEP_X);
						break;
					
					case Canvas.KEY_NUM5:
						this.wayPointList.addElement(this.mapCoordinates.getLatLong());
						break;
					
					case Canvas.KEY_NUM6:
						this.mapCoordinates.incPositionX(0, this.OFFSET_STEP_X);
						break;
					
					case Canvas.KEY_NUM8:
						this.mapCoordinates.incPositionY(0, this.OFFSET_STEP_Y);
						break;
				}
				break;
		}
		
		this.repaint();
	}
	
	
	private void centerMap()
	{
		if (this.devicePos.isZero() && this.mkPos.isZero())
		{
			this.mapCoordinates.setPosition(1, 1, 1);
			return;
		}
		
		if (this.devicePos.isZero() && !this.mkPos.isZero())
		{
			this.mapCoordinates.setPosition(16, new LatLon(this.mkPos.latitude, this.mkPos.longitude));
			return;
		}
		
		if (!this.devicePos.isZero() && this.mkPos.isZero())
		{
			this.mapCoordinates.setPosition(16, new LatLon(this.devicePos.latitude, this.devicePos.longitude));
			return;
		}

		double newLat = (this.devicePos.latitude + this.mkPos.latitude) / 2;
		double newLon = (this.devicePos.longitude + this.mkPos.longitude) / 2;
		this.mapCoordinates.setPosition(16, new LatLon(newLat, newLon));
	}
	
	
	private void clearScreen(Graphics g)
	{
		g.setColor(0xFFFFFF);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
	}
}
