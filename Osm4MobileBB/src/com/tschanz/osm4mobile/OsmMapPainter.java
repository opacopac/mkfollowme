/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import henson.midp.Float11;

public class OsmMapPainter
{
	/// Fields ///
	
	
	/// Constructors ///
	
	private OsmMapPainter()
	{
	}
	
	
	/// Public Methods ///	
	
	public static void drawMap(Graphics g, OsmPosition mapCoordinates)
	{
		OsmPosition tilePos;
		int centerTileX = mapCoordinates.getTileNumX();
		int centerTileY = mapCoordinates.getTileNumY();
		int zoom = mapCoordinates.getZoomLevel();
		
		// out -> in (last image put in queue will be loaded first)
		for (int r = getMaxTileRad(g); r >= 0; r--)
		{
			for (int i = -r; i <= r; i++)
			{
				// top row
		        tilePos = new OsmPosition(zoom, centerTileX + i, centerTileY - r);
		        drawTile(g, mapCoordinates, tilePos);
		        
		        if (r > 0)
		        {
			        // bottom row
			        tilePos = new OsmPosition(zoom, centerTileX + i, centerTileY + r);
			        drawTile(g, mapCoordinates, tilePos);
		        }
		        
		        if (i > -r && i < r)
		        {
			        // left column
		        	tilePos = new OsmPosition(zoom, centerTileX - r, centerTileY + i);
			        drawTile(g, mapCoordinates, tilePos);

			        // right column
		        	tilePos = new OsmPosition(zoom, centerTileX + r, centerTileY + i);
			        drawTile(g, mapCoordinates, tilePos);
		        }
			}
		}
	}
	
	
	public static void drawCrossHair(Graphics g)
	{
		int anchorX = getAnchorX(g);
		int anchorY = getAnchorY(g);
		
		g.setColor(0x000000);
		g.drawLine(anchorX, anchorY - 10, anchorX, anchorY -5);
		g.drawLine(anchorX, anchorY + 5, anchorX, anchorY + 10);
		g.drawLine(anchorX - 10, anchorY, anchorX - 5, anchorY);
		g.drawLine(anchorX + 5, anchorY, anchorX + 10, anchorY);
	}
	
	
	public static void drawPositionMark(Graphics g, OsmPosition mapCoordinates, LatLon pos, int colorRGB)
	{
		OsmPosition markPos = new OsmPosition(mapCoordinates.getZoomLevel(), pos);
		
		long x = getScreenX(markPos, mapCoordinates, g);
		long y = getScreenY(markPos, mapCoordinates, g);
		
		if (!isOutOfScreen(g, x, y, 10))
		{
			g.setColor(colorRGB);
			g.fillArc((int)x - 5, (int)y - 5, 10, 10, 0, 360);
		}
	}
	
	
	public static void drawCircle(Graphics g, OsmPosition mapCoordinates, LatLon pos, int radiusMeter, int colorRGB)
	{
		OsmPosition markPos = new OsmPosition(mapCoordinates.getZoomLevel(), pos);
		
		long x = getScreenX(markPos, mapCoordinates, g);
		long y = getScreenY(markPos, mapCoordinates, g);
		int r = mapCoordinates.getPixelDistX(radiusMeter);
		
		if (!isOutOfScreen(g, x, y, r))
		{
			g.setColor(colorRGB);
			g.drawArc((int)x - r, (int)y - r, r * 2, r * 2, 0, 360);
		}
	}
	
	
	public static void drawLine(Graphics g, OsmPosition mapCoordinates, LatLon posStart, LatLon posEnd, int colorRGB)
	{
		OsmPosition linePosStart = new OsmPosition(mapCoordinates.getZoomLevel(), posStart);
		OsmPosition linePosEnd = new OsmPosition(mapCoordinates.getZoomLevel(), posEnd);
		
		long x1 = getScreenX(linePosStart, mapCoordinates, g);
		long y1 = getScreenY(linePosStart, mapCoordinates, g);
		long x2 = getScreenX(linePosEnd, mapCoordinates, g);
		long y2 = getScreenY(linePosEnd, mapCoordinates, g);
		
		g.setColor(colorRGB);
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}
	
	
	public static void drawScale(Graphics g, OsmPosition mapCoordinates)
	{
		int scaleMeter;
		String text;
		
		double maxMeter = mapCoordinates.getMeterDistX(g.getClippingRect().width / 2);
		double mag = Float11.pow(10, Math.floor(Float11.log10(maxMeter)));
		
		if (maxMeter > 5 * mag)
			scaleMeter = (int)(5 * mag);
		else if (maxMeter > 2 * mag)
			scaleMeter = (int)(2 * mag);
		else
			scaleMeter = (int)mag;
		
		int scalePixel = mapCoordinates.getPixelDistX(scaleMeter);
		
		if (scaleMeter < 1000)
			text = (new Integer(scaleMeter)).toString() + "m";
		else
			text = (new Integer(scaleMeter / 1000)).toString() + "km";

		g.setColor(0x000000);
		g.drawLine(5, g.getClippingRect().height - 5, 5 + scalePixel, g.getClippingRect().height - 5);
		g.drawLine(5, g.getClippingRect().height - 5, 5, g.getClippingRect().height - 7);
		g.drawLine(5 + scalePixel, g.getClippingRect().height - 5, 5 + scalePixel, g.getClippingRect().height - 7);
		g.drawText(text, 7, g.getClippingRect().height - 7, Graphics.BOTTOM | Graphics.LEFT);
	}
	
	
	private static void drawTile(Graphics g, OsmPosition mapCoordinates, OsmPosition tilePos)
	{
        int screenX = (int)getScreenX(tilePos, mapCoordinates, g);
        int screenY = (int)getScreenY(tilePos, mapCoordinates, g);
        
        if (!isTileOutOfScreen(g, screenX, screenY, 0))
        {        
	        Bitmap img = OsmTileLoader.getInstance().getMapImage(tilePos);
	        if (img != null)
	        	//g.drawImage(screenX, screenY,img.getWidth(),img.getHeight() ,img,1, Graphics.TOP , Graphics.LEFT);
	        	g.drawBitmap(screenX, screenY, img.getWidth(),img.getHeight(), img, 0 , 0);
        }
	}
	
	
	private static boolean isOutOfScreen(Graphics g, long x, long y, int tolerance)
	{
		if (x < -tolerance)
			return true;
		
		if (y < -tolerance)
			return true;
		
		if (x > g.getClippingRect().width + tolerance)
			return true;
		
		if (y > g.getClippingRect().height + tolerance)
			return true;
		
		return false;
	}
	
	
	private static boolean isTileOutOfScreen(Graphics g, long x, long y, int tolerance)
	{
		long intersectX = Math.max(x, -tolerance);
		long intersectX2 = Math.min(x + OsmPosition.TILE_PIXEL_WIDTH, g.getClippingRect().width + tolerance);
		long intersectY = Math.max(y, -tolerance);
		long intersectY2 = Math.min(y + OsmPosition.TILE_PIXEL_HEIGHT, g.getClippingRect().height + tolerance);
		
		if (intersectX > intersectX2 || intersectY > intersectY2)
			return true;
		else
			return false;
	}
	
	
	private static int getMaxTileRad(Graphics g)
	{
		double propX = g.getClippingRect().width / (double)OsmPosition.TILE_PIXEL_WIDTH;
		double propY = g.getClippingRect().height / (double)OsmPosition.TILE_PIXEL_HEIGHT;

		return (int)Math.ceil(Math.max(propX, propY));
	}

	
	private static int getAnchorX(Graphics g)
	{
		//return (g.getClipWidth() - g.getClipX()) / 2;
		return (g.getClippingRect().width - g.getClippingRect().x) / 2;
	}
	
	
	private static int getAnchorY(Graphics g)
	{
		///return (g.getClipHeight() - g.getClipY()) / 2;
		return (g.getClippingRect().height - g.getClippingRect().y) / 2;
	}
	
	
	private static long getScreenX(OsmPosition pos, OsmPosition mapCoordinates, Graphics g)
	{
		return (long)((pos.getTilePosX() - mapCoordinates.getTilePosX()) * OsmPosition.TILE_PIXEL_WIDTH) + getAnchorX(g);
	}


	private static long getScreenY(OsmPosition pos, OsmPosition mapCoordinates, Graphics g)
	{
		return (long)((pos.getTilePosY() - mapCoordinates.getTilePosY()) * OsmPosition.TILE_PIXEL_HEIGHT) + getAnchorY(g);
	}
}
