/* 
 *   Author:       Armand Tschanz (armand@tschanz.com)
 *   License:      http://creativecommons.org/licenses/by-nc-sa/2.5/ch/
 *                 (Creative Commons: Attribution / Noncommercial / Share Alike)
 *   See README file for further info 
 */

package com.tschanz.mklib4mobile.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.tschanz.mklib4mobile.MKInputStream;
import com.tschanz.mklib4mobile.response.navictrl.FollowMeResponse;
import com.tschanz.mklib4mobile.response.navictrl.ReadOsdDataResponse;
import com.tschanz.mklib4mobile.response.navictrl.ReadWayPointResponse;
import com.tschanz.mklib4mobile.response.navictrl.SerialLinkTestResponse;
import com.tschanz.mklib4mobile.response.navictrl.WriteWayPointResponse;
import com.tschanz.utils.Logger;

public class ResponseParser
{
    /// Fields ///

    private final static char RESPONSE_START_CHAR = '#';


    /// Public Methods ///

    public static MKResponse createResponse(byte[] rawBytes)
    {
        // Format: #(1) + slaveAddress(1) + responseId(1) + parameterData(n) + crc(2)
        
        if (rawBytes == null)
            return null;

        if (rawBytes.length < 5)
            return null;
        
		if ((rawBytes[0] & 0xFF) != RESPONSE_START_CHAR)
            return null;

        if (!isCrcCorrect(rawBytes))
            return null;

		int slaveAddress = rawBytes[1] & 0xFF;
        int responseId = rawBytes[2] & 0xFF;
        byte[] paramBytes = decodePseudo64(rawBytes, 3, rawBytes.length - 2);

        MKResponse response = CreateInstance((char)responseId);
        
        if (response != null)
        {
            response.setSlaveAddress(slaveAddress - 'a');
        
			ByteArrayInputStream bis = new ByteArrayInputStream(paramBytes);
			MKInputStream dis = new MKInputStream(bis);
	
			try
			{
	            response.readFromInputStream(dis);
			}
			catch (IOException e)
			{
				Logger.Log(e);
			}
        }

        return response;
    }

    
    /// Private Methods ///

    private static byte[] decodePseudo64(byte[] bytes, int offset, int length)
    {
    	ByteArrayOutputStream decodedBytes = new ByteArrayOutputStream();
        int p = offset;

        while (p < length - 3)
        {
            int a, b, c, d, x, y, z;

            a = bytes[p++] - '=';
            b = bytes[p++] - '=';
            c = bytes[p++] - '=';
            d = bytes[p++] - '=';

            x = (a << 2) | (b >> 4);
            y = ((b & 0x0f) << 4) | (c >> 2);
            z = ((c & 0x03) << 6) | d;

            decodedBytes.write(x);
            decodedBytes.write(y);
            decodedBytes.write(z);
        }
        
        return decodedBytes.toByteArray();
    }


    private static boolean isCrcCorrect(byte[] rawBytes)
    {
        int crc1 = rawBytes[rawBytes.length - 2] & 0xFF; // convert to u8
        int crc2 = rawBytes[rawBytes.length - 1] & 0xFF; 

        int crc = 0;

        for (int i = 0; i < rawBytes.length - 2; i++)
            crc += (rawBytes[i] & 0xFF);

        crc %= 4096;

        if (crc1 != crc / 64 + '=')
            return false;

        if (crc2 != crc % 64 + '=')
            return false;


        return true;
    }


    private static MKResponse CreateInstance(char responseId)
    {
        switch (responseId)
        {
            // Common
//            case 'A': return new ReadAnalogValuesLabelsResponse();
//            case 'B': return new ExternControlResponse();
//            case 'H': return new ReadDisplayLineResponse();
//            case 'L': return new ReadDisplayResponse();
//            case 'V': return new ReadVersionResponse();
//            case 'G': return new ReadExternControlResponse();
//            case 'D': return new ReadDebugDataResponse();
//            case 'C': return new ReadData3DResponse();

            // FlightCtrl
//            case 'w': return new AttitudeResponse();
//            case 'Q': return new ReadSettingResponse();
//            case 'S': return new WriteSettingsResponse();
//            case 'P': return new ReadPpmChannelsResponse();
//            case 'N': return new ReadMixerResponse();
//            case 'M': return new WriteMixerResponse();

            // NaviCtrl
            case 'Z': return new SerialLinkTestResponse();
//            case 'E': return new ReadErrorTextResponse();
            case 'W': return new WriteWayPointResponse();
            case 'X': return new ReadWayPointResponse();
            case 'O': return new ReadOsdDataResponse();
            case 's': return new FollowMeResponse();

            // MK2Mag
//            case 'K': return new ReadHeadingResponse();

            default: return null;
        }
    }
}
