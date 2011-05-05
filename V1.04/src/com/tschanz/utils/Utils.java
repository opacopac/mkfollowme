/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.utils;

import java.util.Vector;

/**
 * Helper class providing several static utility function.  
 */
public class Utils
{
	/**
	 * Private class constructor to prevent any instances.
	 */
	private Utils()
	{
	}
	
	
	/**
	 * Sleeps the current thread for a given time, catching possible exceptions.
	 * @param sleepTime - sleep time in milliseconds
	 */
	public static void sleep(long sleepTime)
	{
		try
		{
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
			Logger.Log(e);
		}		
	}
	
	
	/**
	 * Splits a string into an array of substrings using the given separator string. 
	 * @param str - the original string to split up into substrings
	 * @param separator - the separator char/string 
	 * @return array of substrings of the original string, split at the separators
	 */
    public static String[] splitString(String str, String separator)
    {
    	Vector nodes = new Vector();

    	int index = str.indexOf(separator);
    	while(index >= 0)
    	{
	    	nodes.addElement(str.substring(0, index));
	    	str = str.substring(index + separator.length());
	    	index = str.indexOf(separator);
    	}
    	
    	nodes.addElement(str);

    	String[] result = new String[nodes.size()];
    	if(nodes.size() > 0)
	    	for(int loop=0; loop < nodes.size(); loop++)
		    	result[loop] = (String)nodes.elementAt(loop);

    	return result;
    }
}
