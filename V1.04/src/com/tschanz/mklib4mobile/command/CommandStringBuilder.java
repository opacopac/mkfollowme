/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.command;

import java.io.ByteArrayOutputStream;

public class CommandStringBuilder
{
    /// Fields ///

    private static final char START_SIGN = '#';
    private static final char END_OF_LINE = '\r';
    
    
    /// Constructors ///
    
    private CommandStringBuilder()
    {
    }


    /// Public Methods ///

    public static byte[] build(int slaveAddress, int commandId, byte[] parameters)
    {
    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    	outStream.write(START_SIGN);
    	outStream.write(slaveAddress + 'a');
    	outStream.write(commandId);
        writeEncodedParameters(outStream, parameters);
        writeCrc(outStream);
        outStream.write(END_OF_LINE);

        return outStream.toByteArray();
    }


    /// Private Methods ///

    private static void writeEncodedParameters(ByteArrayOutputStream outStream, byte[] parameters)
    {
        int param_pos, a, b, c;


        if (parameters == null || parameters.length == 0)
            return;


        for (param_pos = 0; param_pos < (parameters.length / 3 + (parameters.length % 3 == 0 ? 0 : 1)); param_pos++)
        {
            a = (param_pos * 3 < parameters.length) ? parameters[param_pos * 3] : 0;
            b = ((param_pos * 3 + 1) < parameters.length) ? parameters[param_pos * 3 + 1] : 0;
            c = ((param_pos * 3 + 2) < parameters.length) ? parameters[param_pos * 3 + 2] : 0;

            outStream.write('=' + (a >> 2));
            outStream.write('=' + (((a & 0x03) << 4) | ((b & 0xf0) >> 4)));
            outStream.write('=' + (((b & 0x0f) << 2) | ((c & 0xc0) >> 6)));
            outStream.write('=' + (c & 0x3f));
        }
    }


    private static void writeCrc(ByteArrayOutputStream outStream)
    {
        int crc = 0;
        byte[] bytes = outStream.toByteArray();

        for (int i = 0; i < bytes.length; i++)
            crc += bytes[i];

        crc %= 4096;

        outStream.write(crc / 64 + '=');
        outStream.write(crc % 64 + '=');
    }
}
