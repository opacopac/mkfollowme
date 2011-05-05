package com.tschanz.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class TcpProxyClient implements Runnable
{
	/// Fields ///
	
	private String serverUrl;
	private Vector listeners;
	private String outMsgName;
	private byte[] outMessageData;

	
	/// Constructors ///
	
	public TcpProxyClient(String serverUrl)
	{
		this.serverUrl = serverUrl;
		this.listeners = new Vector();
	}
	
	
	/// Public Methods ///
	
	public void addListener(TcpProxyClientListener listener)
	{
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}
	
	
	public void removeListener(TcpProxyClientListener listener)
	{
		if (this.listeners.contains(listener))
			this.listeners.removeElement(listener);
	}
	
	
	public void sendData(String msgName, byte[] msgData)
	{
		this.outMsgName = msgName;
		this.outMessageData = msgData;
	}
	
	
	public void start()
	{
		new Thread(this).start();
	}
	
	
	public void run()
	{
		StreamConnection connection = null;
		
		while (true) // TODO
		{
			try
			{
				if (this.serverUrl != null && this.serverUrl.length() > 0)
				{
					connection = (StreamConnection)Connector.open(this.serverUrl);
					InputStream inStream = connection.openInputStream(); 
					OutputStream outStream = connection.openOutputStream(); 
					DataInputStream dis = new DataInputStream(inStream);
					DataOutputStream dos = new DataOutputStream(outStream);
					
					while (true) // TODO
					{
						if (dis.available() > 0)
						{
							String name = dis.readUTF();
							this.notifyProxyMessageReceived(name, dis);
						}
						
						if (this.outMsgName != null)
						{
							dos.writeUTF(this.outMsgName);
							if (this.outMessageData != null)
								dos.write(this.outMessageData);
							
							this.outMsgName = null;
							this.outMessageData = null;
						}
						
						Utils.sleep(100);
					}
				}
			}
			catch (IOException e)
			{
				Logger.Log(e);
			}
			finally
			{
				try
				{
					if (connection != null)
						connection.close();
				}
				catch (IOException e)
				{
					Logger.Log(e);
				}
			}
			
			Utils.sleep(2000);
		}	
	}
	
	
	/// Private Methods ///
	
	private void notifyProxyMessageReceived(String msgName, DataInputStream msgData) throws IOException
	{
    	for (int i = 0; i < this.listeners.size(); i++)
    		((TcpProxyClientListener)this.listeners.elementAt(i)).ProxyMessageReceived(msgName, msgData);
    }
}
