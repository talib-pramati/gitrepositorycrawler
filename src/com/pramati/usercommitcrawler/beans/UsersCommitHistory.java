package com.pramati.usercommitcrawler.beans;

import java.util.ArrayList;
import java.util.List;

public class UsersCommitHistory {
	
	List<UserCommitHistory> usersCommitHistory = new ArrayList<UserCommitHistory>();

	public List<UserCommitHistory> getUserCommitHistory() {
		return usersCommitHistory;
	}

	public void setUserCommitHistory(List<UserCommitHistory> userCommitHistory) {
		this.usersCommitHistory = userCommitHistory;
	}

}
