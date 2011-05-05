/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.parameter.navictrl;

public class UartSelector
{
    public static final int FlightCtrl = 0;
    public static final int MK3Mag = 1;
    public static final int MK3GPS = 2;
    public static final int NaviCtrl = -1; // doesn't have an own id-byte, command sends magic reset-sequence instead if selected
    
    
    private UartSelector()
    {
    }
}
