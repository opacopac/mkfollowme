/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

import java.util.Vector;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

import com.tschanz.bluetoothapi.BluetoothDevice;
import com.tschanz.bluetoothapi.BluetoothListener;
import com.tschanz.bluetoothapi.BluetoothProvider;
import com.tschanz.locationapi.GpsCoordinates;
import com.tschanz.locationapi.GpsListener;
import com.tschanz.locationapi.GpsProvider;
import com.tschanz.mklib4mobile.MKCommunicator;
import com.tschanz.mklib4mobile.MKResponseListener;
import com.tschanz.mklib4mobile.command.navictrl.ReadOsdDataCommand;
import com.tschanz.mklib4mobile.command.navictrl.RedirectUartCommand;
import com.tschanz.mklib4mobile.command.navictrl.SerialLinkTestCommand;
import com.tschanz.mklib4mobile.command.navictrl.SetTargetPositionCommand;
import com.tschanz.mklib4mobile.command.navictrl.WriteWayPointCommand;
import com.tschanz.mklib4mobile.parameter.navictrl.GpsPosStatus;
import com.tschanz.mklib4mobile.parameter.navictrl.UartSelector;
import com.tschanz.mklib4mobile.parameter.navictrl.WayPoint;
import com.tschanz.mklib4mobile.response.MKResponse;
import com.tschanz.mklib4mobile.response.navictrl.FollowMeResponse;
import com.tschanz.mklib4mobile.response.navictrl.ReadOsdDataResponse;
import com.tschanz.mklib4mobile.response.navictrl.SerialLinkTestResponse;
import com.tschanz.mklib4mobile.response.navictrl.WriteWayPointResponse;
import com.tschanz.osm4mobile.LatLon;
import com.tschanz.osm4mobile.OsmPosition;
import com.tschanz.osm4mobile.OsmTileLoader;
import com.tschanz.osm4mobile.RecordStoreHelper;
import com.tschanz.utils.Utils;

/**
 * The main MIDlet class for this program 
 */
