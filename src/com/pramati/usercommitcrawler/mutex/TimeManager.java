package com.pramati.usercommitcrawler.mutex;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicLong;

public class TimeManager {

	private static AtomicLong _userThreadCodeExecutionTime = new AtomicLong(0);
	private static AtomicLong _userThreadSytemExecutionTime = new AtomicLong(0);

	 public static void addUserTime(long sum) {

		if(sum > 0)
		 _userThreadCodeExecutionTime.addAndGet(sum);

	}
	 
	 public static void addSystemTime(long sum) {
		 
		if(sum > 0)
		 _userThreadSytemExecutionTime.addAndGet(sum);

	}
	 
	 public static long getSystemTime(long id) {

			ThreadMXBean bean = ManagementFactory.getThreadMXBean();

			return bean.isCurrentThreadCpuTimeSupported() ? (bean
					.getThreadCpuTime(id) - bean.getThreadUserTime(id)) : 0L;
		}

		public static long getUserTime(long id) {

			ThreadMXBean bean = ManagementFactory.getThreadMXBean();

			return bean.isCurrentThreadCpuTimeSupported() ? bean
					.getThreadUserTime(id) : 0L;
		}


	public static Long getTotalExecutionTime() {
		return _userThreadCodeExecutionTime.get() + _userThreadSytemExecutionTime.get();
	}

	public static long get_userThreadCodeExecutionTimeValue() {
		return _userThreadCodeExecutionTime.get();
	}

	public static long get_userThreadSytemExecutionTimeValue() {
		return _userThreadSytemExecutionTime.get();
	}
	
	public static AtomicLong get_userThreadCodeExecutionTime() {
		return _userThreadCodeExecutionTime;
	}

	public static AtomicLong get_userThreadSytemExecutionTime() {
		return _userThreadSytemExecutionTime;
	}



}
