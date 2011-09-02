/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.utils;

import com.tschanz.osm4mobile.OsmDemoMidlet;

/**
 * Class for handling error logging.
 */
public class Logger
{
	private Logger()
	{
	}
	
	
	public static void Log(Exception e)
	{
		OsmDemoMidlet.log("class="+e.getClass()+",error="+e.getMessage());
	}
	
	
	public static void Log(String message)
	{
		OsmDemoMidlet.log(message);
	}
}
