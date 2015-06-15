package com.pramati.usercommitcrawler.beans;

public class RepositoryCommitHistory {
	
	
	public String repoSitoryName;
	public StringBuilder dayBeforeYesrdaysCommit = new StringBuilder();
	public StringBuilder yesterdaysCommit = new StringBuilder();
	public StringBuilder todaysCommit = new StringBuilder();
	public Long networkWatitingTime = new Long(0);
	
	public StringBuilder getDayBeforeYesrdaysCommit() {
		return dayBeforeYesrdaysCommit;
	}
	public void setDayBeforeYesrdaysCommit(StringBuilder dayBeforeYesrdaysCommit) {
		this.dayBeforeYesrdaysCommit = dayBeforeYesrdaysCommit;
	}
	public StringBuilder getYesterdaysCommit() {
		return yesterdaysCommit;
	}
	public void setYesterdaysCommit(StringBuilder yesterdaysCommit) {
		this.yesterdaysCommit = yesterdaysCommit;
	}
	public StringBuilder getTodaysCommit() {
		return todaysCommit;
	}
	public void setTodaysCommit(StringBuilder todaysCommit) {
		this.todaysCommit = todaysCommit;
	}
	public String getRepoSitoryName() {
		return repoSitoryName;
	}
	public void setRepoSitoryName(String repoSitoryName) {
		this.repoSitoryName = repoSitoryName;
	}
	public Long getNetworkWatitingTime() {
		return networkWatitingTime;
	}
	public void setNetworkWatitingTime(Long networkWatitingTime) {
		this.networkWatitingTime = networkWatitingTime;
	}

}
