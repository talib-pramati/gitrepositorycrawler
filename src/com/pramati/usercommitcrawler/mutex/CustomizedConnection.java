package com.pramati.usercommitcrawler.mutex;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

public class CustomizedConnection {
	
	private volatile static CustomizedConnection _instance;
	private static Long networkConnectionTime = new Long(0) ;
	private boolean isConnectionBusy = false;
	
	private CustomizedConnection(){
	
	}
	
	public static CustomizedConnection getInstance()
	{
		if(_instance == null)
		{
			synchronized(CustomizedConnection.class)
			{
				_instance = new CustomizedConnection();
			}
		}
		
		return _instance;
	}
	
	public synchronized Connection makeConnection(String url)
	{
		while(isConnectionBusy);
		long nanoStartTime = System.nanoTime();
		Connection connect = HttpConnection.connect(url);
		long nanoEndTime = System.nanoTime();
		networkConnectionTime = networkConnectionTime + (nanoEndTime - nanoStartTime);
		isConnectionBusy = false;
		return connect;
	}

	public static Long getNetworkConnectionTime() {
		return networkConnectionTime;
	}

}
