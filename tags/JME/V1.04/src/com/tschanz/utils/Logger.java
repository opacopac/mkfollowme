/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.utils;

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
		e.printStackTrace();
	}
	
	
	public static void Log(String message)
	{
		System.out.print(message);
	}
}
