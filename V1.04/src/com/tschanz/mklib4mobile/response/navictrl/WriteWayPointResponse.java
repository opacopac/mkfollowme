/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.response.navictrl;

import java.io.IOException;

import com.tschanz.mklib4mobile.MKInputStream;
import com.tschanz.mklib4mobile.response.MKResponse;

public class WriteWayPointResponse extends MKResponse
{
    /// Fields ///

    private int wayPointCount; // u8
    
    
    /// Public Methods ///
    
    public int getWayPointCount()
    {
        return this.wayPointCount;
    }


    public void readFromInputStream(MKInputStream inStream) throws IOException
    {
        this.wayPointCount = inStream.readUnsignedByte();
    }
}