package com.pramati.usercommitcrawler.beans;

import java.util.ArrayList;
import java.util.List;

public class UserCommitHistory {

	private String userName;

	private List<RepositoryCommitHistory> repositoryCommitHistory = new ArrayList<RepositoryCommitHistory>();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<RepositoryCommitHistory> getRepositoryCommitHistory() {
		return repositoryCommitHistory;
	}

	public void setRepositoryCommitHistory(
			List<RepositoryCommitHistory> repositoryCommitHistory) {
		this.repositoryCommitHistory = repositoryCommitHistory;
	}

}
