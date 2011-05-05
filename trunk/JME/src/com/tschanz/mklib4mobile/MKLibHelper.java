/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile;

import java.util.Vector;

import com.tschanz.utils.Logger;

public class MKLibHelper
{
	private MKLibHelper()
	{
	}
	
	
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
