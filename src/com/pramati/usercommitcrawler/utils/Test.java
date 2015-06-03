package com.pramati.usercommitcrawler.utils;

import org.joda.time.DateTime;

public class Test {
	
	public static void main(String[] args)
	{
		//DateTime dateTime = new DateTime();
		DateTime lastCommit = DateTime.now().minusYears(100);
		DateTime today = DateTime.now().minusYears(100);
		
		if(lastCommit.getYear() == today.getYear() && lastCommit.getMonthOfYear() == today.getMonthOfYear()
				&& lastCommit.getDayOfMonth() == today.getDayOfMonth())
		{
			System.out.println("ha hah aha");
		}
		System.out.println(lastCommit);
		System.out.println(today);
		System.out.println(lastCommit.getYear());
		System.out.println(lastCommit.getMonthOfYear());
		System.out.println(lastCommit.getDayOfMonth() );
	}
}
