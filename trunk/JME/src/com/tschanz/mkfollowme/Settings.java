/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mkfollowme;

public class Settings
{
	private int waypointHoldTime;
	private int waypointToleranceRadius;

	
	public Settings()
	{
		this.waypointHoldTime = 5;
		this.waypointToleranceRadius = 2;
	}
	
	
	public void setWPHoldTime(int holdTime)
	{
		this.waypointHoldTime = holdTime;
	}
	
	
	public int getWPHoldTime()
	{
		return this.waypointHoldTime;
	}
	
	
	public void setWPToleranceRadius(int toleranceRadius)
	{
		this.waypointToleranceRadius = toleranceRadius;
	}
	
	
	public int getWPToleranceRadius()
	{
		return this.waypointToleranceRadius;
	}
}
