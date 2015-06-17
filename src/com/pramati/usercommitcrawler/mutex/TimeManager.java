package com.pramati.usercommitcrawler.mutex;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicLong;

public class TimeManager {

	private static AtomicLong _userThreadCodeExecutionTime = new AtomicLong(0);
	private static AtomicLong _userThreadSytemExecutionTime = new AtomicLong(0);

	public static void addTime(long id) {

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		if (bean.isCurrentThreadCpuTimeSupported()) {
			_userThreadCodeExecutionTime.addAndGet(bean.getThreadUserTime(id));
			_userThreadSytemExecutionTime.addAndGet(bean.getThreadCpuTime(id) - bean.getThreadUserTime(id));
			
			
		}

	}

	public static Long getTotalExecutionTime() {
		return _userThreadCodeExecutionTime.get() + _userThreadSytemExecutionTime.get();
	}

	public static long get_userThreadCodeExecutionTime() {
		return _userThreadCodeExecutionTime.get();
	}

	public static long get_userThreadSytemExecutionTime() {
		return _userThreadSytemExecutionTime.get();
	}



}
