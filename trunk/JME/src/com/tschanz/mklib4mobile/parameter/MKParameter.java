/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.parameter;

import java.io.IOException;

import com.tschanz.mklib4mobile.MKInputStream;
import com.tschanz.mklib4mobile.MKOutputStream;

public abstract class MKParameter
{
	public abstract void writeToOutputStream(MKOutputStream outStream) throws IOException;

	public abstract void readFromInputStream(MKInputStream inStream) throws IOException;
}
