package com.pramati.usercommitcrawler.mutex;

import java.util.concurrent.atomic.AtomicLong;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

public class CustomizedConnection {
	
	private static AtomicLong networkConnectionTime = new AtomicLong(0l);
	
	public Connection makeConnection(String url)
	{
		long nanoStartTime = System.nanoTime();
		Connection connect = HttpConnection.connect(url);
		long nanoEndTime = System.nanoTime();
		networkConnectionTime.addAndGet(nanoEndTime- nanoStartTime);
		return connect;
	}

	public static Long getNetworkConnectionTimeValue() {
		return networkConnectionTime.get();
	}
	
	public static AtomicLong getNetworkConnectionTime() {
		return networkConnectionTime;
	}


}
