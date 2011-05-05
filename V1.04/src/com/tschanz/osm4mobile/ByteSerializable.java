/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.osm4mobile;

import java.io.IOException;

public interface ByteSerializable
{
	byte[] getAsByteArray() throws IOException;
	
	void loadFromByteArray(byte[] bytes) throws IOException;
}
