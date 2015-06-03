package com.pramati.usercommitcrawler.beans;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserInformation {
	
	private ConcurrentHashMap<String, String> userRepositoryURLMap = new ConcurrentHashMap<String, String>();
	private Queue<String> userNameQ = new ConcurrentLinkedQueue<String>();
	public ConcurrentHashMap<String, String> getUserRepositoryURLMap() {
		return userRepositoryURLMap;
	}
	public void setUserRepositoryURLMap(
			ConcurrentHashMap<String, String> userRepositoryURLMap) {
		this.userRepositoryURLMap = userRepositoryURLMap;
	}
	public Queue<String> getUserNameQ() {
		return userNameQ;
	}
	public void setUserNameQ(Queue<String> userNameQ) {
		this.userNameQ = userNameQ;
	}
}