public class MKFollowMe
	extends
		MIDlet
	implements
		GpsListener,
		MKResponseListener,
		BluetoothListener
{
	/// Fields ///
	
    public Display display;
    private MapCanvas mapCanvas;
	private MKCommunicator communicator;
	private GpsProvider gpsProvider;
	private BluetoothProvider btProvider;
	private boolean isFirstLocationUpdate;
	private boolean isFollowingMe;
	private MessageQueue messageQueue;
	private Settings settings;

    
	/// Constructors ///
    
	public MKFollowMe()
    {
    }
    
    
    /// Public Methods ///
	
	/// getter/setter ///
	
	public void setSettings(Settings settings)
	{
		this.settings = settings;
	}


	public Settings getSettings()
	{
		return this.settings;
	}


	public MessageQueue getMessageQueue()
	{
		return this.messageQueue;
	}
	
	
	public GpsProvider getGpsProvider()
	{
		return this.gpsProvider;
	}
	
	
	public BluetoothProvider getBluetoothProvider()
	{
		return this.btProvider;
	}
	
	
	public void setCommunicator(MKCommunicator communicator)
	{
		this.communicator = communicator;
	}

       
	public MKCommunicator getCommunicator()
	{
		return this.communicator;
	}
	
	
	/// MIDlet ///
	
    public void startApp()
    {
    	this.settings = new Settings();
    	
    	// create queue for user messages
    	this.messageQueue = new MessageQueue();

    	// create canvas
    	this.mapCanvas = new MapCanvas(this);
    	
    	// init apis
    	this.btProvider = BluetoothProvider.getInstance();
		this.gpsProvider = GpsProvider.getInstance();
    	
		// add listeners
		if (this.gpsProvider != null)
			this.gpsProvider.addListener(this);
		if (this.btProvider != null)
			this.btProvider.addListener(this);

		// restore state
		this.restoreState();

		// show canvas
    	this.display = Display.getDisplay(this);
    	this.display.setCurrent(this.mapCanvas);
    	

    	this.messageQueue.addMessage("Welcome! :)", null, 20000);
    	this.messageQueue.addMessage("Move: u/d/l/r", null, 20000);
    	this.messageQueue.addMessage("Zoom: 1/3", null, 20000);
    	this.messageQueue.addMessage("Center: 0", null, 20000);
    	this.messageQueue.addMessage("Waypoint: fire", null, 20000);
    }
    
    
    public void pauseApp()
    {
    }
 
    
    public void destroyApp(boolean unconditional)
    {
    	this.saveState();
    }
 
    
    public void exitMIDlet()
    {
    	this.destroyApp(false);
    	this.notifyDestroyed();
    }
    
    
    public void toggleFollowMe()
    {
    	this.isFollowingMe = !this.isFollowingMe;
    	
    	if (this.isFollowingMe)
	   		this.messageQueue.addMessage("FollowMe is active!", null, 5000);
    	else
	   		this.messageQueue.addMessage("FollowMe is disabled!", null, 5000);
    }
    
    
    /// Display ///
    
    public void showMapCanvas()
    {
    	this.display.setCurrent(this.mapCanvas);
    }
    
    
	public void showMenuSerialPort()
	{
    	List list = new MenuSerialPort("Connect to MK", List.IMPLICIT, this);
        this.display.setCurrent(list);
    }

	
	public void showMenuBluetoothDevices(BluetoothDevice[] deviceList)
	{
    	List list = new MenuBluetoothDevices("Connect to MK", List.IMPLICIT, this, deviceList);
        this.display.setCurrent(list);
    }

	
	public void showMenuSettings()
	{
    	Form menu= new MenuSettings("Settings", this);
        this.display.setCurrent(menu);
    }

    
    /// GPS ///
    
    public void toggleGpsLocationUpdates()
    {
    	if (this.gpsProvider == null)
    	{
	   		this.messageQueue.addMessage("No GPS API found!", null, 5000);    		
    		return;
    	}
    	
		this.messageQueue.delete("togglegps");

		if (this.gpsProvider.isUpdatingLocation())
		{
	   		this.messageQueue.addMessage("GPS disabled!", "togglegps", 5000);    		
	   		this.gpsProvider.stopLocationUpdates();
		}
    	else
    	{
	   		this.messageQueue.addMessage("Activating GPS...", "togglegps", 0);
	   		this.isFirstLocationUpdate = true;
	   		this.gpsProvider.startLocationUpdates();
    	}
    }

	
	/**
	 * Called by GpsProvider when the devices' GPS coordinates have been updated.
	 * @see GpsProvider
	 * @see GpsListener
	 */
	public void locationUpdated(GpsCoordinates c)
	{
		if (this.isFirstLocationUpdate)
		{
			this.isFirstLocationUpdate = false;
			this.messageQueue.delete("togglegps");
	   		this.messageQueue.addMessage("GPS active!", "togglegps", 5000);    		
		}
		
		LatLon gpsPos = new LatLon(c.getLatitude(), c.getLongitude());

		this.mapCanvas.setDevicePosition(gpsPos);
		
		if (this.isFollowingMe && this.communicator != null)
			this.sendTargetPosition(gpsPos);
	}
	
	
	/// Bluetooth ///
    
    public void startBluetoothDeviceSearch()
    {
    	if (this.btProvider == null)
    	{
        	this.messageQueue.addMessage("No Bluetooth API fount!", null, 5000);
        	return;
    	}
    	
    	this.messageQueue.addMessage("Searching BT Devices...", "searchbt", 0);
    	this.btProvider.startSearch();
    }
    
    
	/**
	 * Called by BluetoothProvider when a device search has been completed.
	 * @see BluetoothProvider
	 * @see BluetoothListener
	 */
	public void deviceSearchCompleted(BluetoothDevice[] deviceList)
	{
    	this.messageQueue.delete("searchbt");
    	this.showMenuBluetoothDevices(deviceList);
    }

	
	/// MK ///
	
	public void sendMKInitCommands()
	{
		if (this.getCommunicator() == null)
			return;
		
		Utils.sleep(250);
		
		RedirectUartCommand cmd1 = new RedirectUartCommand();
		cmd1.setUart(UartSelector.NaviCtrl);
		cmd1.send(this.getCommunicator());
		
		Utils.sleep(250);
		
		ReadOsdDataCommand cmd2 = new ReadOsdDataCommand();
		cmd2.setSendingInterval(100);
		cmd2.send(this.getCommunicator());
	}
    
    public void sendLinkTest()
    {
    	if (this.communicator == null)
    	{
        	this.messageQueue.addMessage("Not connected!", null, 5000);
    		return;
    	}
    	
    	int pattern = (int)(System.currentTimeMillis() & 0xFFFF);
    	pattern = 50000;

    	this.messageQueue.addMessage("Sending pattern '" + pattern + "'...", "sendtest", 5000);
    	
       	SerialLinkTestCommand cmd = new SerialLinkTestCommand();
       	cmd.setEchoPattern(pattern);
   		cmd.send(this.communicator);
    }

    
    public void sendTargetPosition(LatLon pos)
    {
    	if (this.communicator == null)
    	{
        	this.messageQueue.addMessage("Not connected!", null, 5000);
    		return;
    	}
    	
    	this.messageQueue.addMessage("Sending position...", "sendpos", 0);
    	
		WayPoint wp = this.createWayPoint(pos);
		wp.ToleranceRadius = 0; // 0, so wp won't get hit and mk doesn't fly back to home pos
		wp.HoldTime = 250;
		
		SetTargetPositionCommand cmd = new SetTargetPositionCommand();
		cmd.setWayPoint(wp);
		cmd.send(this.communicator);
    	
    	this.messageQueue.delete("sendpos");
    	this.messageQueue.addMessage("Position sent!", "sendpos", 1000);
    }
    
    
    public void sendWayPointList(Vector latLongList)
    {
    	if (this.communicator == null)
    	{
        	this.messageQueue.addMessage("Not connected!", null, 5000);
    		return;
    	}
    	
    	this.messageQueue.addMessage("Sending waypoints...", "sendwps", 0);
    	
    	// reset wp list
       	WriteWayPointCommand cmd = new WriteWayPointCommand();
       	cmd.getWayPoint().Position.status = GpsPosStatus.Invalid;
   		cmd.send(this.communicator);
   		
   		if (latLongList == null)
   		{
   	    	this.messageQueue.addMessage("No waypoints selected!", "sendwps", 5000);
   			return;
   		}

    	// send new wps
    	for (int i = 0; i < latLongList.size(); i++)
    	{
        	cmd = new WriteWayPointCommand();
        	WayPoint wp = this.createWayPoint((LatLon)latLongList.elementAt(i));
        	cmd.setWayPoint(wp);
    		cmd.send(this.communicator);

    		Utils.sleep(250); // sleep a while so separate beeps can be heard from MK
    	}
    	
    	this.messageQueue.delete("sendwps");
    	this.messageQueue.addMessage("Waypoints sent!", null, 5000);
    }

    
	/**
	 * Called by MKCommunicator when a new response from the MK has arrived.
	 * @see MKCommunicator
	 * @see MKResponseListener
	 */
	public void responseReceived(MKResponse response)
	{
		if (response.getClass() == ReadOsdDataResponse.class)
		{
			ReadOsdDataResponse resp = (ReadOsdDataResponse)response;
			
			// mk pos
			double lat = resp.getNaviData().CurrentPosition.latitude / 10000000.0;
			double lon = resp.getNaviData().CurrentPosition.longitude / 10000000.0;
			this.mapCanvas.setMKPosition(new LatLon(lat, lon));

			// mk home pos
			lat = resp.getNaviData().HomePosition.latitude / 10000000.0;
			lon = resp.getNaviData().HomePosition.longitude / 10000000.0;
			this.mapCanvas.setMkHomePos(new LatLon(lat, lon));

			// operation radius
			int radius = resp.getNaviData().OperatingRadius;
			this.mapCanvas.setOperationRadius(radius);
		}
		
		if (response.getClass() == FollowMeResponse.class)
		{
			// mk pos
			double lat = ((FollowMeResponse)response).getWaypoint().Position.latitude / 10000000.0;
			double lon = ((FollowMeResponse)response).getWaypoint().Position.longitude / 10000000.0;
			this.mapCanvas.setMKPosition(new LatLon(lat, lon));
		}
		
		if (response.getClass() == WriteWayPointResponse.class)
		{
			int wpCount = ((WriteWayPointResponse)response).getWayPointCount();
			this.messageQueue.addMessage("WP #" + wpCount + " received by MK", null, 2000);
		}
		
		if (response.getClass() == SerialLinkTestResponse.class)
		{
			int pattern = ((SerialLinkTestResponse)response).getEchoPattern();
			this.messageQueue.addMessage("Pattern '" + pattern + "' received", null, 5000);
		}
		
	}
	
	
	/// Private Methods ///
	
	private void restoreState()
	{
		OsmPosition pos = RecordStoreHelper.loadMapPosition();
		if (pos != null)
			this.mapCanvas.setMapPosition(pos);
		
		OsmTileLoader.getInstance().RestoreState();
	}
	
	
	private void saveState()
	{
    	RecordStoreHelper.saveMapPosition(this.mapCanvas.getMapPosition());
    	OsmTileLoader.getInstance().SaveState();
	}
	
	
	private WayPoint createWayPoint(LatLon pos)
	{
		WayPoint wp = new WayPoint();
		wp.Position.status = GpsPosStatus.NewData;
		wp.Position.latitude = (int)(pos.latitude * 10000000);
		wp.Position.longitude = (int)(pos.longitude * 10000000);
		wp.ToleranceRadius = this.settings.getWPToleranceRadius();
		wp.HoldTime = this.settings.getWPHoldTime();
		
		return wp;
	}
}
