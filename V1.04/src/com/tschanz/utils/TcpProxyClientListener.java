package com.tschanz.utils;

import java.io.DataInputStream;
import java.io.IOException;


public interface TcpProxyClientListener
{
	void ProxyMessageReceived(String msgName, DataInputStream msgData) throws IOException;
}
