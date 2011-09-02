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

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;

public class OsmDemoMidlet extends UiApplication
{
	/// Fields ///
	
	private static OsmDemoCanvas mapCanvas;
	private static OsmDemoMidlet app;

	
	public static void main(String[] args){
		app=new OsmDemoMidlet();
		mapCanvas = new OsmDemoCanvas(app);
		log("Entro");
		app.pushScreen(mapCanvas);
		app.enterEventDispatcher();
		restoreLastMapPos();
	}
	
	public OsmDemoMidlet(){
		
	}
	
	/// Private Methods ///
	
	private static void restoreLastMapPos()
	{
		OsmPosition pos = RecordStoreHelper.loadMapPosition();
		
		if (pos != null)
			mapCanvas.setMapPosition(pos);
	}
	
	
	
	public static void mostrarMapa(){
		app.pushScreen(mapCanvas);
	}
	
	
	public static void log(String texto){
		/*Log.setActual(Log.getActual()+1);
		Log.updateRecord(Log.getActual(),texto);*/
		
	}
	
	public static void do_alert(String encabezado,String mensaje){
		final String mensg=encabezado+"::"+mensaje;
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {

			public void run()
			{
				synchronized (Application.getEventLock()) {
					try{
						UiEngine ui = Ui.getUiEngine();
						Dialog diag=new Dialog(Dialog.D_OK,mensg,Dialog.OK,Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION),net.rim.device.api.ui.Manager.VERTICAL_SCROLL);
						ui.pushGlobalScreen(diag, 1, UiEngine.GLOBAL_QUEUE);
					}catch(Exception e){

					}
				}
			}
		});
	}
	
	public static void do_alert(String message){
		OsmDemoMidlet.do_alert("",message);
	}
}
