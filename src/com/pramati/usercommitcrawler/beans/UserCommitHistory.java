package com.pramati.usercommitcrawler.beans;

public class UserCommitHistory {
	
	private String userName;
	private String repoURL;
	private StringBuilder yesterDaysCommit;
	private StringBuilder todaysCommit;
	private StringBuilder lastCommit;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public StringBuilder getYesterDaysCommit() {
		return yesterDaysCommit;
	}
	public void setYesterDaysCommit(StringBuilder yesterDaysCommit) {
		this.yesterDaysCommit = yesterDaysCommit;
	}
	public StringBuilder getTodaysCommit() {
		return todaysCommit;
	}
	public void setTodaysCommit(StringBuilder todaysCommit) {
		this.todaysCommit = todaysCommit;
	}
	public StringBuilder getLastCommit() {
		return lastCommit;
	}
	public void setLastCommit(StringBuilder lastCommit) {
		this.lastCommit = lastCommit;
	}
	public String getRepoURL() {
		return repoURL;
	}
	public void setRepoURL(String repoURL) {
		this.repoURL = repoURL;
	}

}
